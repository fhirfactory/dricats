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
package net.fhirfactory.pegacorn.communicate.matrix.issi.forwarder;

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
import net.fhirfactory.dricats.util.PegacornEnvironmentProperties;
import net.fhirfactory.dricats.workshops.InterSubSystemIntegrationWorkshop;
import net.fhirfactory.dricats.wups.archetypes.managed.messageprocessingbased.InteractEgressMessagingGatewayWUP;
import net.fhirfactory.pegacorn.communicate.matrix.credentials.MatrixAccessToken;
import net.fhirfactory.pegacorn.communicate.matrix.issi.forwarder.beans.MatrixEventPreparationBean;
import net.fhirfactory.pegacorn.communicate.matrix.issi.query.beans.MatrixResponseProcessingBean;
import net.fhirfactory.pegacorn.communicate.matrix.model.interfaces.MatrixEventForwarderInterface;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.MatrixEventBase;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class MatrixClientServerEventForwarderWUP extends InteractEgressMessagingGatewayWUP implements CapabilityFulfillmentInterface, MatrixEventForwarderInterface {

    private static String MATRIX_ROOM_EVENT_FORWARDER_ENDPOINT = "direct:matrix-event-forwarder";
    private String CAMEL_COMPONENT_TYPE = "netty-http";
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
    private MatrixAccessToken accessToken;

    @Inject
    private PegacornEnvironmentProperties environmentProperties;

    @Inject
    private MatrixEventPreparationBean eventPreparationTool;

    @Inject
    private MatrixResponseProcessingBean responsePostProcessor;

    @Inject
    private ProducerTemplate camelRouteInjector;

    //
    // Constructor(s)
    //

    public MatrixClientServerEventForwarderWUP() {
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
    // Post Construct Activities
    //

    protected static String getMatrixRoomEventForwarderEndpoint() {
        return MATRIX_ROOM_EVENT_FORWARDER_ENDPOINT;
    }

    //
    // Superclass Method Implementation
    //

    @Override
    public void executePostInitialisationActivities() {
        // Get the AccessToken and assign it
        String synapseAdminUserName = environmentProperties.getMandatoryProperty(MatrixAccessToken.SYNAPSE_ADMIN_USER_NAME_PROPERTY);
        String matrixBridgeAccessToken = environmentProperties.getMandatoryProperty(MatrixAccessToken.MATRIX_BRIDGE_TOKEN_PROPERTY);
        String synapseAccessToken = environmentProperties.getMandatoryProperty(MatrixAccessToken.SYNAPSE_ACCESS_TOKEN_PROPERTY);

        if (StringUtils.isEmpty(synapseAdminUserName) || StringUtils.isEmpty(matrixBridgeAccessToken) || StringUtils.isEmpty(synapseAccessToken)) {
            throw (new RuntimeException("SynapseAdminUserName or SynapseAdminUserPassword is blank"));
        }

        getAccessToken().setRemoteAccessToken(synapseAccessToken);
        getAccessToken().setUserName(synapseAdminUserName);
        getAccessToken().setLocalAccessToken(matrixBridgeAccessToken);
    }

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

    //
    // Core API Activity (Routes)
    //

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

        from(getMatrixRoomEventForwarderEndpoint())
                .routeId(getMatrixRoomEventForwarderEndpoint())
                .bean(eventPreparationTool, "createRequest")
                .log(LoggingLevel.DEBUG, "Request: Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Response: Headers -> ${headers}, body -> ${body}")
                .bean(responsePostProcessor, "processResponse");

    }

    //
    // Capability Implementation
    //

    @Override
    public MAPIResponse forwardEventIntoMatrix(MatrixEventBase matrixEvent) {
        getLogger().debug(".forwardEventIntoMatrix(): Entry, matrixEvent->{}", matrixEvent);
        MAPIResponse response = (MAPIResponse) camelRouteInjector.sendBody(getMatrixRoomEventForwarderEndpoint(), ExchangePattern.InOut, matrixEvent);
        getLogger().debug(".forwardEventIntoMatrix(): Exit, response->{}", response);
        return (response);
    }

    @Override
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request) {
        getLogger().debug(".executeTask(): Entry, request->{}", request);

        getLogger().error(".executeTask(): Requires Implementation");

        getLogger().debug(".executeTask(): Exit");
        return (null);
    }

    //
    // Getters (and Setters)
    //

    @Override
    protected void registerCapabilities() {
        getProcessingPlant().registerCapabilityFulfillmentService("Matrix-RoomServer-MessageInjection", this);
    }

    protected String getHostName() {
        return (this.hostName);
    }

    protected int getPortValue() {
        return (this.portValue);
    }

    protected String getHttpScheme() {
        return (this.httpScheme);
    }

    protected String getWebServicePath() {
        return (this.webServicePath);
    }

    protected String getUriSpecification() {
        return (this.uriSpecification);
    }

    protected MatrixAccessToken getAccessToken() {
        return (this.accessToken);
    }

    protected MatrixEventPreparationBean getQueryPreProcessor() {
        return eventPreparationTool;
    }

    protected MatrixResponseProcessingBean getResponsePostProcessor() {
        return responsePostProcessor;
    }
}
