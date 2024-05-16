/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.deployment.topology.manager;

import net.fhirfactory.dricats.core.model.component.SoftwareComponent;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDNToken;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.mode.NetworkSecurityZoneEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.*;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.interfaces.SolutionNodeFactoryInterface;
import net.fhirfactory.dricats.deployment.topology.manager.cache.TopologyNodesDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * This class WILL do more in the future, but it is for now just a proxy to the
 * TopologyDM.
 */
@ApplicationScoped
public class TopologyIM {

    private static final Logger LOG = LoggerFactory.getLogger(TopologyIM.class);
    private boolean initialised;

    @Inject
    private TopologyNodesDM topologyDataManager;

    @Inject
    private SolutionNodeFactoryInterface solutionNodeFactory;

    public TopologyIM() {
        this.initialised = false;
    }

    @PostConstruct
    public void initialise() {
        LOG.debug(".initialise(): Entry");
        if (!this.initialised) {
            LOG.info(".initialise(): Initalising......");
            LOG.info(".initialise(): [Solution Node Factory Initialisation]: Start");
            solutionNodeFactory.initialise();
            LOG.info(".initialise(): [Solution Node Factory Initialisation]: Finish");
            LOG.info(".initialise(): [Solution Node Resolution]: Start");
            SolutionTopologyNode solutionNode = solutionNodeFactory.getSolutionTopologyNode();
            LOG.info(".initialise(): [Solution Node Resolution]: Finish");
            LOG.info(".initialise(): [Solution Node Assignment]: Start");
            setDeploymentSolution(solutionNode);
            LOG.info(".initialise(): [Solution Node Assignment]: Finish");
            this.initialised = true;
            LOG.info(".initialise(): Done.");
        } else {
            LOG.debug(".initialise(): Already initialised, nothing to do....");
        }
        LOG.debug(".initialise(): Exit");
    }

    public void setDeploymentSolution(SolutionTopologyNode solution) {
        LOG.debug(".setDeploymentSolution(): Entry, solution->{}", solution);
        topologyDataManager.setDeploymentSolution(solution);
        LOG.debug(".setDeploymentSolution(): Exit");
    }

    public SolutionTopologyNode getSolutionTopology() {
        SolutionTopologyNode solution = (SolutionTopologyNode) topologyDataManager.getDeploymentSolution();
        return (solution);
    }

