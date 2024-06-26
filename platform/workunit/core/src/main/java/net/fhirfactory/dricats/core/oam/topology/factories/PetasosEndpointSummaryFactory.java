
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
package net.fhirfactory.dricats.core.oam.topology.factories;

import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.endpoint.PetasosEndpoint;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.FileShareSinkAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.FileShareSourceAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPClientAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.datatypes.JGroupsAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.file.FileShareSinkTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.file.FileShareSourceTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPClientTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.mllp.MLLPClientEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.mllp.MLLPServerEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.mllp.adapters.MLLPClientAdapter;
import net.fhirfactory.dricats.core.model.ui.resources.summaries.EndpointSummary;
import net.fhirfactory.dricats.core.model.ui.resources.summaries.PortSoftwareComponentSummary;
import net.fhirfactory.dricats.core.model.ui.resources.summaries.SoftwareComponentSummary;
import net.fhirfactory.dricats.core.oam.topology.factories.common.PetasosMonitoredComponentFactory;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PetasosEndpointSummaryFactory extends PetasosMonitoredComponentFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosEndpointSummaryFactory.class);

    @Inject
    private TopologyIM topologyIM;

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    public SoftwareComponentSummary newEndpoint(String wupParticipantName, PetasosEndpoint petasosEndpoint) {
        getLogger().debug(".newEndpoint(): Entry, endpointTopologyNode->{}", petasosEndpoint);

        EndpointSummary endpoint = new EndpointSummary();
        endpoint = (EndpointSummary) newPetasosMonitoredComponent(endpoint, petasosEndpoint);
        endpoint.setEndpointType(petasosEndpoint.getEndpointType());
        boolean isServer = petasosEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER) ||
                petasosEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.MLLP_SERVER) ||
                petasosEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.OTHER_API_SERVER) ||
                petasosEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.OTHER_SERVER);
        endpoint.setServer(isServer);
        endpoint.setWupParticipantName(wupParticipantName);
        if (StringUtils.isNotEmpty(petasosEndpoint.getParticipantDisplayName())) {
            endpoint.setParticipantDisplayName(petasosEndpoint.getParticipantDisplayName());
        } else {
            endpoint.setParticipantDisplayName(endpoint.getWupParticipantName());
        }
        getLogger().debug(".newEndpoint(): Exit, endpoint->{}", endpoint);
        return (endpoint);
    }


    public SoftwareComponentSummary newEndpoint(String wupParticipantName, IPCTopologyEndpoint endpointTopologyNode) {
        getLogger().debug(".newEndpoint(): Entry, endpointTopologyNode->{}", endpointTopologyNode);
        if (endpointTopologyNode == null) {
            return (null);
        }
        EndpointSummary endpoint = new EndpointSummary();
        endpoint = (EndpointSummary) newPetasosMonitoredComponent(endpoint, endpointTopologyNode);
        endpoint.setEndpointType(endpointTopologyNode.getEndpointType());
        endpoint.setWupParticipantName(wupParticipantName);
        if (StringUtils.isNotEmpty(endpointTopologyNode.getParticipantDisplayName())) {
            endpoint.setParticipantDisplayName(endpointTopologyNode.getParticipantDisplayName());
        } else {
            endpoint.setParticipantDisplayName(endpoint.getWupParticipantName());
        }
        boolean isEncrypted = false;
        for (IPCAdapter currentAdapter : endpointTopologyNode.getAdapterList()) {
            if (currentAdapter.isEncrypted()) {
                isEncrypted = true;
                break;
            }
        }
        switch (endpointTopologyNode.getEndpointType()) {
            case JGROUPS_INTEGRATION_POINT: {
                IPCServerTopologyEndpoint jgroupsEndpoint = (IPCServerTopologyEndpoint) endpointTopologyNode;
                if (jgroupsEndpoint.getAdapterList() != null) {
                    for (IPCAdapter currentAdapter : jgroupsEndpoint.getAdapterList()) {
                        JGroupsAdapter jgroupsAdapter = (JGroupsAdapter) currentAdapter;
                        PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                        portSummary.setEncrypted(isEncrypted);
                        portSummary.setPortType(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT.getDisplayName());
                        if (jgroupsAdapter.getPortNumber() != null) {
                            portSummary.setHostPort(Integer.toString(jgroupsAdapter.getPortNumber()));
                        } else {
                            portSummary.setHostPort("Unknown");
                        }
                        portSummary.setHostDNSName(jgroupsAdapter.getHostName());
                        ComponentIdType componentId = new ComponentIdType();
                        componentId.setId(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                        componentId.setDisplayName(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                        portSummary.setComponentID(componentId);
                        endpoint.getClientPorts().add(portSummary);
                    }
                }
                break;
            }
            case HTTP_API_SERVER:
                HTTPServerTopologyEndpoint edgeHTTPServer = (HTTPServerTopologyEndpoint) endpointTopologyNode;
                if (edgeHTTPServer.getHTTPServerAdapter() != null) {
                    PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                    portSummary.setHostPort(Integer.toString(edgeHTTPServer.getHTTPServerAdapter().getPortNumber()));
                    portSummary.setHostDNSName(edgeHTTPServer.getHTTPServerAdapter().getHostName());
                    if (StringUtils.isNotEmpty(edgeHTTPServer.getConnectedSystemName())) {
                        endpoint.setConnectedSystemName(edgeHTTPServer.getConnectedSystemName());
                    } else {
                        endpoint.setConnectedSystemName("Not Specified In Configuration");
                    }
                    if (edgeHTTPServer.getHTTPServerAdapter().getServicePortValue() != null) {
                        portSummary.setServicePort(Integer.toString(edgeHTTPServer.getHTTPServerAdapter().getServicePortValue()));
                    } else {
                        portSummary.setServicePort("Unknown");
                    }
                    if (StringUtils.isNotEmpty(edgeHTTPServer.getHTTPServerAdapter().getServiceDNSName())) {
                        portSummary.setServiceDNSName(edgeHTTPServer.getHTTPServerAdapter().getServiceDNSName());
                    } else {
                        portSummary.setServiceDNSName("Unknown");
                    }
                    portSummary.setPortType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER.getDisplayName());
                    ComponentIdType componentId = new ComponentIdType();
                    componentId.setId(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                    componentId.setDisplayName(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                    portSummary.setComponentID(componentId);
                    endpoint.getServerPorts().add(portSummary);
                    endpoint.setServer(true);
                }
                break;
            case HTTP_API_CLIENT:
                HTTPClientTopologyEndpoint httpClient = (HTTPClientTopologyEndpoint) endpointTopologyNode;
                if (httpClient.getHTTPClientAdapters() != null) {
                    for (HTTPClientAdapter currentAdapter : httpClient.getHTTPClientAdapters()) {
                        PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                        portSummary.setComponentID(httpClient.getComponentID());
                        portSummary.setHostPort(Integer.toString(currentAdapter.getPortNumber()));
                        portSummary.setHostDNSName(currentAdapter.getHostName());
                        if (httpClient.hasConnectedSystemName()) {
                            endpoint.setConnectedSystemName(httpClient.getConnectedSystemName());
                        } else {
                            endpoint.setConnectedSystemName("Not Specified In Configuration");
                        }
                        if (currentAdapter.getPortNumber() != null) {
                            portSummary.setServicePort(Integer.toString(currentAdapter.getPortNumber()));
                        } else {
                            portSummary.setServicePort("Unknown");
                        }
                        if (StringUtils.isNotEmpty(currentAdapter.getHostName())) {
                            portSummary.setServiceDNSName(currentAdapter.getHostName());
                        } else {
                            portSummary.setServiceDNSName("Unknown");
                        }
                        portSummary.setPortType(PetasosEndpointTopologyTypeEnum.HTTP_API_CLIENT.getDisplayName());
                        ComponentIdType componentId = new ComponentIdType();
                        componentId.setId(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                        componentId.setDisplayName(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                        portSummary.setComponentID(componentId);
                        endpoint.getServerPorts().add(portSummary);
                    }
                }
                break;
            case MLLP_SERVER: {
                MLLPServerEndpoint mllpServerEndpoint = (MLLPServerEndpoint) endpointTopologyNode;
                if (mllpServerEndpoint.getMLLPServerAdapter() != null) {
                    PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                    portSummary.setParticipantName(mllpServerEndpoint.getParticipantName());
                    portSummary.setParticipantDisplayName(mllpServerEndpoint.getParticipantDisplayName());
                    portSummary.setComponentID(mllpServerEndpoint.getComponentID());
                    portSummary.setHostPort(Integer.toString(mllpServerEndpoint.getMLLPServerAdapter().getPortNumber()));
                    portSummary.setHostDNSName(mllpServerEndpoint.getMLLPServerAdapter().getHostName());
                    if (mllpServerEndpoint.hasConnectedSystemName()) {
                        endpoint.setConnectedSystemName(mllpServerEndpoint.getConnectedSystemName());
                    } else {
                        endpoint.setConnectedSystemName("Not Specified In Configuration");
                    }
                    if (mllpServerEndpoint.getMLLPServerAdapter().getServicePortValue() != null) {
                        portSummary.setServicePort(Integer.toString(mllpServerEndpoint.getMLLPServerAdapter().getServicePortValue()));
                    } else {
                        portSummary.setServicePort("Unknown");
                    }
                    if (StringUtils.isNotEmpty(mllpServerEndpoint.getMLLPServerAdapter().getServiceDNSName())) {
                        portSummary.setServiceDNSName(mllpServerEndpoint.getMLLPServerAdapter().getServiceDNSName());
                    } else {
                        portSummary.setServiceDNSName("Unknown");
                    }
                    portSummary.setPortType(PetasosEndpointTopologyTypeEnum.MLLP_SERVER.getDisplayName());
                    ComponentIdType componentId = new ComponentIdType();
                    componentId.setId(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                    componentId.setDisplayName(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                    portSummary.setComponentID(componentId);
                    endpoint.getServerPorts().add(portSummary);
                    endpoint.setServer(true);
                }
                break;
            }
            case MLLP_CLIENT: {
                MLLPClientEndpoint mllpClientEndpoint = (MLLPClientEndpoint) endpointTopologyNode;
                if (mllpClientEndpoint.getMLLPClientAdapters() != null) {
                    if (!mllpClientEndpoint.getMLLPClientAdapters().isEmpty()) {
                        for (MLLPClientAdapter currentAdapter : mllpClientEndpoint.getMLLPClientAdapters()) {
                            PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                            portSummary.setParticipantName(mllpClientEndpoint.getParticipantName());
                            portSummary.setParticipantDisplayName(mllpClientEndpoint.getParticipantDisplayName());
                            portSummary.setEncrypted(currentAdapter.isEncrypted());
                            portSummary.setPortType(PetasosEndpointTopologyTypeEnum.MLLP_CLIENT.getDisplayName());
                            if (mllpClientEndpoint.hasConnectedSystemName()) {
                                endpoint.setConnectedSystemName(mllpClientEndpoint.getConnectedSystemName());
                            } else {
                                endpoint.setConnectedSystemName("Not Specified In Configuration");
                            }
                            portSummary.setHostDNSName(currentAdapter.getHostName());
                            if (currentAdapter.getPortNumber() != null) {
                                portSummary.setHostPort(Integer.toString(currentAdapter.getPortNumber()));
                            } else {
                                portSummary.setHostPort("Unknown");
                            }
                            ComponentIdType componentId = new ComponentIdType();
                            componentId.setId(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                            componentId.setDisplayName(portSummary.getHostDNSName() + "-" + portSummary.getHostPort());
                            portSummary.setComponentID(componentId);
                            endpoint.getServerPorts().add(portSummary);
                        }
                    }
                }
                break;
            }

            case SQL_SERVER:
                break;
            case SQL_CLIENT:
                break;
            case LDAP_SERVER:
                break;
            case LDAP_CLIENT:
                break;
            case FILE_SHARE_SOURCE: {
                FileShareSourceTopologyEndpoint fileSourceEndpoint = (FileShareSourceTopologyEndpoint) endpointTopologyNode;
                if (fileSourceEndpoint.getAdapterList() != null) {
                    if (!fileSourceEndpoint.getAdapterList().isEmpty()) {
                        for (IPCAdapter currentIPCAdapter : fileSourceEndpoint.getAdapterList()) {
                            FileShareSourceAdapter currentAdapter = (FileShareSourceAdapter) currentIPCAdapter;
                            PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                            portSummary.setParticipantName(fileSourceEndpoint.getParticipantName());
                            portSummary.setParticipantDisplayName(fileSourceEndpoint.getParticipantDisplayName());
                            portSummary.setEncrypted(currentAdapter.isEncrypted());
                            portSummary.setPortType(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SOURCE.getDisplayName());
                            if (fileSourceEndpoint.hasConnectedSystemName()) {
                                endpoint.setConnectedSystemName(fileSourceEndpoint.getConnectedSystemName());
                            } else {
                                endpoint.setConnectedSystemName("Not Specified In Configuration");
                            }
                            portSummary.setParticipantDisplayName(currentAdapter.getFilePathAlias());
                            portSummary.setBasePath(currentAdapter.getFilePath());
                            portSummary.setHostDNSName(currentAdapter.getHostName());
                            ComponentIdType componentId = new ComponentIdType();
                            componentId.setId(fileSourceEndpoint.getSubsystemParticipantName() + "-" + portSummary.getHostDNSName());
                            componentId.setDisplayName(fileSourceEndpoint.getSubsystemParticipantName() + "-" + portSummary.getHostDNSName());
                            portSummary.setComponentID(componentId);
                            endpoint.getServerPorts().add(portSummary);
                        }
                    }
                }
                break;
            }
            case FILE_SHARE_SINK: {
                FileShareSinkTopologyEndpoint fileSinkEndpoint = (FileShareSinkTopologyEndpoint) endpointTopologyNode;
                if (fileSinkEndpoint.getAdapterList() != null) {
                    if (!fileSinkEndpoint.getAdapterList().isEmpty()) {
                        for (IPCAdapter currentIPCAdapter : fileSinkEndpoint.getAdapterList()) {
                            FileShareSinkAdapter currentAdapter = (FileShareSinkAdapter) currentIPCAdapter;
                            PortSoftwareComponentSummary portSummary = new PortSoftwareComponentSummary();
                            portSummary.setParticipantName(fileSinkEndpoint.getParticipantName());
                            portSummary.setParticipantDisplayName(fileSinkEndpoint.getParticipantDisplayName());
                            portSummary.setEncrypted(currentAdapter.isEncrypted());
                            portSummary.setPortType(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SINK.getDisplayName());
                            if (fileSinkEndpoint.hasConnectedSystemName()) {
                                endpoint.setConnectedSystemName(fileSinkEndpoint.getConnectedSystemName());
                            } else {
                                endpoint.setConnectedSystemName("Not Specified In Configuration");
                            }
                            portSummary.setHostDNSName(currentAdapter.getHostName());
                            portSummary.setBasePath(currentAdapter.getFilePath());
                            portSummary.setParticipantDisplayName(currentAdapter.getFilePathAlias());
                            ComponentIdType componentId = new ComponentIdType();
                            componentId.setId(fileSinkEndpoint.getSubsystemParticipantName() + "-" + portSummary.getHostDNSName());
                            componentId.setDisplayName(fileSinkEndpoint.getSubsystemParticipantName() + "-" + portSummary.getHostDNSName());
                            portSummary.setComponentID(componentId);
                            endpoint.getServerPorts().add(portSummary);
                        }
                    }
                }
                break;
            }
            case OTHER_API_SERVER:
                break;
            case OTHER_API_CLIENT:
                break;
            case OTHER_SERVER:
                break;
            case OTHER_CLIENT:
                break;
        }
        getLogger().debug(".newEndpoint(): Exit, endpoint->{}", endpoint);
        return (endpoint);
    }
}
