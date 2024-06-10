/*
 * Copyright (c) 2020 Mark A. Hunter
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

package net.fhirfactory.pegacorn.communicate.matrix.issi.receiver;

import net.fhirfactory.dricats.core.interfaces.topology.WorkshopInterface;
import net.fhirfactory.dricats.core.messaging.wup.MessageBasedWUPEndpointContainer;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPServerAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPServerTopologyEndpoint;
import net.fhirfactory.dricats.core.participants.wup.helper.IngresActivityBeginRegistration;
import net.fhirfactory.dricats.util.PegacornProperties;
import net.fhirfactory.dricats.workshops.InteractWorkshop;
import net.fhirfactory.dricats.wups.archetypes.managed.messageprocessingbased.InteractIngresMessagingGatewayWUP;
import net.fhirfactory.pegacorn.communicate.matrix.issi.receiver.beans.IncomingMatrixEventSet2UoW;
import net.fhirfactory.pegacorn.communicate.matrix.issi.receiver.beans.IncomingMatrixEventSetValidator;
import net.fhirfactory.pegacorn.communicate.matrix.issi.receiver.beans.IncomingMatrixMessageSplitter;
import net.fhirfactory.pegacorn.communicate.matrix.model.exceptions.MatrixEventNotFoundException;
import net.fhirfactory.pegacorn.communicate.matrix.model.exceptions.MatrixUpdateException;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public abstract class MatrixApplicationServicesEventsReceiverWUP extends InteractIngresMessagingGatewayWUP {

    private static String INGRES_GATEWAY_COMPONENT = "netty-http";

    @Inject
    private InteractWorkshop workshop;

    @Inject
    private PegacornProperties pegacornProperties;

    public MatrixApplicationServicesEventsReceiverWUP() {
        super();
    }

    @Override
    public void configure() throws Exception {
        getLogger().debug(".configure(): Entry");

        getLogger().info("{}:: ingresFeed() --> {}", getClass().getSimpleName(), ingresFeed());
        getLogger().info("{}:: egressFeed() --> {}", getClass().getSimpleName(), egressFeed());

        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("/var/lib/pegacorn-keystores/keystore.jks");
        ksp.setPassword(getKeyStoreInstancePassword());
        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyStore(ksp);
        kmp.setKeyPassword(getKeyStoreInstancePassword());
        SSLContextParameters scp = new SSLContextParameters();
        scp.setKeyManagers(kmp);

        ComponentsBuilderFactory.nettyHttp()
                .ssl(true)
                .sslContextParameters(scp)
                .register(getContext(), "netty-http");

        //
        // Exceptions
        //
        routeMatrixEventNotFoundException();
        routeGeneralException();

        //
        // Main Route
        //
        fromInteractIngresService(ingresFeed())
                .routeId(getNameSet().getRouteCoreWUP())
                .transform(simple("${bodyAs(String)}"))
                .bean(IncomingMatrixEventSetValidator.class, "validateEventSetMessage")
                .bean(IncomingMatrixEventSet2UoW.class, "encapsulateMatrixMessage(*, Exchange)")
                .bean(IngresActivityBeginRegistration.class, "registerActivityStart(*,  Exchange)")
                .to(ExchangePattern.InOnly, getContinueProcessingRoute())
                .transform().simple("{}")
                .end();

        fromWithStandardExceptionHandling(getContinueProcessingRoute())
                .bean(IncomingMatrixMessageSplitter.class, "splitMessageIntoEvents")
                .to(egressFeed());
    }

    private String getContinueProcessingRoute() {
        String routeName = "direct:" + getClass().getSimpleName() + "-ContinueProcessingRoute";
        return (routeName);
    }

    @Override
    protected WorkshopInterface specifyWorkshop() {
        return (workshop);
    }


    @Override
    protected MessageBasedWUPEndpointContainer specifyIngresEndpoint() {
        getLogger().debug(".specifyIngresTopologyEndpoint(): Entry");
        MessageBasedWUPEndpointContainer ingresEndpoint = new MessageBasedWUPEndpointContainer();
        ingresEndpoint.setFrameworkEnabled(false);
        HTTPServerTopologyEndpoint myMatrixServerAPIServicesGW = (HTTPServerTopologyEndpoint) getTopologyEndpoint(specifyIngresTopologyEndpointName());
        if (myMatrixServerAPIServicesGW == null) {
            getLogger().error(".specifyIngresTopologyEndpoint(): Unable to derive endpoint for Matrix Application Services API  Server");
            return (ingresEndpoint);
        }
        // Assign the Associated TopologyNode to the Ingres Endpoint
        ingresEndpoint.setEndpointTopologyNode(myMatrixServerAPIServicesGW);
        getLogger().trace(".specifyIngresTopologyEndpoint(): Resolved myMatrixServerAPIServicesGW->{}", myMatrixServerAPIServicesGW);
        // Building the Endpoint Specification (String)

        HTTPServerAdapter httpServerAdapter = myMatrixServerAPIServicesGW.getHTTPServerAdapter();
        int portValue = httpServerAdapter.getPortNumber();
        String interfaceDNSName = httpServerAdapter.getHostName();
        String contextPath = httpServerAdapter.getContextPath();
        if (StringUtils.isEmpty(contextPath) || contextPath.equals("null")) {
            contextPath = "";
        }
        String httpType = null;
        if (httpServerAdapter.isEncrypted()) {

            httpType = "https";
        } else {
            httpType = "http";
        }
        String webServicePath = null;
        String uriSpecification = null;
        if (StringUtils.isEmpty(httpServerAdapter.getContextPath()) || httpServerAdapter.getContextPath().contains("null")) {
            webServicePath = "";
            uriSpecification = httpType + "://" + interfaceDNSName + ":" + Integer.toString(portValue);
        } else {
            webServicePath = httpServerAdapter.getContextPath();
            uriSpecification = httpType + "://" + interfaceDNSName + ":" + Integer.toString(portValue) + webServicePath;
        }
        ingresEndpoint.setEndpointSpecification(INGRES_GATEWAY_COMPONENT + ":" + uriSpecification);
        getLogger().info(".specifyIngresTopologyEndpoint(): Exit, ingresEndpoint->{}", ingresEndpoint);
        return (ingresEndpoint);
    }

    private String getKeyStoreInstancePassword() {
        String password = pegacornProperties.getProperty("KEY_PASSWORD", "unknown");
        if (password.contentEquals("unknown")) {
            getLogger().error(".getKeyStoreInstancePassword(): Unable to resolve Parameter KEY_PASSWORD");
            return (null);
        }
        getLogger().debug(".getKeyStoreInstancePassword(): Exit,  KEY_PASSWORD->{}", password);
        return (password);
    }

    private OnExceptionDefinition routeMatrixEventNotFoundException() {
        OnExceptionDefinition exceptionDef = onException(MatrixEventNotFoundException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "MatrixEventNotFoundException...")
                // use HTTP status 404 when data was not found
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(simple("${exception.message}\n"));

        return (exceptionDef);
    }

    private OnExceptionDefinition routeResourceUpdateException() {
        OnExceptionDefinition exceptionDef = onException(MatrixUpdateException.class)
                .handled(true)
                .log(LoggingLevel.INFO, "ResourceUpdateException...")
                // use HTTP status 404 when data was not found
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setBody(simple("${exception.message}\n"));

        return (exceptionDef);
    }

    private OnExceptionDefinition routeGeneralException() {
        OnExceptionDefinition exceptionDef = onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "${exception.message}\n")
                // use HTTP status 500 when we had a server side error
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setBody(simple("${exception.message}\n"));
        return (exceptionDef);
    }

    @Override
    protected String specifyIngresEndpointVersion() {
        return "1.0.0s";
    }
}