    public void addTopologyNode(TopologyNodeFDN parentNodeFDN, SoftwareComponent newNodeElement) {
        LOG.debug(".addTopologyNode(): Entry, parentNodeFDN->{}, newElement->{}", parentNodeFDN, newNodeElement);

        newNodeElement.setContainingNodeFDN(parentNodeFDN);
        topologyDataManager.addTopologyNode(newNodeElement);
        SoftwareComponent parentNodeElement = getNode(parentNodeFDN);
        switch (newNodeElement.getComponentType()) {
            case SUBSYSTEM: {
                SolutionTopologyNode solution = (SolutionTopologyNode) parentNodeElement;
                LOG.trace(".addTopologyNode(): Adding a Subsystem, parent Solution->{}", solution);
                SubsystemTopologyNode subsystem = (SubsystemTopologyNode) newNodeElement;
                TopologyNodeFDN newNodeFDN = subsystem.getComponentFDN();
                if (!solution.getSubsystemList().contains(newNodeFDN)) {
                    solution.getSubsystemList().add(newNodeFDN);
                }
                break;
            }
            case EXTERNALISED_SERVICE: {
                SubsystemTopologyNode subsystem = (SubsystemTopologyNode) parentNodeElement;
                BusinessServiceTopologyNode businessService = (BusinessServiceTopologyNode) newNodeElement;
                if (!subsystem.getBusinessServices().contains(businessService.getComponentFDN())) {
                    subsystem.getBusinessServices().add(businessService.getComponentFDN());
                }
                break;
            }
            case SITE: {
                BusinessServiceTopologyNode businessService = (BusinessServiceTopologyNode) parentNodeElement;
                DeploymentSiteTopologyNode deploymentSite = (DeploymentSiteTopologyNode) newNodeElement;
                if (!businessService.getDeploymentSites().contains(deploymentSite.getComponentFDN())) {
                    businessService.getDeploymentSites().add(deploymentSite.getComponentFDN());
                }
                break;
            }
            case CLUSTER_SERVICE: {
                DeploymentSiteTopologyNode deploymentSite = (DeploymentSiteTopologyNode) parentNodeElement;
                ClusterServiceTopologyNode clusterService = (ClusterServiceTopologyNode) newNodeElement;
                if (!deploymentSite.getClusterServices().contains(clusterService.getComponentFDN())) {
                    deploymentSite.getClusterServices().add(clusterService.getComponentFDN());
                }
                break;
            }
            case PLATFORM: {
                ClusterServiceTopologyNode clusterService = (ClusterServiceTopologyNode) parentNodeElement;
                PlatformTopologyNode platformTopologyNode = (PlatformTopologyNode) newNodeElement;
                if (!clusterService.getPlatformNodes().contains(platformTopologyNode.getComponentFDN())) {
                    clusterService.getPlatformNodes().add(platformTopologyNode.getComponentFDN());
                }
                break;
            }
            case PROCESSING_PLANT: {
                PlatformTopologyNode platformTopologyNode = (PlatformTopologyNode) parentNodeElement;
                ProcessingPlantSoftwareComponent processingPlant = (ProcessingPlantSoftwareComponent) newNodeElement;
                if (!platformTopologyNode.getProcessingPlants().contains(processingPlant.getComponentFDN())) {
                    platformTopologyNode.getProcessingPlants().add(processingPlant.getComponentFDN());
                }
                break;
            }
            case WORKSHOP: {
                ProcessingPlantSoftwareComponent processingPlant = (ProcessingPlantSoftwareComponent) parentNodeElement;
                WorkshopSoftwareComponent workshop = (WorkshopSoftwareComponent) newNodeElement;
                if (!processingPlant.getWorkshops().contains(workshop.getComponentFDN())) {
                    processingPlant.getWorkshops().add(workshop.getComponentFDN());
                }
                break;
            }
            case WUP: {
                WorkshopSoftwareComponent workshop = (WorkshopSoftwareComponent) parentNodeElement;
                WorkUnitProcessorSoftwareComponent wup = (WorkUnitProcessorSoftwareComponent) newNodeElement;
                if (!workshop.getWupSet().contains(wup.getComponentFDN())) {
                    workshop.getWupSet().add(wup.getComponentFDN());
                    WorkUnitProcessorSubComponentSoftwareComponent wupCore = new WorkUnitProcessorSubComponentSoftwareComponent();
                    wupCore.setContainingNodeFDN(workshop.getComponentFDN());
                    wupCore.setComponentType(PegacornSystemComponentTypeTypeEnum.WUP_CORE);
                    TopologyNodeFDN newFDN = new TopologyNodeFDN(wup.getComponentFDN());
                    newFDN.appendTopologyNodeRDN(wup.getComponentRDN());
                    wupCore.setComponentFDN(newFDN);
                    wupCore.constructFunctionFDN(wup.getNodeFunctionFDN(), wup.getComponentRDN());
                    topologyDataManager.addTopologyNode(wupCore);
                }
                break;
            }
            case WUP_INTERCHANGE_ROUTER:
            case WUP_INTERCHANGE_PAYLOAD_TRANSFORMER: {
                WorkUnitProcessorSoftwareComponent wup = (WorkUnitProcessorSoftwareComponent) parentNodeElement;
                WorkUnitProcessorInterchangeSoftwareComponent wupInterchangeComponent = (WorkUnitProcessorInterchangeSoftwareComponent) newNodeElement;
                if (!wup.getWupInterchangeComponents().contains(wupInterchangeComponent.getComponentFDN())) {
                    wup.getWupInterchangeComponents().add(wupInterchangeComponent.getComponentFDN());
                }
                break;
            }
            case WUP_CORE:
            case WUP_CONTAINER_EGRESS_CONDUIT:
            case WUP_CONTAINER_INGRES_CONDUIT:
            case WUP_CONTAINER_EGRESS_PROCESSOR:
            case WUP_CONTAINER_INGRES_PROCESSOR:
            case WUP_CONTAINER_EGRESS_GATEKEEPER:
            case WUP_CONTAINER_INGRES_GATEKEEPER: {
                WorkUnitProcessorSoftwareComponent wup = (WorkUnitProcessorSoftwareComponent) parentNodeElement;
                WorkUnitProcessorSubComponentSoftwareComponent wupComponent = (WorkUnitProcessorSubComponentSoftwareComponent) newNodeElement;
                if (!wup.getWupInterchangeComponents().contains(wupComponent.getComponentFDN())) {
                    wup.getWupComponents().add(wupComponent.getComponentFDN());
                }
                break;
            }
            case ENDPOINT: {
                switch (parentNodeElement.getComponentType()) {
                    case EXTERNALISED_SERVICE: {
                        BusinessServiceTopologyNode businessService = (BusinessServiceTopologyNode) parentNodeElement;
                        IPCTopologyEndpoint endpoint = (IPCTopologyEndpoint) newNodeElement;
                        if (!businessService.getExternalisedServices().contains(endpoint.getComponentFDN())) {
                            businessService.getExternalisedServices().add(endpoint.getComponentFDN());
                        }
                        break;
                    }
                    case CLUSTER_SERVICE: {
                        ClusterServiceTopologyNode clusterService = (ClusterServiceTopologyNode) parentNodeElement;
                        IPCTopologyEndpoint endpoint = (IPCTopologyEndpoint) newNodeElement;
                        if (!clusterService.getServiceEndpoints().contains(endpoint.getComponentFDN())) {
                            clusterService.getServiceEndpoints().add(endpoint.getComponentFDN());
                        }
                        break;
                    }
                    case PROCESSING_PLANT: {
                        ProcessingPlantSoftwareComponent processingPlant = (ProcessingPlantSoftwareComponent) parentNodeElement;
                        IPCTopologyEndpoint endpoint = (IPCTopologyEndpoint) newNodeElement;
                        if (!processingPlant.getEndpoints().contains(endpoint.getComponentFDN())) {
                            processingPlant.getEndpoints().add(endpoint.getComponentFDN());
                        }
                        break;
                    }
                    case WUP: {
                        WorkUnitProcessorSoftwareComponent wup = (WorkUnitProcessorSoftwareComponent) parentNodeElement;
                        IPCTopologyEndpoint endpoint = (IPCTopologyEndpoint) newNodeElement;
                        switch (endpoint.getComponentSystemRole()) {
                            case COMPONENT_ROLE_SUBSYSTEM_EDGE: {
                                wup.setEgressEndpoint(endpoint);
                                wup.setIngresEndpoint(endpoint);
                                break;
                            }
                            case COMPONENT_ROLE_INTERACT_EGRESS: {
                                wup.setEgressEndpoint(endpoint);
                                break;
                            }
                            case COMPONENT_ROLE_INTERACT_INGRES: {
                                wup.setIngresEndpoint(endpoint);
                                break;
                            }
                            default: {
                                wup.setEgressEndpoint(endpoint);
                                wup.setIngresEndpoint(endpoint);
                            }
                        }
                        break;
                    }
                    default:
                        // Do nothing
                }
            }
            case SOLUTION:
            default: {
                // Do nothing
            }
        }
    }

