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
package net.fhirfactory.pegacorn.communicate.synapse.issi;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.dricats.core.interfaces.capabilities.CapabilityFulfillmentInterface;
import net.fhirfactory.dricats.core.interfaces.topology.WorkshopInterface;
import net.fhirfactory.dricats.core.messaging.wup.MessageBasedWUPEndpointContainer;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationRequest;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationResponse;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPClientAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPClientTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.external.ConnectedExternalSystemTopologyNode;
import net.fhirfactory.dricats.workshops.InterSubSystemIntegrationWorkshop;
import net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased.InteractEgressAPIClientGatewayWUP;
import net.fhirfactory.pegacorn.communicate.synapse.credentials.SynapseAdminAccessToken;
import net.fhirfactory.pegacorn.communicate.synapse.issi.beans.SynapseAccessTokenExtractorBean;
import net.fhirfactory.pegacorn.communicate.synapse.issi.beans.SynapseLoginMethodCreatorBean;
import net.fhirfactory.pegacorn.communicate.synapse.issi.beans.SynapseQueryProcessingBean;
import net.fhirfactory.pegacorn.communicate.synapse.issi.beans.SynapseResponseProcessingBean;
import net.fhirfactory.pegacorn.communicate.synapse.methods.common.SynapseAPIResponse;
import net.fhirfactory.pegacorn.communicate.synapse.model.SynapseAdminProxyInterface;
import net.fhirfactory.pegacorn.communicate.synapse.model.datatypes.SynapseQuery;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

;

public abstract class SynapseAPIProxyWUP extends InteractEgressAPIClientGatewayWUP implements CapabilityFulfillmentInterface, SynapseAdminProxyInterface {

    private static String CAMEL_COMPONENT_TYPE = "netty-http";
    private static String SYNAPSE_LOGIN_INGRES_ENDPOINT = "direct:synapse-login";
    private static String SYNAPSE_ROOM_ACTION_INGRES_ENDPOINT = "direct:synapse-room-action";
    private static String SYNAPSE_USER_ACTION_INGRES_ENDPOINT = "direct:synapse-user-action";
    private String hostName;
    private int portValue;
    private String httpScheme;
    private String webServicePath;
    private String uriSpecification;
    private CloseableHttpClient synapseHTTPClient;
    private ObjectMapper jsonMapper;
    @Inject
    private InterSubSystemIntegrationWorkshop workshop;

    @Inject
    private SynapseAdminAccessToken synapseAccessToken;

    @Inject
    private SynapseAccessTokenExtractorBean synapseAccessTokenExtractorBean;

    @Inject
    private SynapseLoginMethodCreatorBean synapseLoginMethodCreatorBean;

    @Inject
    private SynapseQueryProcessingBean synapseMethodCreator;

    @Inject
    private SynapseResponseProcessingBean responseProcessor;

    @Produce
    private ProducerTemplate camelRouteInjector;


    //
    // Constructor(s)
    //

    public SynapseAPIProxyWUP() {
        super();
        this.hostName = null;
        this.portValue = 0;
        this.httpScheme = null;
        this.webServicePath = null;
        this.uriSpecification = null;
        this.synapseHTTPClient = null;
        jsonMapper = new ObjectMapper();
    }

    //
    // Implemented Methods
    //

    @Override
    protected List<DataParcelManifest> specifySubscriptionTopics() {
        getLogger().debug(".specifySubscriptionTopics(): Entry");

        List<DataParcelManifest> subscribedList = new ArrayList<>();

        getLogger().debug(".specifySubscriptionTopics(): Exit");
        return (subscribedList);
    }

    @Override
    protected WorkshopInterface specifyWorkshop() {
        return (workshop);
    }


    @Override
    protected MessageBasedWUPEndpointContainer specifyEgressEndpoint() {
        MessageBasedWUPEndpointContainer endpoint = new MessageBasedWUPEndpointContainer();
        HTTPClientTopologyEndpoint clientTopologyEndpoint = (HTTPClientTopologyEndpoint) getTopologyEndpoint(specifyEgressTopologyEndpointName());
        ConnectedExternalSystemTopologyNode targetSystem = clientTopologyEndpoint.getTargetSystem();
        HTTPClientAdapter httpClient = (HTTPClientAdapter) targetSystem.getTargetPorts().get(0);
        this.portValue = httpClient.getPortNumber();
        this.hostName = httpClient.getHostName();
        String httpType = null;
        if (httpClient.isEncrypted()) {
            httpType = "https";
        } else {
            httpType = "http";
        }
        this.httpScheme = httpType;
        if (StringUtils.isEmpty(httpClient.getContextPath()) || httpClient.getContextPath().contains("null")) {
            this.webServicePath = "";
            this.uriSpecification = this.httpScheme + "://" + getHostName() + ":" + getPortValue();
        } else {
            this.webServicePath = httpClient.getContextPath();
            this.uriSpecification = this.httpScheme + "://" + getHostName() + ":" + getPortValue() + getWebServicePath();
        }
        endpoint.setEndpointSpecification(CAMEL_COMPONENT_TYPE + ":" + this.uriSpecification);
        endpoint.setEndpointTopologyNode(clientTopologyEndpoint);
        endpoint.setFrameworkEnabled(false);
        return endpoint;
    }

