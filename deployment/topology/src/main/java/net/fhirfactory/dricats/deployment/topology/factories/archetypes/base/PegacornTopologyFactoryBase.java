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
package net.fhirfactory.dricats.deployment.topology.factories.archetypes.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.fhirfactory.dricats.core.interfaces.topology.PegacornTopologyFactoryInterface;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFunctionFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.endpoint.JGroupsIntegrationPointNamingUtilities;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantStatusEnum;
import net.fhirfactory.dricats.core.model.participant.ProcessingPlantPetasosParticipantHolder;
import net.fhirfactory.dricats.core.model.participant.ProcessingPlantPetasosParticipantNameHolder;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.HTTPServerAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.http.HTTPServerTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.mode.ConcurrencyModeEnum;
import net.fhirfactory.dricats.core.model.topology.mode.NetworkSecurityZoneEnum;
import net.fhirfactory.dricats.core.model.topology.mode.ResilienceModeEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.*;
import net.fhirfactory.dricats.deployment.names.sites.SiteKeyNames;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.datatypes.ParameterNameValuePairType;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.common.TopologyFactoryHelpersBase;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.interfaces.SolutionNodeFactoryInterface;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.util.PegacornProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PegacornTopologyFactoryBase extends TopologyFactoryHelpersBase implements PegacornTopologyFactoryInterface {

    private static final String PROPERTY_FILENAME_EXTENSION = ".yaml";

    private BaseSubsystemPropertyFile propertyFile;
    private ProcessingPlantSoftwareComponent processingPlantNode;
    private boolean initialised;

    @Inject
    private SiteKeyNames siteKeyNames;

    @Inject
    private TopologyIM topologyIM;

    @Inject
    private SolutionNodeFactoryInterface solutionNodeInterface;

    @Inject
    private PegacornCommonInterfaceNames interfaceNames;

    @Inject
    private PegacornProperties pegacornProperties;

    @Inject
    private JGroupsIntegrationPointNamingUtilities componentNameingUtilities;

    @Inject
    private ProcessingPlantPetasosParticipantHolder participantHolder;

    @Inject
    private ProcessingPlantPetasosParticipantNameHolder participantNameHolder;

    //
    // Default Constructor
    //

    public PegacornTopologyFactoryBase() {
        this.initialised = false;
    }

    //
    // Abstract Methods
    //

    abstract protected Logger specifyLogger();

    abstract protected String specifyPropertyFileName();

    abstract protected Class specifyPropertyFileClass();

    abstract protected ProcessingPlantSoftwareComponent buildSubsystemTopology();

    //
    // PostConstruct function
    //

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initialise(): Entry");
        if (!initialised) {
            getLogger().info(".initialise(): Initalising......");
            getLogger().info(".initialise(): [Solution Node Factory Initialisation]: Start");
            this.solutionNodeInterface.initialise();
            getLogger().info(".initialise(): [Solution Node Factory Initialisation]: Finish");
            getLogger().info(".initialise(): [Property File Loading]: Start");
            this.propertyFile = readPropertyFile();
            getLogger().info(".initialise(): [Property File Loading]: Finish");
            getLogger().info(".initialise(): [Subsystem Topology Building]: Start");
            this.processingPlantNode = buildSubsystemTopology();
            getLogger().info(".initialise(): [Subsystem Topology Building]: Finish");
            this.initialised = true;
            getLogger().info(".initialise(): Done.");
        } else {
            getLogger().info(".initialise(): Already initialised, nothing to do....");
        }
        getLogger().debug(".initialise(): Exit");
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (specifyLogger());
    }

    public BaseSubsystemPropertyFile getPropertyFile() {
        return (this.propertyFile);
    }

    public TopologyIM getTopologyIM() {
        return topologyIM;
    }

    protected SiteKeyNames getSiteKeyNames() {
        return (siteKeyNames);
    }

    public SolutionNodeFactoryInterface getSolutionNodeInterface() {
        return solutionNodeInterface;
    }

    protected PegacornCommonInterfaceNames getInterfaceNames() {
        return (interfaceNames);
    }

    //
    // Node Builders
    //

    @Override
    public TopologyNodeRDN createNodeRDN(String nodeName, String nodeVersion, PegacornSystemComponentTypeTypeEnum nodeType) {
        getLogger().debug(".createNodeRDN: Entry, nodeName->{}, nodeVersion->{}, nodeType->{}", nodeName, nodeVersion, nodeType);
        TopologyNodeRDN newRDN = createSimpleNodeRDN(nodeName, nodeVersion, nodeType);
        getLogger().debug(".createNodeRDN: Exit, newRDN->{}", newRDN);
        return (newRDN);
    }

    protected String getActualHostIP() {
        String actualHostIP = pegacornProperties.getProperty("MY_HOST_IP", "Unknown");
        return (actualHostIP);
    }

    protected String getActualPodIP() {
        String actualHostIP = pegacornProperties.getProperty("MY_POD_IP", "Unknown");
        return (actualHostIP);
    }

    /**
     * Subsystem Node Builder
     *
     * @param solution
     * @return
     */

    public SubsystemTopologyNode addSubsystemNode(SolutionTopologyNode solution) {
        getLogger().debug(".addSubsystemNode(): Entry, solution->{}", solution);
        SubsystemTopologyNode subsystem = new SubsystemTopologyNode();
        getLogger().trace(".addSubsystemNode(): Create the Subsystem RDN (createNodeRDN()) --> Start");
        TopologyNodeRDN nodeRDN = createNodeRDN(getPropertyFile().getSubsystemInstant().getSubsystemName(), getPropertyFile().getSubsystemInstant().getSubsystemVersion(), PegacornSystemComponentTypeTypeEnum.SUBSYSTEM);
        getLogger().trace(".addSubsystemNode(): Create the Subsystem RDN (createNodeRDN()) --> Finish, nodeRDN->{}", nodeRDN);
        getLogger().trace(".addSubsystemNode(): Create the Subsystem FDN (constructFDN()) --> Start");
        subsystem.constructFDN(solution.getComponentFDN(), nodeRDN);
        subsystem.getComponentID().setDisplayName(getPropertyFile().getSubsystemInstant().getParticipantName());
        getLogger().trace(".addSubsystemNode(): Create the Subsystem FDN (constructFDN()) --> Finish, nodeFDN->{}", subsystem.getComponentFDN());
        getLogger().trace(".addSubsystemNode(): Create the Subsystem Function FDN (constructFunctionFDN()) --> Start");
        subsystem.constructFunctionFDN(solution.getNodeFunctionFDN(), nodeRDN);
        getLogger().trace(".addSubsystemNode(): Create the Subsystem FDN (constructFunctionFDN()) --> Finish, nodeFunctionFDN->{}", subsystem.getNodeFunctionFDN());
        subsystem.setComponentRDN(nodeRDN);
        getLogger().trace(".addSubsystemNode(): Set the Subsystem Concurrency Mode");
        subsystem.setConcurrencyMode(getConcurrenceMode());
        getLogger().trace(".addSubsystemNode(): Set the Subsystem Resilience Mode");
        subsystem.setResilienceMode(getResilienceMode());
        getLogger().trace(".addSubsystemNode(): Set the Subsystem Site Count");
        subsystem.setSiteCount(getPropertyFile().getDeploymentSites().getSiteCount());
        getLogger().trace(".addSubsystemNode(): Set the Subsystem's Parent Solution");
        subsystem.setContainingNodeFDN(solution.getComponentFDN());
        getLogger().trace(".addSubsystemNode(): Add the subsystem to the Solution subsystem list");
        solution.getSubsystemList().add(subsystem.getComponentFDN());
        subsystem.setComponentType(PegacornSystemComponentTypeTypeEnum.SUBSYSTEM);
        getLogger().trace(".addSubsystemNode(): Add the subsystem to the Topology Cache");
        getTopologyIM().addTopologyNode(solution.getComponentFDN(), subsystem);
        getLogger().debug(".addSubsystemNode(): Exit");
        return (subsystem);
    }

    /**
     * Business Service Node Set Builder
     *
     * @param subsystem
     * @return
     */

    public BusinessServiceTopologyNode addBusinessServiceNode(SubsystemTopologyNode subsystem) {
        getLogger().debug(".addBusinessServiceNode(): Entry");
        BusinessServiceTopologyNode businessService = new BusinessServiceTopologyNode();
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service RDN (createNodeRDN()) --> Start");
        TopologyNodeRDN nodeRDN = createNodeRDN(getPropertyFile().getSubsystemInstant().getExternalisedServiceName(), getPropertyFile().getSubsystemInstant().getSubsystemVersion(), PegacornSystemComponentTypeTypeEnum.EXTERNALISED_SERVICE);
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service RDN (createNodeRDN()) --> Finish, nodeRDN->{}", nodeRDN);
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service FDN (constructFDN()) --> Start");
        businessService.constructFDN(subsystem.getComponentFDN(), nodeRDN);
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service FDN (constructFDN()) --> Finish, nodeFDN->{}", businessService.getComponentFDN());
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service Function FDN (constructFunctionFDN()) --> Start");
        businessService.constructFunctionFDN(subsystem.getNodeFunctionFDN(), nodeRDN);
        getLogger().trace(".addBusinessServiceNode(): Create the Business Service FDN (constructFunctionFDN()) --> Finish, nodeFunctionFDN->{}", businessService.getNodeFunctionFDN());
        businessService.setComponentRDN(nodeRDN);
        businessService.setComponentType(PegacornSystemComponentTypeTypeEnum.EXTERNALISED_SERVICE);
        businessService.setContainingNodeFDN(subsystem.getComponentFDN());
        subsystem.getBusinessServices().add(businessService.getComponentFDN());
        getLogger().trace(".addBusinessServiceNode(): Add the BusinessService to the Topology Cache");
        getTopologyIM().addTopologyNode(subsystem.getComponentFDN(), businessService);
        getLogger().debug(".addBusinessServiceNode(): Exit");
        return (businessService);
    }

    /**
     * Deployment Site Node Builder
     *
     * @param businessService
     * @return
     */
    public DeploymentSiteTopologyNode addDeploymentSiteNode(BusinessServiceTopologyNode businessService) {
        getLogger().debug(".addDeploymentSiteNode(): Entry, businessService->{}", businessService);
        DeploymentSiteTopologyNode site = new DeploymentSiteTopologyNode();
        String siteName = getPropertyFile().getSubsystemInstant().getSite();
        TopologyNodeFDN businessServiceFDN = businessService.getComponentFDN();
        TopologyNodeFunctionFDN businessServiceFunctionFDN = businessService.getNodeFunctionFDN();
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site RDN (createNodeRDN()) --> Start");
        TopologyNodeRDN nodeRDN = createNodeRDN(getPropertyFile().getDeploymentSites().getSite1Name(), getPropertyFile().getSubsystemInstant().getSubsystemVersion(), PegacornSystemComponentTypeTypeEnum.SITE);
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site RDN (createNodeRDN()) --> Finish, nodeRDN->{}", nodeRDN);
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site FDN (constructFDN()) --> Start, businessServiceFDN->{}, nodeRDN->{}", businessServiceFDN, nodeRDN);
        site.constructFDN(businessServiceFDN, nodeRDN);
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site FDN (constructFDN()) --> Finish, nodeFDN->{}", businessServiceFDN);
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site Function FDN (constructFunctionFDN()) --> Start, businessServiceFunctionFDN->{}, nodeRDN->{}", businessServiceFunctionFDN, nodeRDN);
        site.constructFunctionFDN(businessServiceFunctionFDN, nodeRDN);
        getLogger().trace(".addDeploymentSiteNode(): Create the Deployment Site FDN (constructFunctionFDN()) --> Finish, nodeFunctionFDN->{}", businessServiceFunctionFDN);
        site.setComponentRDN(nodeRDN);
        site.setComponentType(PegacornSystemComponentTypeTypeEnum.SITE);
        site.setInstanceCount(getPropertyFile().getDeploymentSites().getSiteCount());
        site.setContainingNodeFDN(businessService.getComponentFDN());
        businessService.getDeploymentSites().add(site.getComponentFDN());
        getLogger().trace(".addDeploymentSiteNode(): Add the DeploymentSite to the Topology Cache");
        getTopologyIM().addTopologyNode(businessService.getComponentFDN(), site);
        getLogger().debug(".addDeploymentSiteNode(): Exit");
        return (site);
    }

    /**
     * Build Cluster Service Node
     *
     * @param site
     * @return
     */
    public ClusterServiceTopologyNode addClusterServiceNode(DeploymentSiteTopologyNode site) {
        getLogger().debug(".addClusterServiceNode(): Entry");
        ClusterServiceTopologyNode clusterService = new ClusterServiceTopologyNode();
        TopologyNodeRDN nodeRDN = createNodeRDN(getPropertyFile().getSubsystemInstant().getClusterServiceName(), getPropertyFile().getSubsystemInstant().getProcessingPlantVersion(), PegacornSystemComponentTypeTypeEnum.CLUSTER_SERVICE);
        clusterService.constructFDN(site.getComponentFDN(), nodeRDN);
        clusterService.constructFunctionFDN(site.getNodeFunctionFDN(), nodeRDN);
        clusterService.setComponentRDN(nodeRDN);
        clusterService.setComponentType(PegacornSystemComponentTypeTypeEnum.CLUSTER_SERVICE);
        clusterService.setResilienceMode(getResilienceMode());
        clusterService.setConcurrencyMode(getConcurrenceMode());
        clusterService.setDefaultDNSName(getPropertyFile().getSubsystemInstant().getClusterServiceDNSName());
        clusterService.setInternalTrafficEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        clusterService.setContainingNodeFDN(site.getComponentFDN());
        site.getClusterServices().add(clusterService.getComponentFDN());
        getLogger().trace(".addClusterServiceNode(): Add the ClusterService to the Topology Cache");
        getTopologyIM().addTopologyNode(site.getComponentFDN(), clusterService);
        getLogger().debug(".addClusterServiceNode(): Exit");
        return (clusterService);
    }

    /**
     * Platform Node Set Builder
     *
     * @param clusterService
     * @return
     */
    public PlatformTopologyNode addPlatformNode(ClusterServiceTopologyNode clusterService) {
        getLogger().debug(".addPlatformNode(): Entry");
        PlatformTopologyNode node = new PlatformTopologyNode();
        String hostName = pegacornProperties.getProperty("MY_POD_NAME", "PlatformNode0");
//      TODO Fix This --> lookup POD Name or DNS Name
        TopologyNodeRDN nodeRDN = createNodeRDN(hostName, getPropertyFile().getSubsystemInstant().getProcessingPlantVersion(), PegacornSystemComponentTypeTypeEnum.PLATFORM);
        node.constructFDN(clusterService.getComponentFDN(), nodeRDN);
        node.setActualHostIP(getActualHostIP());
        node.setActualPodIP(getActualPodIP());
        node.setComponentType(PegacornSystemComponentTypeTypeEnum.PLATFORM);
        node.constructFunctionFDN(clusterService.getNodeFunctionFDN(), nodeRDN);
        node.setComponentRDN(nodeRDN);
        node.setContainingNodeFDN(clusterService.getComponentFDN());
        clusterService.getPlatformNodes().add(node.getComponentFDN());
        getLogger().trace(".addPlatformNode(): Add the PlatformNode to the Topology Cache");
        getTopologyIM().addTopologyNode(clusterService.getComponentFDN(), node);
        getLogger().debug(".addPlatformNode(): Exit");
        return (node);
    }

    /**
     * @param node
     * @return
     */
    public ProcessingPlantSoftwareComponent addPegacornProcessingPlant(PlatformTopologyNode node) {
        getLogger().debug(".addPegacornProcessingPlant(): Entry");
        ProcessingPlantSoftwareComponent processingPlant = new ProcessingPlantSoftwareComponent();
        String name = getPropertyFile().getSubsystemInstant().getParticipantName();
        String uniqueId = componentNameingUtilities.buildProcessingPlantName(name, componentNameingUtilities.getCurrentUUID());
        String version = getPropertyFile().getSubsystemInstant().getProcessingPlantVersion();
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueId, version, PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT);
        processingPlant.setComponentRDN(nodeRDN);
        processingPlant.constructFDN(node.getComponentFDN(), nodeRDN);
        processingPlant.setParticipantName(getPropertyFile().getSubsystemInstant().getParticipantName());
        processingPlant.setParticipantDisplayName(getPropertyFile().getSubsystemInstant().getParticipantName());
        processingPlant.setSubsystemParticipantName(getPropertyFile().getSubsystemInstant().getParticipantName());
        processingPlant.setActualHostIP(getActualHostIP());
        processingPlant.setActualPodIP(getActualPodIP());
        TopologyNodeRDN functionRDN = createNodeRDN(name, version, PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT);
        processingPlant.constructFunctionFDN(node.getNodeFunctionFDN(), functionRDN);
        processingPlant.setResilienceMode(getResilienceMode());
        processingPlant.setConcurrencyMode(getConcurrenceMode());
        processingPlant.setComponentType(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT);
        processingPlant.setNameSpace(getPropertyFile().getDeploymentZone().getNameSpace());
        processingPlant.setDeploymentSite(getPropertyFile().getSubsystemInstant().getSite());

        processingPlant.setReplicationCount(getPropertyFile().getDeploymentMode().getProcessingPlantReplicationCount());

        participantNameHolder.setSubsystemParticipantName(getPropertyFile().getSubsystemInstant().getParticipantName());

        //
        // Assign the Configuration File Names (InterZone)
        processingPlant.setPetasosIPCStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosIPCStackConfigFile());
        processingPlant.setPetasosTopologyStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosTopologyStackConfigFile());
        processingPlant.setMultiZoneInfinispanStackConfigFile(getPropertyFile().getDeploymentMode().getMultiuseInfinispanStackConfigFile());
        processingPlant.setPetasosTaskingStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosTaskingStackConfigFile());
        processingPlant.setPetasosAuditStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosAuditStackConfigFile());
        processingPlant.setPetasosInterceptionStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosInterceptionStackConfigFile());
        processingPlant.setPetasosMetricsStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosMetricsStackConfigFile());
        processingPlant.setPetasosSubscriptionsStackConfigFile(getPropertyFile().getDeploymentMode().getPetasosSubscriptionStackConfigFile());

        processingPlant.setAssignedDNSName(getPropertyFile().getSubsystemInstant().getProcessingPlantDNSName());
        processingPlant.setInternalTrafficEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        processingPlant.setReplicationCount(getPropertyFile().getDeploymentMode().getProcessingPlantReplicationCount());
        processingPlant.setContainingNodeFDN(node.getComponentFDN());
        NetworkSecurityZoneEnum zone = NetworkSecurityZoneEnum.fromDisplayName(getPropertyFile().getDeploymentZone().getSecurityZoneName());
        if (zone == null) {
            getLogger().error(".addPegacornProcessingPlant(): Cannot resolve Network Security Zone for component, provided->{}", getPropertyFile().getDeploymentZone().getSecurityZoneName());
            StringBuilder possibleValues = new StringBuilder();
            for (NetworkSecurityZoneEnum currentValue : NetworkSecurityZoneEnum.values()) {
                possibleValues.append(currentValue.getDisplayName() + " ");
            }
            getLogger().error(".addPegacornProcessingPlant(): Possible Values Are->{}", possibleValues.toString());
        }
        processingPlant.setSecurityZone(zone);
        setProcessingPlantNode(processingPlant);
        node.getProcessingPlants().add(processingPlant.getComponentFDN());
        populateOtherDeploymentProperties(processingPlant, getPropertyFile().getDeploymentMode().getOtherDeploymentParameters());
        getLogger().trace(".addPegacornProcessingPlant(): Add the ProcessingPlant to the Topology Cache");

        getTopologyIM().addTopologyNode(node.getComponentFDN(), processingPlant);

        //
        // Create Petasos Participant for Processing Plant
        PetasosParticipant processingPlantPetasosParticipant = new PetasosParticipant(processingPlant);
        processingPlantPetasosParticipant.setParticipantStatus(PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING);
        participantHolder.setMyProcessingPlantPetasosParticipant(processingPlantPetasosParticipant);
        //
        // All done
        getLogger().debug(".addPegacornProcessingPlant(): Exit");
        return (processingPlant);
    }

    /**
     * @param name
     * @param version
     * @param processingPlant
     */
    @Override
    public WorkshopSoftwareComponent createWorkshop(String name, String version, ProcessingPlantSoftwareComponent processingPlant, PegacornSystemComponentTypeTypeEnum nodeType) {
        getLogger().debug(".addWorkshop(): Entry");
        WorkshopSoftwareComponent workshop = new WorkshopSoftwareComponent();
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, version, nodeType);
        workshop.setComponentRDN(nodeRDN);
        String participantName = getProcessingPlantNode().getParticipantName() + "." + name;
        workshop.setParticipantName(participantName);
        workshop.setParticipantDisplayName(name);
        workshop.constructFDN(processingPlant.getComponentFDN(), nodeRDN);
        TopologyNodeRDN functionRDN = createNodeRDN(name, version, nodeType);
        workshop.constructFunctionFDN(processingPlant.getNodeFunctionFDN(), functionRDN);
        workshop.setComponentType(nodeType);
        workshop.setResilienceMode(getResilienceMode());
        workshop.setConcurrencyMode(getConcurrenceMode());
        workshop.setContainingNodeFDN(processingPlant.getComponentFDN());
        workshop.setSecurityZone(processingPlant.getSecurityZone());
        processingPlant.getWorkshops().add(workshop.getComponentFDN());
        getLogger().trace(".addWorkshop(): Add the Workshop to the Topology Cache");
        getTopologyIM().addTopologyNode(processingPlant.getComponentFDN(), workshop);
        getLogger().debug(".addWorkshop(): Exit");
        return (workshop);
    }

    /**
     * @param name
     * @param version
     * @param workshop
     */
    @Override
    public WorkUnitProcessorSoftwareComponent createWorkUnitProcessor(String name, String version, String participantName, WorkshopSoftwareComponent workshop, PegacornSystemComponentTypeTypeEnum nodeType) {
        getLogger().debug(".addWorkUnitProcessor(): Entry, name->{}, version->{}", name, version);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(version)) {
            getLogger().error(".createWorkUnitProcessor(): name or version are emtpy!!!!");
        }
        WorkUnitProcessorSoftwareComponent wup = new WorkUnitProcessorSoftwareComponent();
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, version, nodeType);
        wup.setComponentRDN(nodeRDN);
        wup.constructFDN(workshop.getComponentFDN(), nodeRDN);
        TopologyNodeRDN functionRDN = createNodeRDN(name, version, nodeType);
        wup.constructFunctionFDN(workshop.getNodeFunctionFDN(), functionRDN);
        wup.setContainingNodeFDN(workshop.getComponentFDN());
        wup.setComponentType(nodeType);
        wup.setConcurrencyMode(getConcurrenceMode());
        wup.setResilienceMode(getResilienceMode());
        wup.setSecurityZone(workshop.getSecurityZone());
        wup.setSubsystemParticipantName(getPropertyFile().getSubsystemInstant().getParticipantName());
        wup.setParticipantName(participantName);
        wup.setParticipantDisplayName(name);
        wup.setReplicationCount(getPropertyFile().getDeploymentMode().getProcessingPlantReplicationCount());
        workshop.getWupSet().add(wup.getComponentFDN());
        getLogger().trace(".addWorkUnitProcessor(): Add the WorkUnitProcessor to the Topology Cache");
        getTopologyIM().addTopologyNode(workshop.getComponentFDN(), wup);
        getLogger().debug(".addWorkUnitProcessor(): Exit");
        return (wup);
    }

    /**
     * @param name
     * @param topologyType
     * @param wup
     */
    @Override
    public WorkUnitProcessorSubComponentSoftwareComponent createWorkUnitProcessorComponent(String name, PegacornSystemComponentTypeTypeEnum topologyType, WorkUnitProcessorSoftwareComponent wup) {
        getLogger().debug(".addWorkUnitProcessorComponent(): Entry");
        WorkUnitProcessorSubComponentSoftwareComponent wupComponent = new WorkUnitProcessorSubComponentSoftwareComponent();
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, wup.getComponentRDN().getNodeVersion(), topologyType);
        wupComponent.constructFDN(wup.getComponentFDN(), nodeRDN);
        TopologyNodeRDN functionRDN = createNodeRDN(name, wup.getComponentRDN().getNodeVersion(), topologyType);
        wupComponent.constructFunctionFDN(wup.getNodeFunctionFDN(), functionRDN);
        wupComponent.setComponentRDN(nodeRDN);
        wupComponent.setComponentType(topologyType);
        wupComponent.setContainingNodeFDN(wup.getComponentFDN());
        wupComponent.setConcurrencyMode(getConcurrenceMode());
        wupComponent.setResilienceMode(getResilienceMode());
        wupComponent.setSecurityZone(wup.getSecurityZone());
        wup.getWupComponents().add(wupComponent.getComponentFDN());
        getLogger().trace(".addWorkUnitProcessorComponent(): Add the WorkUnitProcessor Component to the Topology Cache");
        getTopologyIM().addTopologyNode(wup.getComponentFDN(), wupComponent);
        getLogger().debug(".addWorkUnitProcessorComponent(): Exit");
        return (wupComponent);
    }

    /**
     * @param name
     * @param topologyNodeType
     * @param wup
     */
    @Override
    public WorkUnitProcessorInterchangeSoftwareComponent createWorkUnitProcessingInterchangeComponent(String name, PegacornSystemComponentTypeTypeEnum topologyNodeType, WorkUnitProcessorSoftwareComponent wup) {
        getLogger().debug(".addWorkUnitProcessingInterchangeComponent(): Entry");
        WorkUnitProcessorInterchangeSoftwareComponent wupInterchangeComponent = new WorkUnitProcessorInterchangeSoftwareComponent();
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, wup.getComponentRDN().getNodeVersion(), topologyNodeType);
        wupInterchangeComponent.constructFDN(wup.getComponentFDN(), nodeRDN);
        TopologyNodeRDN functionRDN = createNodeRDN(name, wup.getComponentRDN().getNodeVersion(), topologyNodeType);
        wupInterchangeComponent.constructFunctionFDN(wup.getNodeFunctionFDN(), functionRDN);
        wupInterchangeComponent.setComponentRDN(nodeRDN);
        wupInterchangeComponent.setComponentType(topologyNodeType);
        wupInterchangeComponent.setContainingNodeFDN(wup.getComponentFDN());
        wupInterchangeComponent.setConcurrencyMode(getConcurrenceMode());
        wupInterchangeComponent.setResilienceMode(getResilienceMode());
        wupInterchangeComponent.setSecurityZone(wup.getSecurityZone());
        wup.getWupInterchangeComponents().add(wupInterchangeComponent.getComponentFDN());
        getLogger().trace(".addWorkUnitProcessorComponent(): Add the WorkUnitProcessor Interchange Component to the Topology Cache");
        getTopologyIM().addTopologyNode(wup.getComponentFDN(), wupInterchangeComponent);
        getLogger().debug(".addWorkUnitProcessingInterchangeComponent(): Exit");
        return (wupInterchangeComponent);
    }


    /**
     * @return
     */

    @Override
    public Boolean getSubsystemInternalTrafficEncrypt() {
        getLogger().debug(".getSubsystemInternalTrafficEncrypt(): Entry");
        Boolean encryptTraffic = getPropertyFile().getDeploymentMode().isUsingInternalEncryption();
        getLogger().debug(".getSubsystemInternalTrafficEncrypt(): Exit, Encrypt Internal Traffic? --> {}", encryptTraffic);
        return (encryptTraffic);
    }


    //
    // Build Prometheus Port (if there)
    //

    protected void addPrometheusPort(ProcessingPlantSoftwareComponent processingPlant) {
        getLogger().debug(".addPrometheusPort(): Entry, processingPlant->{}", processingPlant);
        HTTPServerTopologyEndpoint prometheusPort = new HTTPServerTopologyEndpoint();
        HTTPServerPortSegment port = getPropertyFile().getPrometheusPort();
        if (port == null) {
            getLogger().debug(".addPrometheusPort(): Exit, no port to add");
            return;
        }

        String name = interfaceNames.getEndpointServerName(interfaceNames.getPrometheusEndpointName());
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        String participantName = processingPlant.getParticipantName() + interfaceNames.getPrometheusEndpointName();
        prometheusPort.setParticipantName(participantName);
        prometheusPort.setParticipantDisplayName(participantName);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        prometheusPort.setComponentRDN(nodeRDN);
        prometheusPort.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER);
        prometheusPort.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        prometheusPort.constructFDN(processingPlant.getComponentFDN(), nodeRDN);
        TopologyNodeRDN functionRDN = createNodeRDN(name, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        prometheusPort.constructFunctionFDN(processingPlant.getNodeFunctionFDN(), functionRDN);
        prometheusPort.setEndpointConfigurationName(interfaceNames.getPrometheusEndpointName());
        prometheusPort.setContainingNodeFDN(processingPlant.getComponentFDN());
        prometheusPort.setServer(true);

        HTTPServerAdapter httpServerAdapter = new HTTPServerAdapter();
        httpServerAdapter.setEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        httpServerAdapter.setPortNumber(port.getServerPort());
        httpServerAdapter.setContextPath(port.getContextPath());
        httpServerAdapter.setHostName(port.getServerHostname());
        prometheusPort.setHTTPServerAdapter(httpServerAdapter);

        processingPlant.getEndpoints().add(prometheusPort.getComponentFDN());
        getLogger().trace(".addPrometheusPort(): Add the Prometheus Port to the Topology Cache");
        getTopologyIM().addTopologyNode(processingPlant.getComponentFDN(), prometheusPort);
        getLogger().debug(".addPrometheusPort(): Exit, endpoint added");
    }

    //
    // Build Jolokia Port (if there)
    //

    protected void addJolokiaPort(ProcessingPlantSoftwareComponent processingPlant) {
        getLogger().debug(".addJolokiaPort(): Entry, processingPlant->{}", processingPlant);
        HTTPServerTopologyEndpoint jolokiaPort = new HTTPServerTopologyEndpoint();
        HTTPServerPortSegment port = getPropertyFile().getJolokiaPort();
        if (port == null) {
            getLogger().debug(".addJolokiaPort(): Exit, no port to add");
            return;
        }
        String participantName = processingPlant.getParticipantName() + interfaceNames.getJolokiaEndpointName();
        jolokiaPort.setParticipantName(participantName);
        jolokiaPort.setParticipantDisplayName(participantName);
        String name = interfaceNames.getEndpointServerName(interfaceNames.getJolokiaEndpointName());
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jolokiaPort.setComponentRDN(nodeRDN);
        jolokiaPort.setEndpointConfigurationName(interfaceNames.getJolokiaEndpointName());
        jolokiaPort.constructFDN(processingPlant.getComponentFDN(), nodeRDN);
        jolokiaPort.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER);
        jolokiaPort.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        TopologyNodeRDN functionRDN = createNodeRDN(name, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jolokiaPort.constructFunctionFDN(processingPlant.getNodeFunctionFDN(), functionRDN);
        jolokiaPort.setServer(true);
        jolokiaPort.setContainingNodeFDN(processingPlant.getComponentFDN());

        HTTPServerAdapter httpServerAdapter = new HTTPServerAdapter();
        httpServerAdapter.setEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        httpServerAdapter.setPortNumber(port.getServerPort());
        httpServerAdapter.setContextPath(port.getContextPath());
        httpServerAdapter.setHostName(port.getServerHostname());
        jolokiaPort.setHTTPServerAdapter(httpServerAdapter);

        processingPlant.getEndpoints().add(jolokiaPort.getComponentFDN());
        getLogger().trace(".addJolokiaPort(): Add the Jolokia Port to the Topology Cache");
        getTopologyIM().addTopologyNode(processingPlant.getComponentFDN(), jolokiaPort);
        getLogger().debug(".addJolokiaPort(): Exit, endpoint added");
    }

    //
    // Build KubeLiveliness Port (if there)
    //

    protected void addKubeLivelinessPort(ProcessingPlantSoftwareComponent processingPlant) {
        getLogger().debug(".addKubeLivelinessPort(): Entry, processingPlant->{}", processingPlant);
        HTTPServerTopologyEndpoint kubeLivelinessPort = new HTTPServerTopologyEndpoint();
        HTTPServerPortSegment port = getPropertyFile().getKubeLivelinessProbe();
        if (port == null) {
            getLogger().debug(".addKubeLivelinessPort(): Exit, no port to add");
            return;
        }
        String participantName = processingPlant.getParticipantName() + interfaceNames.getKubeLivelinessEndpointName();
        kubeLivelinessPort.setParticipantName(participantName);
        kubeLivelinessPort.setParticipantDisplayName(participantName);
        String name = interfaceNames.getEndpointServerName(interfaceNames.getKubeLivelinessEndpointName());
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        kubeLivelinessPort.setComponentRDN(nodeRDN);
        kubeLivelinessPort.setEndpointConfigurationName(interfaceNames.getKubeLivelinessEndpointName());
        kubeLivelinessPort.constructFDN(processingPlant.getComponentFDN(), nodeRDN);
        kubeLivelinessPort.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER);
        kubeLivelinessPort.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        TopologyNodeRDN functionRDN = createNodeRDN(name, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        kubeLivelinessPort.constructFunctionFDN(processingPlant.getNodeFunctionFDN(), functionRDN);
        kubeLivelinessPort.setServer(true);
        kubeLivelinessPort.setContainingNodeFDN(processingPlant.getComponentFDN());

        HTTPServerAdapter httpServerAdapter = new HTTPServerAdapter();
        httpServerAdapter.setEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        httpServerAdapter.setPortNumber(port.getServerPort());
        httpServerAdapter.setContextPath(port.getContextPath());
        httpServerAdapter.setHostName(port.getServerHostname());
        kubeLivelinessPort.setHTTPServerAdapter(httpServerAdapter);

        processingPlant.getEndpoints().add(kubeLivelinessPort.getComponentFDN());
        getLogger().trace(".addKubeLivelinessPort(): Add the KubeLivelinessPort Port to the Topology Cache");
        getTopologyIM().addTopologyNode(processingPlant.getComponentFDN(), kubeLivelinessPort);
        getLogger().debug(".addKubeLivelinessPort(): Exit, endpoint added");
    }

    //
    // Build KubeReadiness Port (if there)
    //

    protected void addKubeReadinessPort(ProcessingPlantSoftwareComponent processingPlant) {
        getLogger().debug(".addKubeReadinessPort(): Entry, processingPlant->{}", processingPlant);
        HTTPServerTopologyEndpoint kubeReadinessPort = new HTTPServerTopologyEndpoint();
        HTTPServerPortSegment port = getPropertyFile().getKubeReadinessProbe();
        if (port == null) {
            getLogger().debug(".addKubeReadinessPort(): Exit, no port to add");
            return;
        }
        String participantName = processingPlant.getParticipantName() + interfaceNames.getKubeReadinessEndpointName();
        kubeReadinessPort.setParticipantName(participantName);
        kubeReadinessPort.setParticipantDisplayName(participantName);
        String name = interfaceNames.getEndpointServerName(interfaceNames.getKubeReadinessEndpointName());
        String uniqueName = componentNameingUtilities.buildUniqueComponentName(name);
        TopologyNodeRDN nodeRDN = createNodeRDN(uniqueName, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        kubeReadinessPort.setComponentRDN(nodeRDN);
        kubeReadinessPort.setEndpointConfigurationName(interfaceNames.getKubeReadinessEndpointName());
        kubeReadinessPort.constructFDN(processingPlant.getComponentFDN(), nodeRDN);
        kubeReadinessPort.setEndpointType(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER);
        kubeReadinessPort.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        TopologyNodeRDN functionRDN = createNodeRDN(name, processingPlant.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        kubeReadinessPort.constructFunctionFDN(processingPlant.getNodeFunctionFDN(), functionRDN);
        kubeReadinessPort.setComponentRDN(nodeRDN);
        kubeReadinessPort.setContainingNodeFDN(processingPlant.getComponentFDN());

        HTTPServerAdapter httpServerAdapter = new HTTPServerAdapter();
        httpServerAdapter.setEncrypted(getPropertyFile().getDeploymentMode().isUsingInternalEncryption());
        httpServerAdapter.setPortNumber(port.getServerPort());
        httpServerAdapter.setContextPath(port.getContextPath());
        httpServerAdapter.setHostName(port.getServerHostname());
        kubeReadinessPort.setHTTPServerAdapter(httpServerAdapter);

        processingPlant.getEndpoints().add(kubeReadinessPort.getComponentFDN());
        getLogger().trace(".addKubeReadinessPort(): Add the KubeReadinessPort Port to the Topology Cache");
        getTopologyIM().addTopologyNode(processingPlant.getComponentFDN(), kubeReadinessPort);
        getLogger().debug(".addKubeReadinessPort(): Exit, endpoint added");
    }

    //
    // Resilience Mode Calculation
    //
    public ResilienceModeEnum getResilienceMode() {
        getLogger().debug(".getResilienceMode(): Entry");
        boolean clustered = false;
        boolean kubernetes = false;
        boolean multisite = false;

        if (getPropertyFile().getDeploymentSites().getSiteCount() < 2) {
            multisite = false;
        }

        if (getPropertyFile().getDeploymentMode().getProcessingPlantReplicationCount() < 2) {
            clustered = true;
        }

        if (getPropertyFile().getDeploymentMode().isKubernetes()) {
            kubernetes = true;
        }

        if (!clustered && !kubernetes && !multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
            return (ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
        }
        if (!clustered && kubernetes && !multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
            return (ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
        }
        if (clustered && !kubernetes && !multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
            return (ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
        }
        if (clustered && kubernetes && !multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
            return (ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
        }
        if (!clustered && !kubernetes && multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
            return (ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
        }
        if (clustered && !kubernetes && multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
            return (ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
        }
        if (!clustered && kubernetes && multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
            return (ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
        }
        if (clustered && kubernetes && multisite) {
            getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
            return (ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
        }
        getLogger().debug(".getResilienceMode(): Exit, returning->{}", ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
        return (ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
    }

    //
    // Concurrency Mode Calculation
    //

    public ConcurrencyModeEnum getConcurrenceMode() {
        getLogger().debug(".getConcurrenceMode(): Entry");
        boolean standalone = false;
        boolean concurrent = false;

        if (getResilienceMode().equals(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE)) {
            standalone = true;
        }
        if (getPropertyFile().getDeploymentMode().isConcurrent()) {
            concurrent = true;
        }
        if (!standalone && !concurrent) {
            getLogger().debug(".getConcurrenceMode(): Exit, returning->{}", ConcurrencyModeEnum.CONCURRENCY_MODE_ONDEMAND);
            return (ConcurrencyModeEnum.CONCURRENCY_MODE_ONDEMAND);
        }
        if (!standalone && concurrent) {
            getLogger().debug(".getConcurrenceMode(): Exit, returning->{}", ConcurrencyModeEnum.CONCURRENCY_MODE_CONCURRENT);
            return (ConcurrencyModeEnum.CONCURRENCY_MODE_CONCURRENT);
        }
        getLogger().debug(".getConcurrenceMode(): Exit, returning->{}", ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
        return (ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
    }


    protected BaseSubsystemPropertyFile readPropertyFile() {
        String propertyFileName = specifyPropertyFileName();
        Class propertyFileClass = specifyPropertyFileClass();
        getLogger().warn(".readPropertyFile(): Entry, propertyFileName->{}", propertyFileName);
        try {
            getLogger().trace(".readPropertyFile(): Establish YAML ObjectMapper");
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            yamlMapper.findAndRegisterModules();
            yamlMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
            getLogger().warn(".readPropertyFile(): [Openning Configuration File] Start");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//            InputStream propertyYAMLFile = classLoader.getResourceAsStream("./"+propertyFileName);
            Path path = Paths.get(propertyFileName);
            File file = path.toFile();
            getLogger().warn(".readPropertyFile(): [Openning Configuration File] End");
            getLogger().warn(".readPropertyFile(): [Importing Configuration File] Start");
            this.propertyFile = (BaseSubsystemPropertyFile) yamlMapper.readValue(file, propertyFileClass);
            getLogger().warn(".readPropertyFile(): [Read YAML Configuration File] Finish");
            getLogger().debug(".loadPropertyFile(): Exit, file loaded, propertyFile->{}", this.propertyFile);
            return (this.propertyFile);
        } catch (FileNotFoundException noFile) {
            getLogger().error(".loadPropertyFile(): Configuration File->{} is not found, error->{}", propertyFileName, noFile.getMessage());
        } catch (IOException ioError) {
            getLogger().error(".loadPropertyFile(): Configuration File->{} could not be loaded, error->{}", propertyFileName, ioError.getMessage());
        }
        getLogger().debug(".loadPropertyFile(): failed to load file");
        return (null);
    }

    public ProcessingPlantSoftwareComponent getProcessingPlantNode() {
        return processingPlantNode;
    }

    public void setProcessingPlantNode(ProcessingPlantSoftwareComponent processingPlantNode) {
        this.processingPlantNode = processingPlantNode;
    }

    public void populateOtherDeploymentProperties(ProcessingPlantSoftwareComponent node, List<ParameterNameValuePairType> otherDeploymentPropertiesMap) {
        ConcurrentHashMap<String, String> propertiesMap = new ConcurrentHashMap<>();
        if (otherDeploymentPropertiesMap.isEmpty()) {
            node.setOtherConfigurationParameters(propertiesMap);
            return;
        }
        for (ParameterNameValuePairType currentNameValuePair : otherDeploymentPropertiesMap) {
            propertiesMap.put(currentNameValuePair.getParameterName(), currentNameValuePair.getParameterValue());
        }
        node.setOtherConfigurationParameters(propertiesMap);
    }


}