    public void removeNode(TopologyNodeFDN elementID) {
        LOG.debug(".unregisterNode(): Entry, elementID --> {}", elementID);
        topologyDataManager.deleteTopologyNode(elementID);
    }

    public Set<SoftwareComponent> getNodeElementSet() {
        LOG.debug(".getNodeElementSet(): Entry");
        Set<SoftwareComponent> topologyNodeSet = topologyDataManager.getTopologyNodeSet();
        LOG.debug(".getNodeElementSet(): Exit");
        return (topologyNodeSet);
    }

    public SoftwareComponent getNode(TopologyNodeFDN nodeID) {
        LOG.debug(".getNode(): Entry, nodeID --> {}", nodeID);
        SoftwareComponent retrievedNode = topologyDataManager.getSoftwareComponent(nodeID);
        LOG.debug(".getNode(): Exit, retrievedNode --> {}", retrievedNode);
        return (retrievedNode);
    }

    public SoftwareComponent getNode(TopologyNodeFDNToken nodeFDNToken) {
        LOG.debug(".getNode(): Entry, nodeFDNToken --> {}", nodeFDNToken);
        TopologyNodeFDN nodeFDN = new TopologyNodeFDN(nodeFDNToken);
        SoftwareComponent retrievedNode = getNode(nodeFDN);
        LOG.debug(".getNode(): Exit, retrievedNode --> {}", retrievedNode);
        return (retrievedNode);
    }

    public SoftwareComponent getNode(ComponentIdType componentId) {
        LOG.debug(".getNode(): Entry, componentId --> {}", componentId);
        SoftwareComponent retrievedNode = topologyDataManager.getSoftwareComponent(componentId);
        LOG.debug(".getNode(): Exit, retrievedNode --> {}", retrievedNode);
        return (retrievedNode);
    }

    public SoftwareComponent getNode(String participantName) {
        LOG.debug(".getNode(): Entry, participantName --> {}", participantName);
        SoftwareComponent retrievedNode = topologyDataManager.getSoftwareComponent(participantName);
        LOG.debug(".getNode(): Exit, retrievedNode --> {}", retrievedNode);
        return (retrievedNode);
    }

    public List<SoftwareComponent> nodeSearch(PegacornSystemComponentTypeTypeEnum nodeType, String nodeName, String nodeVersion) {
        List<SoftwareComponent> nodeList = topologyDataManager.getSoftwareComponent(nodeType, nodeName, nodeVersion);
        return (nodeList);
    }

    public NetworkSecurityZoneEnum getDeploymentNetworkSecurityZone(String nodeName) {
        return (getDeploymentNetworkSecurityZone(nodeName));
    }
}
