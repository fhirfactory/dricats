/*
 * Copyright (c) 2021 Mark A. Hunter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.communicate.matrix.issi.forwarder.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import net.fhirfactory.dricats.core.oam.metrics.cache.common.ITOpsReplicaLocalServerName;
import net.fhirfactory.pegacorn.communicate.matrix.credentials.MatrixAccessToken;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.MatrixEventBase;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.contenttypes.MEventTypeEnum;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.message.MRoomTextMessageEvent;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MatrixEventPreparationBean {
    private static final Logger LOG = LoggerFactory.getLogger(MatrixEventPreparationBean.class);
    private static String MATRIX_API_SUBPATH = "/_matrix/client/r0";
    private static String SYNAPSE_API_V1_SUBPATH = "_synapse/admin/v1/";
    private ObjectMapper jsonMapper;
    @Inject
    private MatrixAccessToken matrixAccessToken;

    @Inject
    private ITOpsReplicaLocalServerName localServerName;

    //
    // Constructor(s)
    //

    public MatrixEventPreparationBean() {
        this.jsonMapper = new ObjectMapper();
    }

    //
    // Business Method
    //

    public String createRequest(MatrixEventBase matrixEvent, Exchange camelExchange) {
        getLogger().debug(".createRequest(): Entry, matrixEvent->{}", matrixEvent);

        //
        // These are the values we have to populate
        String queryPath = null;
        HttpMethod method = null;
        String body = null;
        String queryParameters;

        //
        // Assume Good
        MEventTypeEnum mEventTypeEnum = MEventTypeEnum.fromString(matrixEvent.getEventType());

        String roomId = matrixEvent.getRoomIdentifier();
        String eventId = matrixEvent.getEventIdentifier();
        String userId = null;
        if (StringUtils.isEmpty(matrixEvent.getSender())) {
            userId = matrixAccessToken.getUserId();
        } else {
            userId = matrixEvent.getSender();
        }
        queryPath = MATRIX_API_SUBPATH + "/rooms/" + roomId + "/send/m.room.message/" + eventId + "?user_id=" + userId + "&throwExceptionOnFailure=false";
        //
        // Again, Assume Good
        switch (mEventTypeEnum) {
            case M_ROOM_MESSAGE:
                getLogger().debug(".createRequest(): Is a MRoomMessage");
                method = HttpMethod.PUT;
                if (matrixEvent instanceof MRoomTextMessageEvent) {
                    MRoomTextMessageEvent textMessageEvent = SerializationUtils.clone((MRoomTextMessageEvent) matrixEvent);
                    textMessageEvent.setSender(null);
                    textMessageEvent.setEventIdentifier(null);
                    textMessageEvent.setRoomIdentifier(null);
                    try {
                        body = jsonMapper.writeValueAsString(textMessageEvent.getContent());
                    } catch (JsonProcessingException e) {
                        getLogger().error(".createRequest(): Error, message->{}, stackTrace->{}", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
                    }
                }
                break;

            default:
        }

        //
        // Now set the Exchange (HTTP) Parameters

        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, method);
        camelExchange.getIn().setHeader(Exchange.HTTP_PATH, queryPath);
        camelExchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        camelExchange.getIn().setHeader("Authorization", "Bearer " + getMatrixAccessToken().getRemoteAccessToken());
        getLogger().debug(".createRequest(): Exit, body->{}", body);
        return (body);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected MatrixAccessToken getMatrixAccessToken() {
        return (matrixAccessToken);
    }

    protected ObjectMapper getJSONMapper() {
        return (jsonMapper);
    }
}
