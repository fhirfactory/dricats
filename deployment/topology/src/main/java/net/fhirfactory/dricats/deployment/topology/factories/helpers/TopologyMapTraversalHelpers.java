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
package net.fhirfactory.dricats.deployment.topology.factories.helpers;

import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapterDefinition;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCClusteredServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.*;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TopologyMapTraversalHelpers {
    private static final Logger LOG = LoggerFactory.getLogger(TopologyMapTraversalHelpers.class);

    @Inject
    private TopologyIM topologyIM;

    /**
     * @param subsystemNode
     * @param requiredInterface
     * @return
     */
    public BusinessServiceTopologyNode selectBusinessService(SubsystemTopologyNode subsystemNode, IPCAdapterDefinition requiredInterface) {
        if (subsystemNode == null) {
            return (null);
        }
        for (TopologyNodeFDN businessServiceNodeFDN : subsystemNode.getBusinessServices()) {
            BusinessServiceTopologyNode businessServiceNode = (BusinessServiceTopologyNode) topologyIM.getNode(businessServiceNodeFDN);
            for (TopologyNodeFDN endpointNodeFDN : businessServiceNode.getExternalisedServices()) {
                IPCClusteredServerTopologyEndpoint endpointNode = (IPCClusteredServerTopologyEndpoint) topologyIM.getNode(endpointNodeFDN);
                for (IPCAdapter currentInterface : endpointNode.getAdapterList()) {
                    for (IPCAdapterDefinition currentInterfaceDefinition : currentInterface.getSupportedInterfaceDefinitions()) {
                        boolean interfaceNameSame = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalName());
                        boolean interfaceNameVersion = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalVersion());
                        if (interfaceNameSame && interfaceNameVersion) {
                            return (businessServiceNode);
                        }
                    }
                }
            }
        }
        return (null);
    }

    /**
     * @param businessServiceTopologyNode
     * @param siteName
     * @return
     */
    public DeploymentSiteTopologyNode selectSite(BusinessServiceTopologyNode businessServiceTopologyNode, String siteName) {
        if (businessServiceTopologyNode == null) {
            return (null);
        }
        for (TopologyNodeFDN currentSiteNodeFDN : businessServiceTopologyNode.getDeploymentSites()) {
            DeploymentSiteTopologyNode currentSiteNode = (DeploymentSiteTopologyNode) topologyIM.getNode(currentSiteNodeFDN);
            boolean siteNameSame = currentSiteNode.getComponentRDN().getNodeName().contentEquals(siteName);
            if (siteNameSame) {
                return (currentSiteNode);
            }
        }
        return (null);
    }

    /**
     * @param siteNode
     * @param requiredInterface
     * @return
     */
    public ClusterServiceTopologyNode selectClusterService(DeploymentSiteTopologyNode siteNode, IPCAdapterDefinition requiredInterface) {
        if (siteNode == null) {
            return (null);
        }
        for (TopologyNodeFDN serviceNodeFDN : siteNode.getClusterServices()) {
            ClusterServiceTopologyNode serviceNode = (ClusterServiceTopologyNode) topologyIM.getNode(serviceNodeFDN);
            for (TopologyNodeFDN endpointNodeFDN : serviceNode.getServiceEndpoints()) {
                IPCClusteredServerTopologyEndpoint endpointNode = (IPCClusteredServerTopologyEndpoint) topologyIM.getNode(endpointNodeFDN);
                for (IPCAdapter currentInterface : endpointNode.getAdapterList()) {
                    for (IPCAdapterDefinition currentInterfaceDefinition : currentInterface.getSupportedInterfaceDefinitions()) {
                        boolean interfaceNameSame = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalName());
                        boolean interfaceNameVersion = currentInterfaceDefinition.getInterfaceFormalVersion().contentEquals(requiredInterface.getInterfaceFormalVersion());
                        if (interfaceNameSame && interfaceNameVersion) {
                            return (serviceNode);
                        }
                    }
                }
            }
        }
        return (null);
    }

    /**
     * @param clusterService
     * @param requiredInterface
     * @return
     */
    public IPCClusteredServerTopologyEndpoint selectClusterServiceEndpoint(ClusterServiceTopologyNode clusterService, IPCAdapterDefinition requiredInterface) {
        if (clusterService == null) {
            return (null);
        }
        for (TopologyNodeFDN endpointNodeFDN : clusterService.getServiceEndpoints()) {
            IPCClusteredServerTopologyEndpoint endpointNode = (IPCClusteredServerTopologyEndpoint) topologyIM.getNode(endpointNodeFDN);
            for (IPCAdapter currentInterface : endpointNode.getAdapterList()) {
                for (IPCAdapterDefinition currentInterfaceDefinition : currentInterface.getSupportedInterfaceDefinitions()) {
                    boolean interfaceNameSame = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalName());
                    boolean interfaceNameVersion = currentInterfaceDefinition.getInterfaceFormalVersion().contentEquals(requiredInterface.getInterfaceFormalVersion());
                    if (interfaceNameSame && interfaceNameVersion) {
                        return (endpointNode);
                    }
                }
            }
        }
        return (null);
    }

    /**
     * @param clusterService
     * @return
     */
    public PlatformTopologyNode selectPlatform(ClusterServiceTopologyNode clusterService) {
        if (clusterService == null) {
            return (null);
        }
        int platformNodeCount = clusterService.getPlatformNodes().size();
        if (platformNodeCount <= 0) {
            return (null);
        }
        if (platformNodeCount == 1) {
            TopologyNodeFDN platformTopologyNodeFDN = clusterService.getPlatformNodes().get(0);
            PlatformTopologyNode platformTopologyNode = (PlatformTopologyNode) topologyIM.getNode(platformTopologyNodeFDN);
            return (platformTopologyNode);
        }
        Long randomEntryNumberLong = Math.round((Math.random() * platformNodeCount));
        int randomEntryNumber = Math.toIntExact(randomEntryNumberLong);
        TopologyNodeFDN platformTopologyNodeFDN = clusterService.getPlatformNodes().get(randomEntryNumber);
        PlatformTopologyNode platformTopologyNode = (PlatformTopologyNode) topologyIM.getNode(platformTopologyNodeFDN);
        return (platformTopologyNode);
    }

    /**
     * @param platformTopologyNode
     * @param requiredInterface
     * @return
     */
    public ProcessingPlantSoftwareComponent selectProcessingPlant(PlatformTopologyNode platformTopologyNode, IPCAdapterDefinition requiredInterface) {
        if (platformTopologyNode == null) {
            return (null);
        }
        for (TopologyNodeFDN currentProcessingPlantFDN : platformTopologyNode.getProcessingPlants()) {
            ProcessingPlantSoftwareComponent currentProcessingPlant = (ProcessingPlantSoftwareComponent) topologyIM.getNode(currentProcessingPlantFDN);
            for (TopologyNodeFDN endpointNodeFDN : currentProcessingPlant.getEndpoints()) {
                IPCClusteredServerTopologyEndpoint endpointNode = (IPCClusteredServerTopologyEndpoint) topologyIM.getNode(endpointNodeFDN);
                for (IPCAdapter currentInterface : endpointNode.getAdapterList()) {
                    for (IPCAdapterDefinition currentInterfaceDefinition : currentInterface.getSupportedInterfaceDefinitions()) {
                        boolean interfaceNameSame = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalName());
                        boolean interfaceNameVersion = currentInterfaceDefinition.getInterfaceFormalVersion().contentEquals(requiredInterface.getInterfaceFormalVersion());
                        if (interfaceNameSame && interfaceNameVersion) {
                            return (currentProcessingPlant);
                        }
                    }
                }
            }
        }
        return (null);
    }

    /**
     * @param processingPlantNode
     * @param requiredInterface
     * @return
     */
    public IPCClusteredServerTopologyEndpoint selectProcessingPlantEndpoint(ProcessingPlantSoftwareComponent processingPlantNode, IPCAdapterDefinition requiredInterface) {
        if (processingPlantNode == null) {
            return (null);
        }
        for (TopologyNodeFDN endpointNodeFDN : processingPlantNode.getEndpoints()) {
            IPCClusteredServerTopologyEndpoint endpointNode = (IPCClusteredServerTopologyEndpoint) topologyIM.getNode(endpointNodeFDN);
            for (IPCAdapter currentInterface : endpointNode.getAdapterList()) {
                for (IPCAdapterDefinition currentInterfaceDefinition : currentInterface.getSupportedInterfaceDefinitions()) {
                    boolean interfaceNameSame = currentInterfaceDefinition.getInterfaceFormalName().contentEquals(requiredInterface.getInterfaceFormalName());
                    boolean interfaceNameVersion = currentInterfaceDefinition.getInterfaceFormalVersion().contentEquals(requiredInterface.getInterfaceFormalVersion());
                    if (interfaceNameSame && interfaceNameVersion) {
                        return (endpointNode);
                    }
                }
            }
        }
        return (null);
    }

}
