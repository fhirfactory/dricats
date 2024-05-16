/*
 * The MIT License
 *
 * Copyright 2020 Mark A. Hunter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.fhirfactory.dricats.deployment.topology.factories.archetypes;

import net.fhirfactory.dricats.common.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.common.model.componentid.TopologyNodeTypeEnum;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemPort;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemProperties;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.InterfaceDefinitionSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.ClusteredInteractServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.StandardInteractClientPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.common.PetasosEnabledSubsystemTopologyFactory;
import net.fhirfactory.dricats.deployment.topology.model.common.IPCInterface;
import net.fhirfactory.dricats.deployment.topology.model.common.IPCInterfaceDefinition;
import net.fhirfactory.dricats.deployment.topology.model.endpoints.base.ExternalSystemIPCEndpoint;
import net.fhirfactory.dricats.deployment.topology.model.endpoints.common.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.deployment.topology.model.endpoints.interact.ClusteredInteractServerTopologyEndpointPort;
import net.fhirfactory.dricats.deployment.topology.model.endpoints.interact.StandardInteractClientTopologyEndpointPort;
import net.fhirfactory.dricats.deployment.topology.model.mode.ResilienceModeEnum;
import net.fhirfactory.dricats.deployment.topology.model.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.deployment.topology.model.nodes.external.ConnectedExternalSystemTopologyNode;

/**
 * @author Mark A Hunter
 */
public abstract class MITaFSubsystemTopologyFactory extends PetasosEnabledSubsystemTopologyFactory {

    //
    // Build an MLLP Server Endpoint
    //

