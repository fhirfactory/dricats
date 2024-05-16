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
package net.fhirfactory.pegacorn.communicate.matrix.issi.query;

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
import net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased.InteractEgressAPIClientGatewayWUP;
import net.fhirfactory.pegacorn.communicate.matrix.credentials.MatrixAccessToken;
import net.fhirfactory.pegacorn.communicate.matrix.issi.query.beans.MatrixApplicationServicesQueryProcessingBean;
import net.fhirfactory.pegacorn.communicate.matrix.issi.query.beans.MatrixQueryProcessingBean;
import net.fhirfactory.pegacorn.communicate.matrix.issi.query.beans.MatrixResponseProcessingBean;
import net.fhirfactory.pegacorn.communicate.matrix.methods.common.MatrixQuery;
import net.fhirfactory.pegacorn.communicate.matrix.model.interfaces.MatrixAdminProxyInterface;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public abstract class MatrixClientServerAPIProxyWUP extends InteractEgressAPIClientGatewayWUP implements CapabilityFulfillmentInterface, MatrixAdminProxyInterface {

    private static String CAMEL_COMPONENT_TYPE = "netty-http";
    private static String MATRIX_APPLICATION_SERVICES_INGRES_ENDPOINT = "direct:matrix-application-services-action";
    private static String MATRIX_ROOM_ACTION_INGRES_ENDPOINT = "direct:matrix-room-action";
    private static String MATRIX_SPACE_ACTION_INGRES_ENDPOINT = "direct:matrix-space-action";
    private static String MATRIX_USER_ACTION_INGRES_ENDPOINT = "direct:synapse-user-action";
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
    private MatrixQueryProcessingBean queryPreProcessor;

    @Inject
    private MatrixResponseProcessingBean responsePostProcessor;

    @Inject
    private MatrixApplicationServicesQueryProcessingBean applicationServicesQueryProcessor;

    @Inject
    private ProducerTemplate camelRouteInjector;


    //
    // Constructor(s)
    //

    public MatrixClientServerAPIProxyWUP() {
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
    // Superclass Method Implementation
    //

    public static String getMatrixRoomActionIngresEndpoint() {
        return MATRIX_ROOM_ACTION_INGRES_ENDPOINT;
    }

    public static String getMatrixSpaceActionIngresEndpoint() {
        return MATRIX_SPACE_ACTION_INGRES_ENDPOINT;
    }

    public static String getMatrixUserActionIngresEndpoint() {
        return MATRIX_USER_ACTION_INGRES_ENDPOINT;
    }

    //
    // Core API Activity (Routes)
    //

    public static String getMatrixApplicationServicesIngresEndpoint() {
        return MATRIX_APPLICATION_SERVICES_INGRES_ENDPOINT;
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

        getExceptionHandler();

        from(getMatrixApplicationServicesIngresEndpoint())
                .routeId(getMatrixApplicationServicesIngresEndpoint())
                .bean(applicationServicesQueryProcessor, "createRequest")
                .log(LoggingLevel.DEBUG, "Request: Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Response: Headers -> ${headers}, body -> ${body}")
                .to("direct:httpTransactionResponse");

        from(getMatrixRoomActionIngresEndpoint())
                .routeId(getMatrixRoomActionIngresEndpoint())
                .bean(queryPreProcessor, "createRequest")
                .log(LoggingLevel.DEBUG, "Request: Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Response: Headers -> ${headers}, body -> ${body}")
                .to("direct:httpTransactionResponse");

        from(getMatrixSpaceActionIngresEndpoint())
                .routeId(getMatrixSpaceActionIngresEndpoint())
                .bean(queryPreProcessor, "createRequest")
                .log(LoggingLevel.DEBUG, "Request: Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Response: Headers -> ${headers}, body -> ${body}")
                .to("direct:httpTransactionResponse");

        from(getMatrixUserActionIngresEndpoint())
                .routeId(getMatrixUserActionIngresEndpoint())
                .bean(queryPreProcessor, "createRequest")
                .log(LoggingLevel.DEBUG, "Request: Headers -> ${headers}, body -> ${body}")
                .to("netty-http:" + getUriSpecification())
                .log(LoggingLevel.DEBUG, "Response: Headers -> ${headers}, body -> ${body}")
                .to("direct:httpTransactionResponse");

        from("direct:httpTransactionResponse")
                .bean(responsePostProcessor, "processResponse");
    }

    //
    // Exception Handling
    //

    @Override
    public MAPIResponse executeApplicationServicesAction(MatrixQuery matrixQuery) {
        getLogger().debug(".executeApplicationServicesAction(): Entry, matrixQuery->{}", matrixQuery);
        MAPIResponse response = (MAPIResponse) camelRouteInjector.sendBody(getMatrixApplicationServicesIngresEndpoint(), ExchangePattern.InOut, matrixQuery);
        getLogger().debug(".executeApplicationServicesAction(): Exit, response->{}", response);
        return (response);
    }

    //
    // Capability Implementation
    //

    @Override
    public MAPIResponse executeRoomAction(MatrixQuery matrixQuery) {
        getLogger().debug(".executeRoomAction(): Entry, matrixQuery->{}", matrixQuery);
        MAPIResponse response = (MAPIResponse) camelRouteInjector.sendBody(getMatrixRoomActionIngresEndpoint(), ExchangePattern.InOut, matrixQuery);
        getLogger().debug(".executeRoomAction(): Exit, response->{}", response);
        return (response);
    }

    @Override
    public MAPIResponse executeUserAction(MatrixQuery matrixQuery) {
        getLogger().debug(".executeUserAction(): Entry, matrixQuery->{}", matrixQuery);
        MAPIResponse response = (MAPIResponse) camelRouteInjector.sendBody(getMatrixUserActionIngresEndpoint(), ExchangePattern.InOut, matrixQuery);
        getLogger().debug(".executeUserAction(): Exit, response->{}", response);
        return (response);
    }

    //
    // Post Construct Activities
    //

    @Override
    public MAPIResponse executeSpaceAction(MatrixQuery matrixQuery) {
        getLogger().debug(".executeUserAction(): Entry, matrixQuery->{}", matrixQuery);
        MAPIResponse response = (MAPIResponse) camelRouteInjector.sendBody(getMatrixSpaceActionIngresEndpoint(), ExchangePattern.InOut, matrixQuery);
        getLogger().debug(".executeUserAction(): Exit, response->{}", response);
        return (response);
    }

    //
    // Getters (and Setters)
    //

    protected OnExceptionDefinition getExceptionHandler() {
        OnExceptionDefinition exceptionDef = onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Matrix API Proxy Exception: Headers -> ${headers}, body -> ${body}")
                .to("direct:httpTransactionResponse");
        return (exceptionDef);
    }

    @Override
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request) {
        return null;
    }

    @Override
    protected void registerCapabilities() {
        getProcessingPlant().registerCapabilityFulfillmentService("Matrix-RoomServer-Query", this);
    }

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

    protected MatrixQueryProcessingBean getQueryPreProcessor() {
        return queryPreProcessor;
    }

    protected MatrixResponseProcessingBean getResponsePostProcessor() {
        return responsePostProcessor;
    }
}
