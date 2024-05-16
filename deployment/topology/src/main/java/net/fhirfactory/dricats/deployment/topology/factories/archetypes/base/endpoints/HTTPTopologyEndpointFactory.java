/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.endpoints;

import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPClientAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPServerAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapterDefinition;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPClientTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.interact.StandardInteractClientTopologyEndpointPort;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.core.model.topology.nodes.external.ConnectedExternalSystemTopologyNode;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemPort;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemProperties;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.common.TopologyFactoryHelpersBase;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.util.PegacornProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HTTPTopologyEndpointFactory extends TopologyFactoryHelpersBase {
    private static final Logger LOG = LoggerFactory.getLogger(HTTPTopologyEndpointFactory.class);

    @Inject
    private PegacornProperties pegacornProperties;

    @Inject
    private PegacornCommonInterfaceNames interfaceNames;

    @Inject
    private TopologyIM topologyIM;

    //
    // Constructor(s)
    //

    //
    // Getters and Setters
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    protected PegacornCommonInterfaceNames getInterfaceNames() {
        return (interfaceNames);
    }

    protected TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    //
    // Business Methods
    //

    public IPCServerTopologyEndpoint newHTTPServerTopologyEndpoint(BaseSubsystemPropertyFile propertyFile, EndpointProviderInterface endpointProvider, String name, StandardServerPortSegment httpServerPort) {
        getLogger().debug(".newHTTPServerTopologyEndpoint(): Entry, endpointProvider->{}, endpointFunctionName->{}, httpServerPort->{}", endpointProvider, httpServerPort);
        if (httpServerPort == null) {
            getLogger().debug(".newHTTPServerTopologyEndpoint(): Exit, no port to add");
            return (null);
        }
        HTTPServerTopologyEndpoint httpServer = new HTTPServerTopologyEndpoint();
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        httpServer.setComponentRDN(nodeRDN);
        httpServer.setEndpointConfigurationName(httpServerPort.getName());
        httpServer.setActualHostIP(getActualHostIP());
        httpServer.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        httpServer.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER);
        httpServer.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        httpServer.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        httpServer.setServer(true);
        httpServer.setContainingNodeFDN(endpointProvider.getComponentFDN());
        httpServer.setConnectedSystemName(httpServerPort.getConnectedSystem().getSubsystemName());
        //
        // Create the ServerAdapter (the thing that actually holds the port number and ip address details)
        HTTPServerAdapter httpServerAdapter = new HTTPServerAdapter();
        //
        // Unfortunately, the two different HTTP endpoints don't inherent from the same superclass until way up in
        // the chain - so we need to explicitly separate the creation/assignment of parameters... :(
        if (httpServerPort instanceof ClusteredHTTPServerPortSegment) {
            //
            // This assumes that there is some type of load balancer in front of our exposed server ports
            ClusteredHTTPServerPortSegment clusteredHTTPServerPort = (ClusteredHTTPServerPortSegment) httpServerPort;
            //
            // This is the Port Number we are actually binding to
            if (clusteredHTTPServerPort.getServerPort() == null) {
                throw (new IllegalArgumentException("serverPort for " + clusteredHTTPServerPort.getName() + " is not defined in configuration file"));
            }
            httpServerAdapter.setPortNumber(clusteredHTTPServerPort.getServerPort());
            //
            // This is the Port Number exposed via the clustering service (i.e. a load-balancer)
            if (clusteredHTTPServerPort.getServicePort() == null) {
                throw (new IllegalArgumentException("servicePort for " + clusteredHTTPServerPort.getName() + " is not defined in the configuration file"));
            }
            httpServerAdapter.setServicePortValue(clusteredHTTPServerPort.getServicePort());
            //
            // Set the context path
            String serverPath = clusteredHTTPServerPort.getContextPath();
            httpServerAdapter.setContextPath(serverPath);
            //
            // This is the hostname that we actually bind our port to
            if (StringUtils.isEmpty(clusteredHTTPServerPort.getServerHostname())) {
                throw (new IllegalArgumentException("serverHost for " + clusteredHTTPServerPort.getName() + "is not defined in configuration file"));
            }
            httpServerAdapter.setHostName(httpServerPort.getServerHostname());
            //
            // This is the hostname/ip-address that we are exposing via the clustering service (i.e. load-balancer)
            if (StringUtils.isEmpty(propertyFile.getLoadBalancer().getIpAddress())) {
                if (StringUtils.isEmpty(clusteredHTTPServerPort.getServiceDNS())) {
                    throw (new IllegalArgumentException("serviceDNS and loadBalancer.ipAddress for " + clusteredHTTPServerPort.getName() + "are not defined in configuration file"));
                } else {
                    httpServerAdapter.setServiceDNSName(clusteredHTTPServerPort.getServiceDNS());
                }
            } else {
                httpServerAdapter.setServiceDNSName(propertyFile.getLoadBalancer().getIpAddress());
            }
            //
            // Should encryption (https) be used?
            httpServerAdapter.setEncrypted(clusteredHTTPServerPort.isEncrypted());
            //
            // Add the adapter to the TopologyEndpoint
            httpServer.setHTTPServerAdapter(httpServerAdapter);
            //
            // Now build the Endpoint Description
            if (clusteredHTTPServerPort.isEncrypted()) {
                if (StringUtils.isEmpty(httpServerAdapter.getContextPath())) {
                    httpServer.setEndpointDescription("Server-->HTTPS://" + httpServerAdapter.getServiceDNSName() + ":" + httpServerAdapter.getServicePortValue() + "/");
                } else {
                    httpServer.setEndpointDescription("Server-->HTTPS://" + httpServerAdapter.getServiceDNSName() + ":" + httpServerAdapter.getServicePortValue() + httpServerAdapter.getContextPath());
                }
            } else {
                if (StringUtils.isEmpty(httpServerAdapter.getContextPath())) {
                    httpServer.setEndpointDescription("Server-->HTTP://" + httpServerAdapter.getServiceDNSName() + ":" + httpServerAdapter.getServicePortValue() + "/");
                } else {
                    httpServer.setEndpointDescription("Server-->HTTP://" + httpServerAdapter.getServiceDNSName() + ":" + httpServerAdapter.getServicePortValue() + httpServerAdapter.getContextPath());
                }
            }
            //
            // Now build the Endpoint Participant Name
            String providerParticipantName = endpointProvider.getParticipantName();
            if (clusteredHTTPServerPort.isEncrypted()) {
                httpServer.setParticipantName(providerParticipantName + ".Server.HTTPS." + httpServerAdapter.getServiceDNSName() + "." + httpServerAdapter.getServicePortValue());
                httpServer.setParticipantDisplayName("Server.HTTPS." + httpServerAdapter.getServiceDNSName() + "." + httpServerAdapter.getServicePortValue());
            } else {
                httpServer.setParticipantName(providerParticipantName + ".Server.HTTP." + httpServerAdapter.getServiceDNSName() + "." + httpServerAdapter.getServicePortValue());
                httpServer.setParticipantDisplayName("Server.HTTP." + httpServerAdapter.getServiceDNSName() + "." + httpServerAdapter.getServicePortValue());
            }
        } else {
            //
            // This assumes that there is nothing between our exposed ports and client connections
            HTTPServerPortSegment nonClusteredHTTPServerPort = (HTTPServerPortSegment) httpServerPort;
            //
            // This is the Port Number we are actually binding to
            if (nonClusteredHTTPServerPort.getServerPort() == null) {
                throw (new IllegalArgumentException("portValue for " + nonClusteredHTTPServerPort.getName() + " is not defined in configuration file"));
            }
            httpServerAdapter.setPortNumber(nonClusteredHTTPServerPort.getServerPort());
            //
            // This is the hostname that we actually bind our port to
            if (StringUtils.isEmpty(nonClusteredHTTPServerPort.getServerHostname())) {
                throw (new IllegalArgumentException("hostDNSEntry for " + nonClusteredHTTPServerPort.getName() + "is not defined in configuration file"));
            }
            httpServerAdapter.setHostName(httpServerPort.getServerHostname());
            //
            // Set the Context Path
            String serverPath = nonClusteredHTTPServerPort.getContextPath();
            httpServerAdapter.setContextPath(serverPath);
            //
            // Should encryption (https) be used?
            httpServerAdapter.setEncrypted(nonClusteredHTTPServerPort.isEncrypted());
            //
            // Now build the Endpoint Description
            if (nonClusteredHTTPServerPort.isEncrypted()) {
                if (StringUtils.isEmpty(httpServerAdapter.getContextPath())) {
                    httpServer.setEndpointDescription("Server-->HTTPS://" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber() + "/");
                } else {
                    httpServer.setEndpointDescription("Server-->HTTPS://" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber() + httpServerAdapter.getContextPath());
                }
            } else {
                if (StringUtils.isEmpty(httpServerAdapter.getContextPath())) {
                    httpServer.setEndpointDescription("Server-->HTTP://" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber() + "/");
                } else {
                    httpServer.setEndpointDescription("Server-->HTTP://" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber() + httpServerAdapter.getContextPath());
                }
            }
            //
            // Now build the Endpoint Participant Name / Participant Display Name
            if (nonClusteredHTTPServerPort.isEncrypted()) {
                httpServer.setParticipantName(endpointProvider.getParticipantName() + "." + "Server.HTTPS." + httpServerAdapter.getHostName() + "." + httpServerAdapter.getPortNumber());
                httpServer.setParticipantDisplayName("HTTPS.Server:" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber());
            } else {
                httpServer.setParticipantName(endpointProvider.getParticipantName() + "." + "Server.HTTP." + httpServerAdapter.getHostName() + "." + httpServerAdapter.getPortNumber());
                httpServer.setParticipantDisplayName("HTTP.Server:" + httpServerAdapter.getHostName() + ":" + httpServerAdapter.getPortNumber());
            }
        }
        //
        // Add the Adapter to the endpoint
        httpServer.setHTTPServerAdapter(httpServerAdapter);
        //
        // Add the endpoint to the provider
        endpointProvider.addEndpoint(httpServer.getComponentFDN());
        //
        // Add the endpoint to the topology cache
        getLogger().trace(".newHTTPServerTopologyEndpoint(): Add the HTTP Server Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), httpServer);
        //
        // We're done
        getLogger().debug(".newHTTPServerTopologyEndpoint(): Exit, clusteredHTTPServerPort added");
        return (httpServer);
    }

    //
    // Build an HTTP Client Endpoint (Helper Method)
    //

    public StandardInteractClientTopologyEndpointPort newHTTPClientTopologyEndpoint(BaseSubsystemPropertyFile propertyFile, EndpointProviderInterface endpointProvider, String endpointFunctionName, HTTPClientPortSegment httpClientPortConfigurationSegment) {
        getLogger().debug(".newEdgeHTTPClientTopologyEndpoint(): Entry, endpointProvider->{}, httpClientPortConfigurationSegment->{}", endpointProvider, httpClientPortConfigurationSegment);
        HTTPClientTopologyEndpoint httpClient = new HTTPClientTopologyEndpoint();
        if (httpClientPortConfigurationSegment == null) {
            getLogger().debug(".newHTTPClient(): Exit, no port to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointServerName(endpointFunctionName);
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        httpClient.setComponentRDN(nodeRDN);
        httpClient.setEndpointConfigurationName(httpClientPortConfigurationSegment.getName());
        httpClient.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        httpClient.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_CLIENT);
        httpClient.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        httpClient.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        httpClient.setComponentRDN(nodeRDN);
        httpClient.setParticipantName(endpointFunctionName);
        httpClient.setContainingNodeFDN(endpointProvider.getComponentFDN());
        ConnectedSystemProperties connectedSystem = httpClientPortConfigurationSegment.getConnectedSystem();
        httpClient.setConnectedSystemName(connectedSystem.getSubsystemName());
        ConnectedExternalSystemTopologyNode externalSystem = new ConnectedExternalSystemTopologyNode();
        externalSystem.setSubsystemName(connectedSystem.getSubsystemName());
        ConnectedSystemPort targetPort1 = connectedSystem.getTargetPort1();
        HTTPClientAdapter systemEndpointPort1 = newHTTPClientAdapter(targetPort1);
        externalSystem.getTargetPorts().add(systemEndpointPort1);
        if (connectedSystem.getTargetPort2() != null) {
            ConnectedSystemPort targetPort2 = connectedSystem.getTargetPort2();
            HTTPClientAdapter systemEndpointPort2 = newHTTPClientAdapter(targetPort2);
            externalSystem.getTargetPorts().add(systemEndpointPort2);
        }
        if (connectedSystem.getTargetPort3() != null) {
            ConnectedSystemPort targetPort3 = connectedSystem.getTargetPort3();
            HTTPClientAdapter systemEndpointPort3 = newHTTPClientAdapter(targetPort3);
            externalSystem.getTargetPorts().add(systemEndpointPort3);
        }
        httpClient.setTargetSystem(externalSystem);
        //
        // Now build the Endpoint Description
        if (targetPort1.getEncryptionRequired()) {
            if (StringUtils.isEmpty(targetPort1.getTargetPath())) {
                httpClient.setEndpointDescription(endpointProvider.getParticipantName() + "." + "Client.HTTPS://" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue() + "/");
            } else {
                httpClient.setEndpointDescription(endpointProvider.getParticipantName() + "." + "Client.HTTPS://" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue() + targetPort1.getTargetPath());
            }
        } else {
            if (StringUtils.isEmpty(targetPort1.getTargetPath())) {
                httpClient.setEndpointDescription(endpointProvider.getParticipantName() + "." + "Server-->HTTP://" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue() + "/");
            } else {
                httpClient.setEndpointDescription(endpointProvider.getParticipantName() + "." + "Server-->HTTP://" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue() + targetPort1.getTargetPath());
            }
        }

        //
        // Now build the Endpoint Participant Name / Participant Display Name
        if (targetPort1.getEncryptionRequired()) {
            httpClient.setParticipantName(endpointProvider.getParticipantName() + ".Client.HTTPS." + targetPort1.getTargetPortDNSName() + "." + targetPort1.getTargetPortValue());
            httpClient.setParticipantDisplayName("HTTPS.Client:" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue());

        } else {
            httpClient.setParticipantName(endpointProvider.getParticipantName() + ".Client.HTTP." + targetPort1.getTargetPortDNSName() + "." + targetPort1.getTargetPortValue());
            httpClient.setParticipantDisplayName("HTTP.Client:" + targetPort1.getTargetPortDNSName() + ":" + targetPort1.getTargetPortValue());
        }
        //
        // Add the endpoint to the provider
        endpointProvider.addEndpoint(httpClient.getComponentFDN());
        //
        // Add the endpoint to the topology cache
        getLogger().trace(".newHTTPClient(): Add the httpClient Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), httpClient);
        //
        // We're done
        getLogger().debug(".newHTTPClient(): Exit, endpoint added");
        return (httpClient);
    }

    public HTTPClientAdapter newHTTPClientAdapter(ConnectedSystemPort connectedSystemPort) {
        getLogger().debug(".HTTPClientAdapter(): Entry, connectedSystemPort->{}", connectedSystemPort);
        HTTPClientAdapter systemEndpointPort = new HTTPClientAdapter();
        boolean encryptionRequired = false;
        if (connectedSystemPort.getEncryptionRequired() != null) {
            encryptionRequired = connectedSystemPort.getEncryptionRequired();
        }
        systemEndpointPort.setEncrypted(encryptionRequired);
        if (StringUtils.isNotEmpty(connectedSystemPort.getTargetPath())) {
            systemEndpointPort.setContextPath(connectedSystemPort.getTargetPath());
        }
        if (StringUtils.isEmpty(connectedSystemPort.getTargetPortDNSName())) {
            throw (new IllegalArgumentException("TargetPortDNSName is not defined in configuration file"));
        }
        systemEndpointPort.setHostName(connectedSystemPort.getTargetPortDNSName());
        if (connectedSystemPort.getTargetPortValue() == null) {
            throw (new IllegalArgumentException("TargetPortValue is not defined in configuration file"));
        }
        systemEndpointPort.setPortNumber(connectedSystemPort.getTargetPortValue());
        IPCAdapterDefinition currentInterfaceDefinition = new IPCAdapterDefinition();
        currentInterfaceDefinition.setInterfaceFormalName(connectedSystemPort.getTargetInterfaceDefinition().getInterfaceDefinitionName());
        currentInterfaceDefinition.setInterfaceFormalVersion(connectedSystemPort.getTargetInterfaceDefinition().getInterfaceDefinitionVersion());
        systemEndpointPort.getSupportedInterfaceDefinitions().add(currentInterfaceDefinition);
        getLogger().debug(".HTTPClientAdapter(): Exit, systemEndpointPort->{}", systemEndpointPort);
        return (systemEndpointPort);
    }

}
