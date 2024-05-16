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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.pegacorn.communicate.synapse.credentials.SynapseAdminAccessToken;
import net.fhirfactory.pegacorn.communicate.synapse.model.datatypes.SynapseQuery;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SynapseQueryProcessingBean {
    private static final Logger LOG = LoggerFactory.getLogger(SynapseQueryProcessingBean.class);
    private static String MATRIX_API_SUBPATH = "/_matrix/client/r0";
    private static String SYNAPSE_API_V1_SUBPATH = "_synapse/admin/v1/";
    private ObjectMapper jsonMapper;
    @Inject
    private SynapseAdminAccessToken synapseAccessToken;

    //
    // Constructor(s)
    //

    public SynapseQueryProcessingBean() {
        this.jsonMapper = new ObjectMapper();
    }

    //
    // Business Method
    //

    public String createRequest(SynapseQuery synapseQuery, Exchange camelExchange) {
        getLogger().debug(".createRequest(): Entry, synapseQuery->{}", synapseQuery);

        String body = synapseQuery.getBody();

        //
        // Now set the Exchange (HTTP) Parameters
        String queryPath = null;
        if (synapseQuery.getHttpPath().contains("?")) {
            queryPath = synapseQuery.getHttpPath() + "&throwExceptionOnFailure=false";
        } else {
            queryPath = synapseQuery.getHttpPath() + "?throwExceptionOnFailure=false";
        }
        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, synapseQuery.getHttpMethod());
        camelExchange.getIn().setHeader(Exchange.HTTP_PATH, queryPath);
        camelExchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        camelExchange.getIn().setHeader("Authorization", "Bearer " + getSynapseAccessToken().getSessionAccessToken());

        getLogger().debug(".createRequest(): Exit, body->{}", body);
        return (body);
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
