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
package net.fhirfactory.pegacorn.communicate.matrix.methods;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.pegacorn.communicate.matrix.model.interfaces.MatrixEventForwarderInterface;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MatrixInstantMessageMethods {
    private static final Logger LOG = LoggerFactory.getLogger(MatrixInstantMessageMethods.class);

    private ObjectMapper jsonMapper;

    @Inject
    private MatrixEventForwarderInterface matrixEventForwarder;

    //
    // Constructor(s)
    //

    public MatrixInstantMessageMethods() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        jsonMapper.registerModule(module);
        jsonMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
    }

    //
    // Business Methods
    //

    public MAPIResponse syncRoomEvents(String roomID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    /**
     * Get a single event based on roomId/eventId. You must have permission to retrieve this event
     * e.g. by being a member in the room for this event.
     *
     * @param roomID
     * @param userID
     * @return
     */
    public MAPIResponse getRoomEvent(String roomID, String userID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postAudioMessage(String roomID, String userID, MRoomAudioMessageEvent audioMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postEmoteMessage(String roomID, String userID, MRoomEmoteMessageEvent emoteMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postFileMessage(String roomID, String userID, MRoomFileMessageEvent fileMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postImageMessage(String roomID, String userID, MRoomImageMessageEvent imageMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postLocationMessage(String roomID, String userID, MRoomLocationMessageEvent locationMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postNoticeMessage(String roomID, String userID, MRoomNoticeMessageEvent noticeMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse postTextMessage(String roomID, String userID, MRoomTextMessageEvent textMessage) {
        getLogger().debug(".postTextMessage(): Entry, roomID->{}, userID->{}, textMessage->{}", roomID, userID, textMessage);
        if (userID != null) {
            textMessage.setSender(userID);
        }
        if (roomID != null) {
            textMessage.setRoomIdentifier(roomID);
        }
        MAPIResponse response = matrixEventForwarder.forwardEventIntoMatrix(textMessage);
        getLogger().debug(".postTextMessage(): Exit, response->{}", response);
        return (response);
    }

    public MAPIResponse postVideoMessage(String roomID, String userID, MRoomVideoMessageEvent videoMessage) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }
}
