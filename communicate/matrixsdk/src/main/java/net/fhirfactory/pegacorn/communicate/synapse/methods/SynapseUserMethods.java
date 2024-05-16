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
package net.fhirfactory.pegacorn.communicate.synapse.methods;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.handler.codec.http.HttpMethod;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import net.fhirfactory.pegacorn.communicate.synapse.methods.common.SynapseAPIResponse;
import net.fhirfactory.pegacorn.communicate.synapse.model.SynapseAdminProxyInterface;
import net.fhirfactory.pegacorn.communicate.synapse.model.SynapseUser;
import net.fhirfactory.pegacorn.communicate.synapse.model.datatypes.SynapseQuery;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SynapseUserMethods {
    private static final Logger LOG = LoggerFactory.getLogger(SynapseUserMethods.class);

    ObjectMapper jsonMapper;

    @Inject
    SynapseAdminProxyInterface synapseProxy;

    @Produce
    private ProducerTemplate camelRouteInjector;

    public SynapseUserMethods() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        jsonMapper.registerModule(module);
        jsonMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
    }

    //
    // Business Methods
    //

    public MAPIResponse loginUser(String userId, String password) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // Rate Limit Functions
    public MAPIResponse overrideRateLimit(String userId, Integer transactionsPerSecond, Integer burstTransactions) {
        getLogger().debug(".overrideRateLimit(): Entry");
        SynapseQuery query = new SynapseQuery();
        //
        // Create the Query
        query.setHttpPath("/_synapse/admin/v1/users/" + userId + "/override_ratelimit");
        query.setHttpMethod(HttpMethod.POST.name());

        JSONObject bodyObject = new JSONObject();
        bodyObject.put("messages_per_second", transactionsPerSecond);
        bodyObject.put("burst_count", burstTransactions);

        query.setBody(bodyObject.toString());

        SynapseAPIResponse response = (SynapseAPIResponse) camelRouteInjector.sendBody(synapseProxy.getSynapseRoomActionIngresEndpoint(), ExchangePattern.InOut, query);
        getLogger().debug(".getALLAccounts(): response->{}", response);

        MAPIResponse taskResponse = new MAPIResponse();
        taskResponse.setResponseCode(response.getResponseCode());
        taskResponse.setResponseContent(response.getResponseContent());

        return (taskResponse);
    }

    public MAPIResponse getRateLimit(String userId) {

        MAPIResponse taskResponse = new MAPIResponse();
        return (taskResponse);
    }

    public MAPIResponse deleteRateLimit(String userId) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // User Account Details

    public MAPIResponse getUserAccountDetail(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    public MAPIResponse createUserAccount(SynapseUser practitioner) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    public MAPIResponse updateUserAccount(SynapseUser practitioner) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    public MAPIResponse listALLAccounts(Map<String, String> searchCriteria) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    public List<SynapseUser> getALLAccounts() {
        getLogger().debug(".getALLAccounts(): Entry");
        SynapseQuery query = new SynapseQuery();
        //
        // Create the Query
        query.setHttpPath("/_synapse/admin/v2/users");
        query.setHttpMethod(HttpMethod.GET.name());

        SynapseAPIResponse response = (SynapseAPIResponse) camelRouteInjector.sendBody(synapseProxy.getSynapseRoomActionIngresEndpoint(), ExchangePattern.InOut, query);
        getLogger().debug(".getALLAccounts(): response->{}", response);

        //
        // Extract the Rooms
        JSONObject localMessageObject = new JSONObject(response.getResponseContent());
        LOG.trace("getALLAccounts(): Converted to JSONObject --> " + localMessageObject.toString());
        JSONArray localMessageEvents = localMessageObject.getJSONArray("users");
        LOG.trace("getALLAccounts(): Converted to JSONArray, number of elements --> " + localMessageEvents.length());
        boolean processingIsSuccessful = true;

        String rawUserSet = localMessageEvents.toString();
        List<SynapseUser> userList = new ArrayList<>();
        try {
            userList = getJSONMapper().readValue(rawUserSet, new TypeReference<List<SynapseUser>>() {
            });
        } catch (JsonProcessingException e) {
            getLogger().error(".getALLAccounts(): Cannot convert retrieved room set, error->{}", ExceptionUtils.getStackTrace(e));
        }

        getLogger().info(".getALLAccounts(): Retrieved User Count->{}", userList.size());

        return (userList);
    }

    public MAPIResponse getUserSessions(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    public MAPIResponse deactivateUserAccount(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // User Membership

    public MAPIResponse getUserRooms(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // User Media

    public MAPIResponse getUserMedia(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // User Devices

    public MAPIResponse getUserDevices(String userID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    public ObjectMapper getJSONMapper() {
        return jsonMapper;
    }
}