    @Override
    public void configure() throws Exception {

        getExceptionHandlerForGeneralSynapseTransactions();

        from(SYNAPSE_LOGIN_INGRES_ENDPOINT)
                .routeId("SynapseLoginRoute")
                .bean(synapseLoginMethodCreatorBean, "createRequest")
                .log(LoggingLevel.DEBUG, "Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Headers -> ${headers}, body -> ${body}")
                .bean(synapseAccessTokenExtractorBean, "extractAndSetToken");

        from(getSynapseRoomActionIngresEndpoint())
                .routeId(getSynapseRoomActionIngresEndpoint())
                .bean(synapseMethodCreator, "createRequest")
                .log(LoggingLevel.DEBUG, "Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Headers -> ${headers}, body -> ${body}")
                .to("direct:SynapseResponseProcessor");

        from("direct:SynapseResponseProcessor")
                .bean(responseProcessor, "processResponse");
    }

    protected OnExceptionDefinition getExceptionHandlerForGeneralSynapseTransactions() {
        OnExceptionDefinition exceptionDef = onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Matrix API Proxy Exception: Headers -> ${headers}, body -> ${body}")
                .to("direct:SynapseResponseProcessor");
        return (exceptionDef);
    }

    @Override
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request) {
        getLogger().debug(".executeTask(): Entry, request->{}", request);
        String capability = request.getRequiredCapabilityName();

        String requestString = null;

        CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
        response.setInstantCompleted(Instant.now());
        response.setAssociatedRequestID(request.getRequestID());
/*        switch(capability){
            case "Synapse-Room-Query": {
                SynapseAPIResponse result = executeRoomAction(HttpMethod.GET, requestString, request.getRequestStringContent());
                response.setSuccessful(result.isSuccessful());
                if(result.isSuccessful()) {
                    response.setResponseContent(result.getResponseContent());
                } else {
                    response.setResponseContent(result.getErrorContent());
                }
                break;
            }
            case "Synapse-User-Query": {
                SynapseAPIResponse result = executeUserAction(HttpMethod.GET, requestString, request.getRequestStringContent());
                response.setSuccessful(result.isSuccessful());
                if(result.isSuccessful()) {
                    response.setResponseContent(result.getResponseContent());
                } else {
                    response.setResponseContent(result.getErrorContent());
                }
                break;
            }
            default:{
                response.setSuccessful(false);
            }
        }

 */
        getLogger().debug(".executeTask(): Exit, response->{}", response);
        return (response);
    }

    @Override
    protected void registerCapabilities() {
        getProcessingPlant().registerCapabilityFulfillmentService("Synapse-Room-Query", this);
        getProcessingPlant().registerCapabilityFulfillmentService("Synapse-User-Query", this);
    }

    @Override
    public SynapseAPIResponse executeRoomAction(SynapseQuery synapseQuery) {
        getLogger().debug(".executeRoomQuery(): Entry, synapseQuery->{}", synapseQuery);

        SynapseAPIResponse queryResponse = new SynapseAPIResponse();

        getLogger().debug(".executeRoomQuery(): Exit, queryResponse->{}", queryResponse);
        return (queryResponse);
    }

    @Override
    public SynapseAPIResponse executeUserAction(SynapseQuery synapseQuery) {
        getLogger().debug(".executeUserAction(): Entry, synapseQuery->{}", synapseQuery);

        SynapseAPIResponse queryResponse = new SynapseAPIResponse();

        getLogger().debug(".executeUserAction(): Exit, queryResponse->{}", queryResponse);
        return (queryResponse);
    }

    //
    // Login Sequence
    //

    @Override
    public String executeLogin() {
        getLogger().debug(".executeLogin(): Entry");
        String sessionAccessToken = (String) getCamelRouteInjector().sendBody(SYNAPSE_LOGIN_INGRES_ENDPOINT, ExchangePattern.InOut, "Nothing");
        getLogger().debug(".executeLogin(): Entry, sessionAccessToken->{}", sessionAccessToken);
        return (sessionAccessToken);
    }

//
    // Getters (and Setters)
    //

    protected ProducerTemplate getCamelRouteInjector() {
        return (this.camelRouteInjector);
    }

    protected SynapseAdminAccessToken getSynapseAccessToken() {
        return (this.synapseAccessToken);
    }

    protected int getPortValue() {
        return (this.portValue);
    }

    protected String getHttpScheme() {
        return (this.httpScheme);
    }

    protected String getHostName() {
        return (this.hostName);
    }

    protected String getWebServicePath() {
        return (this.webServicePath);
    }

    public HttpClient getSynapseHTTPClient() {
        return (synapseHTTPClient);
    }

    protected String getUriSpecification() {
        return uriSpecification;
    }

    @Override
    public WorkshopInterface getWorkshop() {
        return workshop;
    }

    @Override
    public String getSynapseRoomActionIngresEndpoint() {
        return (SYNAPSE_ROOM_ACTION_INGRES_ENDPOINT);
    }

    @Override
    public String getSynapseUserActionIngresEndpoint() {
        return (SYNAPSE_USER_ACTION_INGRES_ENDPOINT);
    }
}