    protected ClusteredInteractServerTopologyEndpointPort newMLLPServerEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, ClusteredInteractServerPortSegment mllpServerPort) {
        getLogger().debug(".createMLLPServerEndpoint(): Entry, endpointProvider->{}, mllpServerPort->{}", endpointProvider, mllpServerPort);
        ClusteredInteractServerTopologyEndpointPort mllpServerTopologyNode = new ClusteredInteractServerTopologyEndpointPort();
        if (mllpServerPort == null) {
            getLogger().debug(".createMLLPServerEndpoint(): Exit, no port to add");
            return (null);
        }
        mllpServerTopologyNode.setEncrypted(mllpServerPort.isEncrypted());
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.MLLP_SERVER, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createNodeRDN(name, endpointProvider.getNodeRDN().getNodeVersion(), TopologyNodeTypeEnum.ENDPOINT);
        mllpServerTopologyNode.setNodeRDN(nodeRDN);
        mllpServerTopologyNode.setActualHostIP(getActualHostIP());
        mllpServerTopologyNode.setActualPodIP(getActualPodIP());
        mllpServerTopologyNode.setName(endpointFunctionName);
        mllpServerTopologyNode.constructFDN(endpointProvider.getNodeFDN(), nodeRDN);
        mllpServerTopologyNode.setPortType(mllpServerPort.getPortType());
        mllpServerTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.MLLP_SERVER);
        mllpServerTopologyNode.setComponentType(TopologyNodeTypeEnum.ENDPOINT);
        mllpServerTopologyNode.setPortValue(mllpServerPort.getPortValue());
        mllpServerTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        mllpServerTopologyNode.setNodeRDN(nodeRDN);
        mllpServerTopologyNode.setaServer(true);
        mllpServerTopologyNode.setHostDNSName(mllpServerPort.getHostDNSEntry());
        mllpServerTopologyNode.setConnectedSystemName(mllpServerPort.getConnectedSystem().getSubsystemName());
        mllpServerTopologyNode.setContainingNodeFDN(endpointProvider.getNodeFDN());
        for (InterfaceDefinitionSegment currentSegment : mllpServerPort.getSupportedInterfaceProfiles()) {
            IPCInterface currentInterface = new IPCInterface();
            IPCInterfaceDefinition currentInterfaceDefinition = new IPCInterfaceDefinition();
            currentInterfaceDefinition.setInterfaceFormalName(currentSegment.getInterfaceDefinitionName());
            currentInterfaceDefinition.setInterfaceFormalVersion(currentSegment.getInterfaceDefinitionVersion());
            currentInterface.getSupportedInterfaceDefinitions().add(currentInterfaceDefinition);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
            mllpServerTopologyNode.getSupportedInterfaceSet().add(currentInterface);
        }
        endpointProvider.addEndpoint(mllpServerTopologyNode.getNodeFDN());
        getLogger().trace(".createMLLPServerEndpoint(): Add the createMLLPServerEndpoint Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getNodeFDN(), mllpServerTopologyNode);
        getLogger().debug(".createMLLPServerEndpoint(): Exit, endpoint added");
        return (mllpServerTopologyNode);
    }

    //
    // Build an MLLP Client Endpoint
    //

    protected StandardInteractClientTopologyEndpointPort newMLLPClientEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, StandardInteractClientPortSegment mllpClientPort) {
        getLogger().debug(".newMLLPClientEndpoint(): Entry, endpointProvider->{}, mllpClientPort->{}", endpointProvider, mllpClientPort);
        StandardInteractClientTopologyEndpointPort mllpClientTopologyNode = new StandardInteractClientTopologyEndpointPort();
        if (mllpClientPort == null) {
            getLogger().debug(".newMLLPClientEndpoint(): Exit, no port to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.MLLP_CLIENT, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createNodeRDN(name, endpointProvider.getNodeRDN().getNodeVersion(), TopologyNodeTypeEnum.ENDPOINT);
        mllpClientTopologyNode.setNodeRDN(nodeRDN);
        mllpClientTopologyNode.setActualPodIP(getActualPodIP());
        mllpClientTopologyNode.setActualHostIP(getActualHostIP());
        mllpClientTopologyNode.setName(endpointFunctionName);
        mllpClientTopologyNode.constructFDN(endpointProvider.getNodeFDN(), nodeRDN);
        mllpClientTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.MLLP_CLIENT);
        mllpClientTopologyNode.setComponentType(TopologyNodeTypeEnum.ENDPOINT);
        mllpClientTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        mllpClientTopologyNode.setNodeRDN(nodeRDN);
        mllpClientTopologyNode.setContainingNodeFDN(endpointProvider.getNodeFDN());
        ConnectedSystemProperties connectedSystem = mllpClientPort.getConnectedSystem();
        mllpClientTopologyNode.setConnectedSystemName(connectedSystem.getSubsystemName());
        ConnectedExternalSystemTopologyNode externalSystem = new ConnectedExternalSystemTopologyNode();
        externalSystem.setSubsystemName(connectedSystem.getSubsystemName());
        ConnectedSystemPort targetPort1 = connectedSystem.getTargetPort1();
        ExternalSystemIPCEndpoint systemEndpointPort1 = newExternalSystemIPCEndpoint(targetPort1, mllpClientTopologyNode.isEncrypted());
        externalSystem.getTargetPorts().add(systemEndpointPort1);
        if (connectedSystem.getTargetPort2() != null) {
            ConnectedSystemPort targetPort2 = connectedSystem.getTargetPort2();
            ExternalSystemIPCEndpoint systemEndpointPort2 = newExternalSystemIPCEndpoint(targetPort2, mllpClientTopologyNode.isEncrypted());
            externalSystem.getTargetPorts().add(systemEndpointPort2);
        }
        if (connectedSystem.getTargetPort3() != null) {
            ConnectedSystemPort targetPort3 = connectedSystem.getTargetPort3();
            ExternalSystemIPCEndpoint systemEndpointPort3 = newExternalSystemIPCEndpoint(targetPort3, mllpClientTopologyNode.isEncrypted());
            externalSystem.getTargetPorts().add(systemEndpointPort3);
        }
        mllpClientTopologyNode.setTargetSystem(externalSystem);
        endpointProvider.addEndpoint(mllpClientTopologyNode.getNodeFDN());
        getLogger().trace(".newMLLPClientEndpoint(): Add the createMLLPServerEndpoint Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getNodeFDN(), mllpClientTopologyNode);
        getLogger().debug(".newMLLPClientEndpoint(): Exit, endpoint added");
        return (mllpClientTopologyNode);
    }

    protected ExternalSystemIPCEndpoint newExternalSystemIPCEndpoint(ConnectedSystemPort connectedSystemPort, boolean defaultEncryption) {
        getLogger().debug(".newExternalSystemIPCEndpoint(): Entry, connectedSystemPort->{}", connectedSystemPort);
        ExternalSystemIPCEndpoint systemEndpointPort = new ExternalSystemIPCEndpoint();
        boolean encryptionRequired = defaultEncryption;
        if (connectedSystemPort.getEncryptionRequired() != null) {
            encryptionRequired = connectedSystemPort.getEncryptionRequired();
        }
        systemEndpointPort.setEncryptionRequired(encryptionRequired);
        systemEndpointPort.setTargetPortDNSName(connectedSystemPort.getTargetPortDNSName());
        systemEndpointPort.setTargetPortValue(connectedSystemPort.getTargetPortValue());
        IPCInterfaceDefinition currentInterfaceDefinition = new IPCInterfaceDefinition();
        currentInterfaceDefinition.setInterfaceFormalName(connectedSystemPort.getTargetInterfaceDefinition().getInterfaceDefinitionName());
        currentInterfaceDefinition.setInterfaceFormalVersion(connectedSystemPort.getTargetInterfaceDefinition().getInterfaceDefinitionVersion());
        systemEndpointPort.setSupportedInterfaceDefinition(currentInterfaceDefinition);
        getLogger().debug(".newExternalSystemIPCEndpoint(): Exit, systemEndpointPort->{}", systemEndpointPort);
        return (systemEndpointPort);
    }

    //
    // Build an HTTP Server Endpoint
    //

    protected ClusteredInteractServerTopologyEndpointPort newHTTPServerEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, ClusteredInteractServerPortSegment serverPort) {
        getLogger().debug(".newHTTPServerEndpoint(): Entry, endpointProvider->{}, serverPort->{}", endpointProvider, serverPort);
        ClusteredInteractServerTopologyEndpointPort mllpServerTopologyNode = new ClusteredInteractServerTopologyEndpointPort();
        if (serverPort == null) {
            getLogger().debug(".createMLLPServerEndpoint(): Exit, no port to add");
            return (null);
        }
        mllpServerTopologyNode.setEncrypted(serverPort.isEncrypted());
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createNodeRDN(name, endpointProvider.getNodeRDN().getNodeVersion(), TopologyNodeTypeEnum.ENDPOINT);
        mllpServerTopologyNode.setNodeRDN(nodeRDN);
        mllpServerTopologyNode.setActualHostIP(getActualHostIP());
        mllpServerTopologyNode.setActualPodIP(getActualPodIP());
        mllpServerTopologyNode.setName(endpointFunctionName);
        mllpServerTopologyNode.constructFDN(endpointProvider.getNodeFDN(), nodeRDN);
        mllpServerTopologyNode.setPortType(serverPort.getPortType());
        mllpServerTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.MLLP_SERVER);
        mllpServerTopologyNode.setComponentType(TopologyNodeTypeEnum.ENDPOINT);
        mllpServerTopologyNode.setPortValue(serverPort.getPortValue());
        mllpServerTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        mllpServerTopologyNode.setNodeRDN(nodeRDN);
        mllpServerTopologyNode.setaServer(true);
        mllpServerTopologyNode.setHostDNSName(serverPort.getHostDNSEntry());
        mllpServerTopologyNode.setConnectedSystemName(serverPort.getConnectedSystem().getSubsystemName());
        mllpServerTopologyNode.setContainingNodeFDN(endpointProvider.getNodeFDN());
        for (InterfaceDefinitionSegment currentSegment : serverPort.getSupportedInterfaceProfiles()) {
            IPCInterface currentInterface = new IPCInterface();
            IPCInterfaceDefinition currentInterfaceDefinition = new IPCInterfaceDefinition();
            currentInterfaceDefinition.setInterfaceFormalName(currentSegment.getInterfaceDefinitionName());
            currentInterfaceDefinition.setInterfaceFormalVersion(currentSegment.getInterfaceDefinitionVersion());
            currentInterface.getSupportedInterfaceDefinitions().add(currentInterfaceDefinition);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
            currentInterface.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
            mllpServerTopologyNode.getSupportedInterfaceSet().add(currentInterface);
        }
        endpointProvider.addEndpoint(mllpServerTopologyNode.getNodeFDN());
        getLogger().trace(".createMLLPServerEndpoint(): Add the createMLLPServerEndpoint Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getNodeFDN(), mllpServerTopologyNode);
        getLogger().debug(".createMLLPServerEndpoint(): Exit, endpoint added");
        return (mllpServerTopologyNode);
    }

    //
    // Build an HTTP Client Endpoint
    //

    protected StandardInteractClientTopologyEndpointPort newHTTPClientEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, StandardInteractClientPortSegment clientPort) {
        getLogger().debug(".newHTTPClientEndpoint(): Entry, endpointProvider->{}, clientPort->{}", endpointProvider, clientPort);
        StandardInteractClientTopologyEndpointPort mllpClientTopologyNode = new StandardInteractClientTopologyEndpointPort();
        if (clientPort == null) {
            getLogger().debug(".newHTTPClientEndpoint(): Exit, no port to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.HTTP_API_CLIENT, endpointFunctionName);
        TopologyNodeRDN nodeRDN = createNodeRDN(name, endpointProvider.getNodeRDN().getNodeVersion(), TopologyNodeTypeEnum.ENDPOINT);
        mllpClientTopologyNode.setNodeRDN(nodeRDN);
        mllpClientTopologyNode.setActualPodIP(getActualPodIP());
        mllpClientTopologyNode.setActualHostIP(getActualHostIP());
        mllpClientTopologyNode.setName(endpointFunctionName);
        mllpClientTopologyNode.constructFDN(endpointProvider.getNodeFDN(), nodeRDN);
        mllpClientTopologyNode.setEndpointType(PetasosEndpointTopologyTypeEnum.MLLP_CLIENT);
        mllpClientTopologyNode.setComponentType(TopologyNodeTypeEnum.ENDPOINT);
        mllpClientTopologyNode.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        mllpClientTopologyNode.setNodeRDN(nodeRDN);
        mllpClientTopologyNode.setContainingNodeFDN(endpointProvider.getNodeFDN());
        ConnectedSystemProperties connectedSystem = clientPort.getConnectedSystem();
        mllpClientTopologyNode.setConnectedSystemName(connectedSystem.getSubsystemName());
        ConnectedExternalSystemTopologyNode externalSystem = new ConnectedExternalSystemTopologyNode();
        externalSystem.setSubsystemName(connectedSystem.getSubsystemName());
        ConnectedSystemPort targetPort1 = connectedSystem.getTargetPort1();
        ExternalSystemIPCEndpoint systemEndpointPort1 = newExternalSystemIPCEndpoint(targetPort1, mllpClientTopologyNode.isEncrypted());
        externalSystem.getTargetPorts().add(systemEndpointPort1);
        if (connectedSystem.getTargetPort2() != null) {
            ConnectedSystemPort targetPort2 = connectedSystem.getTargetPort2();
            ExternalSystemIPCEndpoint systemEndpointPort2 = newExternalSystemIPCEndpoint(targetPort2, mllpClientTopologyNode.isEncrypted());
            externalSystem.getTargetPorts().add(systemEndpointPort2);
        }
        if (connectedSystem.getTargetPort3() != null) {
            ConnectedSystemPort targetPort3 = connectedSystem.getTargetPort3();
            ExternalSystemIPCEndpoint systemEndpointPort3 = newExternalSystemIPCEndpoint(targetPort3, mllpClientTopologyNode.isEncrypted());
            externalSystem.getTargetPorts().add(systemEndpointPort3);
        }
        mllpClientTopologyNode.setTargetSystem(externalSystem);
        endpointProvider.addEndpoint(mllpClientTopologyNode.getNodeFDN());
        getLogger().trace(".newHTTPClientEndpoint(): Add the newHTTPClientEndpoint Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getNodeFDN(), mllpClientTopologyNode);
        getLogger().debug(".newHTTPClientEndpoint(): Exit, endpoint added");
        return (mllpClientTopologyNode);
    }
}
