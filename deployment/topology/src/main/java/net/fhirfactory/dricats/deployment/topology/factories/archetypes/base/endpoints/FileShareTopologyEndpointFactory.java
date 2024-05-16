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

import net.fhirfactory.dricats.core.model.component.valuesets.SoftwareComponentConnectivityContextEnum;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.FileShareSinkAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.FileShareSourceAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.file.FileShareSinkTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.file.FileShareSourceTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.mode.ResilienceModeEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.core.model.topology.nodes.external.ConnectedExternalSystemTopologyNode;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemProperties;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractFileShareEndpointSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.common.TopologyFactoryHelpersBase;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.util.PegacornProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FileShareTopologyEndpointFactory extends TopologyFactoryHelpersBase {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareTopologyEndpointFactory.class);

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

    //
    // Build a FILE Source Endpoint
    //
    public FileShareSourceTopologyEndpoint newFileShareSourceEndpoint(BaseSubsystemPropertyFile propertyFile, EndpointProviderInterface endpointProvider, String endpointFunctionName, InteractFileShareEndpointSegment fileShareSource) {
        getLogger().debug(".newFileShareSourceEndpoint(): Entry, endpointProvider->{}, fileShareSource->{}", endpointProvider, fileShareSource);
        FileShareSourceTopologyEndpoint fileShareSourceTopologyNode = new FileShareSourceTopologyEndpoint();
        if (fileShareSource == null) {
            getLogger().debug(".newFileShareSourceEndpoint(): Exit, no endpoint to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SOURCE, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        fileShareSourceTopologyNode.setComponentRDN(nodeRDN);
        fileShareSourceTopologyNode.setEndpointConfigurationName(fileShareSource.getName());
        fileShareSourceTopologyNode.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        fileShareSourceTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SOURCE);
        fileShareSourceTopologyNode.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        fileShareSourceTopologyNode.setComponentSystemRole(SoftwareComponentConnectivityContextEnum.COMPONENT_ROLE_INTERACT_INGRES);
        fileShareSourceTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        fileShareSourceTopologyNode.setConnectedSystemName(fileShareSource.getConnectedSystem().getSubsystemName());
        fileShareSourceTopologyNode.setContainingNodeFDN(endpointProvider.getComponentFDN());
        fileShareSourceTopologyNode.setServer(true);

        FileShareSourceAdapter systemEndpoint = newFileShareSourceAdapter(fileShareSource);
        systemEndpoint.setTargetSystemName(fileShareSource.getConnectedSystem().getExternalisedServiceName());
        systemEndpoint.setEncrypted(fileShareSource.isEncrypted());
        systemEndpoint.setEnablingTopologyEndpoint(fileShareSourceTopologyNode.getComponentID());
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
        systemEndpoint.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);

        fileShareSourceTopologyNode.setFileShareSourceAdapter(systemEndpoint);
        fileShareSourceTopologyNode.setEndpointDescription("Server-->FILE:" + systemEndpoint.getFilePath());
        fileShareSourceTopologyNode.setParticipantName(endpointProvider.getParticipantName() + "." + "FILE.Source." + systemEndpoint.getFilePathAlias());
        fileShareSourceTopologyNode.setParticipantDisplayName("FILE.Source:" + systemEndpoint.getFilePathAlias());

        endpointProvider.addEndpoint(fileShareSourceTopologyNode.getComponentFDN());
        getLogger().warn(".newFileShareSourceEndpoint(): Add the {}/{} Port to the Topology Cache", fileShareSourceTopologyNode.getComponentRDN().getNodeName(), endpointFunctionName);
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), fileShareSourceTopologyNode);
        getLogger().debug(".newFileShareSourceEndpoint(): Exit, endpoint added->{}", fileShareSourceTopologyNode);

        return (fileShareSourceTopologyNode);
    }

    //
    // Build a FILE Sink Endpoint
    //
    public FileShareSinkTopologyEndpoint newFileShareSinkEndpoint(BaseSubsystemPropertyFile propertyFile, EndpointProviderInterface endpointProvider, String endpointFunctionName, InteractFileShareEndpointSegment fileShareSink) {
        getLogger().debug(".newFileShareSinkEndpoint(): Entry, endpointProvider->{}, mllpClientPort->{}", endpointProvider, fileShareSink);
        FileShareSinkTopologyEndpoint fileShareSinkTopologyNode = new FileShareSinkTopologyEndpoint();
        if (fileShareSink == null) {
            getLogger().debug(".newFileShareSinkEndpoint(): Exit, no endpoint to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SINK, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        fileShareSinkTopologyNode.setComponentRDN(nodeRDN);
        fileShareSinkTopologyNode.setEndpointConfigurationName(fileShareSink.getName());
        fileShareSinkTopologyNode.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        fileShareSinkTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SINK);
        fileShareSinkTopologyNode.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        fileShareSinkTopologyNode.setComponentSystemRole(SoftwareComponentConnectivityContextEnum.COMPONENT_ROLE_INTERACT_EGRESS);
        fileShareSinkTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        fileShareSinkTopologyNode.setContainingNodeFDN(endpointProvider.getComponentFDN());
        ConnectedSystemProperties connectedSystem = fileShareSink.getConnectedSystem();
        fileShareSinkTopologyNode.setConnectedSystemName(connectedSystem.getSubsystemName());
        ConnectedExternalSystemTopologyNode externalSystem = new ConnectedExternalSystemTopologyNode();
        externalSystem.setSubsystemName(connectedSystem.getSubsystemName());
        FileShareSinkAdapter systemEndpoint = newFileShareSinkAdapter(fileShareSink);
        externalSystem.getTargetPorts().add(systemEndpoint);

        fileShareSinkTopologyNode.setEndpointDescription("Client-->FILE:" + systemEndpoint.getFilePath());
        fileShareSinkTopologyNode.setParticipantName(endpointProvider.getParticipantName() + "." + "FILE.Sink." + systemEndpoint.getFilePathAlias());
        fileShareSinkTopologyNode.setParticipantDisplayName("FILE.Sink:" + systemEndpoint.getFilePathAlias());

        fileShareSinkTopologyNode.setTargetSystem(externalSystem);
        endpointProvider.addEndpoint(fileShareSinkTopologyNode.getComponentFDN());
        getLogger().warn(".newFileShareSinkEndpoint(): Add the {}/{} Port to the Topology Cache", fileShareSinkTopologyNode.getComponentRDN().getNodeName(), endpointFunctionName);
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), fileShareSinkTopologyNode);
        getLogger().debug(".newFileShareSinkEndpoint(): Exit, endpoint added");
        return (fileShareSinkTopologyNode);
    }

    public FileShareSourceAdapter newFileShareSourceAdapter(InteractFileShareEndpointSegment connectedSystem) {
        getLogger().debug(".newFileShareSourceAdapter(): Entry, connectedSystemPort->{}", connectedSystem);
        FileShareSourceAdapter systemEndpointPort = new FileShareSourceAdapter();
        if (StringUtils.isEmpty(connectedSystem.getFileSharePath())) {
            throw (new IllegalArgumentException("fileSharePath is empty"));
        }
        if (StringUtils.isEmpty(connectedSystem.getFileSharePathAlias())) {
            throw (new IllegalArgumentException("fileSharePathAlias is empty"));
        }
        systemEndpointPort.setFilePath(connectedSystem.getFileSharePath());
        systemEndpointPort.setFilePathAlias(connectedSystem.getFileSharePathAlias());
        getLogger().info(".newFileShareSourceAdapter(): Exit, systemEndpointPort->{}", systemEndpointPort);
        return (systemEndpointPort);
    }

    public FileShareSinkAdapter newFileShareSinkAdapter(InteractFileShareEndpointSegment connectedSystem) {
        getLogger().debug(".newFileShareSinkAdapter(): Entry, connectedSystemPort->{}", connectedSystem);
        FileShareSinkAdapter systemEndpointPort = new FileShareSinkAdapter();
        if (StringUtils.isEmpty(connectedSystem.getFileSharePath())) {
            throw (new IllegalArgumentException("fileSharePath is empty"));
        }
        if (StringUtils.isEmpty(connectedSystem.getFileSharePathAlias())) {
            throw (new IllegalArgumentException("fileSharePathAlias is empty"));
        }
        systemEndpointPort.setFilePath(connectedSystem.getFileSharePath());
        systemEndpointPort.setFilePathAlias(connectedSystem.getFileSharePathAlias());
        getLogger().info(".newFileShareSinkAdapter(): Exit, systemEndpointPort->{}", systemEndpointPort);
        return (systemEndpointPort);
    }
}
