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

import net.fhirfactory.pegacorn.communicate.synapse.credentials.SynapseAdminAccessToken;
import net.fhirfactory.pegacorn.communicate.synapse.methods.common.SynapseAPIResponse;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SynapseResponseProcessingBean {
    private static final Logger LOG = LoggerFactory.getLogger(SynapseResponseProcessingBean.class);

    @Inject
    private SynapseAdminAccessToken synapseAccessToken;

    //
    // Constructor(s)
    //

    public SynapseResponseProcessingBean() {

    }

    //
    // Business Method(s)
    //

    public SynapseAPIResponse processResponse(String httpResponseBody, Exchange camelExchange) {
        getLogger().debug(".processResponse(): Entry, httpResponseBody->{}", httpResponseBody);

        SynapseAPIResponse response = new SynapseAPIResponse();

        response.setResponseContent(httpResponseBody);

        int responseCode = (int) camelExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
        getLogger().debug(".processResponse(): Entry, responseCode->{}", responseCode);
        response.setResponseCode(responseCode);

        getLogger().debug(".processResponse(): Exit, response->{}", response);
        return (response);
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

}
