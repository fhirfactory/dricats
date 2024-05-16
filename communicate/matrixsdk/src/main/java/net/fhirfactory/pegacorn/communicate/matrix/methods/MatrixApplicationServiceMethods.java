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
import io.netty.handler.codec.http.HttpMethod;
import net.fhirfactory.pegacorn.communicate.matrix.credentials.MatrixAccessToken;
import net.fhirfactory.pegacorn.communicate.matrix.methods.common.MatrixQuery;
import net.fhirfactory.pegacorn.communicate.matrix.model.interfaces.MatrixAdminProxyInterface;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MatrixApplicationServiceMethods {
    private static final Logger LOG = LoggerFactory.getLogger(MatrixApplicationServiceMethods.class);

    private ObjectMapper jsonMapper;

    @Inject
    private MatrixAccessToken matrixAccessToken;

    @Inject
    private MatrixAdminProxyInterface matrixAdminAPI;

    //
    // Constructor(s)
    //

    public MatrixApplicationServiceMethods() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        jsonMapper.registerModule(module);
        jsonMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
    }

    //
    // Business Methods
    //

    //
    // Registration

    public MAPIResponse registerApplicationService() {
        getLogger().debug(".registerApplicationService(): Entry");

        //
        // Create the Query
        MatrixQuery query = new MatrixQuery();
        // The header
        query.setHttpPath("_matrix/client/v3/register");
        query.setHttpMethod(HttpMethod.POST.name());
        // The body
        JSONObject body = new JSONObject();
        body.put("type", "m.login.application_service");
        body.put("username", "application-service-admin");
        query.setBody(body.toString());

        //
        // Execute the request
        MAPIResponse mapiResponse = matrixAdminAPI.executeApplicationServicesAction(query);

        //
        // Process the response (update the SessionAccessToken)
        if (mapiResponse.getResponseCode() == 200) {
            JSONObject responseObject = new JSONObject(mapiResponse.getResponseContent());
            String sessionToken = responseObject.getString("access_token");
            String userId = responseObject.getString("user_id");
            matrixAccessToken.setUserId(userId);
            matrixAccessToken.setSessionAccessToken(sessionToken);
        }
        getLogger().debug(".registerApplicationService(): Exit, response->{}", mapiResponse);
        return (mapiResponse);
    }

    //
    // Login

    public MAPIResponse loginApplicationService() {
        getLogger().debug(".loginApplicationService(): Entry");

        //
        // Create the Query
        MatrixQuery query = new MatrixQuery();
        // The header
        query.setHttpPath("_matrix/client/v3/login");
        query.setHttpMethod(HttpMethod.POST.name());
        // The body
        JSONObject body = new JSONObject();
        body.put("type", "m.login.application_service");
        JSONObject identifier = new JSONObject();
        identifier.put("type", "m.id.user");
        identifier.put("user", "application-service-admin");
        body.put("identifier", identifier);

        query.setBody(body.toString());

        //
        // Execute the request
        MAPIResponse mapiResponse = matrixAdminAPI.executeApplicationServicesAction(query);

        //
        // Process the response (update the SessionAccessToken)
        if (mapiResponse.getResponseCode() == 200) {
            JSONObject responseObject = new JSONObject(mapiResponse.getResponseContent());
            String sessionToken = responseObject.getString("access_token");
            String userId = responseObject.getString("user_id");
            String homeServer = responseObject.getString("home_server");
            matrixAccessToken.setUserId(userId);
            matrixAccessToken.setSessionAccessToken(sessionToken);
            matrixAccessToken.setHomeServer(homeServer);
        }
        getLogger().debug(".loginApplicationService(): Exit, response->{}", mapiResponse);
        return (mapiResponse);
    }

    //
    // Login

    public MAPIResponse setApplicationServiceDisplayName(String displayName) {
        getLogger().debug(".setApplicationServiceDisplayName(): Entry");

        //
        // Create the Query
        MatrixQuery query = new MatrixQuery();
        // The header
        query.setHttpPath("/_matrix/client/v3/profile/" + matrixAccessToken.getUserId() + "/displayname");
        query.setHttpMethod(HttpMethod.PUT.name());
        // The body
        JSONObject body = new JSONObject();
        body.put("displayname", displayName);

        query.setBody(body.toString());

        //
        // Execute the request
        MAPIResponse mapiResponse = matrixAdminAPI.executeApplicationServicesAction(query);

        getLogger().debug(".setApplicationServiceDisplayName(): Exit, response->{}", mapiResponse);
        return (mapiResponse);
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected ObjectMapper getJSONMapper() {
        return (jsonMapper);
    }
}
