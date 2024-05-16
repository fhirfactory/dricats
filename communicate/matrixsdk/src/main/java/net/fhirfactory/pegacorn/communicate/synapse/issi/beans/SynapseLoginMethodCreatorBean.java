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
package net.fhirfactory.pegacorn.communicate.synapse.issi.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.login.MSimpleLogin;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.login.datatypes.MUserIdentifierType;
import net.fhirfactory.pegacorn.communicate.synapse.credentials.SynapseAdminAccessToken;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SynapseLoginMethodCreatorBean {
    private static final Logger LOG = LoggerFactory.getLogger(SynapseLoginMethodCreatorBean.class);
    private static String MATRIX_API_SUBPATH = "/_matrix/client/r0";
    private ObjectMapper jsonMapper;
    @Inject
    private SynapseAdminAccessToken synapseAccessToken;

    //
    // Constructor(s)
    //

    public SynapseLoginMethodCreatorBean() {
        this.jsonMapper = new ObjectMapper();
    }

    //
    // Business Method
    //

    public String createRequest(String nothingString, Exchange camelExchange) {
        getLogger().debug(".createRequest(): Entry, nothingString->{}", nothingString);

        //
        // Create the Login Body
        MSimpleLogin loginRequest = new MSimpleLogin();
        loginRequest.setType("m.login.password");
        MUserIdentifierType userIdentifier = new MUserIdentifierType();
        userIdentifier.setType("m.id.user");
        userIdentifier.setUser(getSynapseAccessToken().getUserName());
        loginRequest.setIdentifier(userIdentifier);
        loginRequest.setPassword(getSynapseAccessToken().getUserPassword());

        String loginRequestAsString = null;
        try {
            loginRequestAsString = getJSONMapper().writeValueAsString(loginRequest);
        } catch (JsonProcessingException e) {
            getLogger().error(".initialiseSynapseHTTPClient(): Exception->{}", ExceptionUtils.getStackTrace(e));
        }

        //
        // Now set the Exchange (HTTP) Parameters
        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpMethod.POST);
        camelExchange.getIn().setHeader(Exchange.HTTP_PATH, MATRIX_API_SUBPATH + "/login");
        camelExchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");

        getLogger().debug(".createRequest(): Exit, loginRequestAsString->{}", loginRequestAsString);
        return (loginRequestAsString);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected SynapseAdminAccessToken getSynapseAccessToken() {
        return (synapseAccessToken);
    }

    protected ObjectMapper getJSONMapper() {
        return (jsonMapper);
    }
}
