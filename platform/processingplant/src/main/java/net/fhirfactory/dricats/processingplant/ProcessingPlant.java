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
package net.fhirfactory.dricats.processingplant;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.interfaces.auditing.PetasosAuditEventGranularityLevelInterface;
import net.fhirfactory.dricats.core.interfaces.capabilities.CapabilityFulfillmentInterface;
import net.fhirfactory.dricats.core.interfaces.pathway.TaskPathwayManagementServiceInterface;
import net.fhirfactory.dricats.core.interfaces.topology.PegacornTopologyFactoryInterface;
import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantRoleSupportInterface;
import net.fhirfactory.dricats.core.model.audit.valuesets.PetasosAuditEventGranularityLevelEnum;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationRequest;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationResponse;
import net.fhirfactory.dricats.core.model.component.SoftwareComponent;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFunctionFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.*;
import net.fhirfactory.dricats.core.model.endpoint.JGroupsIntegrationPointNamingUtilities;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantRegistration;
import net.fhirfactory.dricats.core.model.participant.ProcessingPlantPetasosParticipantHolder;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.core.model.topology.mode.NetworkSecurityZoneEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.ProcessingPlantSoftwareComponent;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkshopSoftwareComponent;
import net.fhirfactory.dricats.core.oam.metrics.PetasosMetricAgentFactory;
import net.fhirfactory.dricats.core.oam.metrics.collectors.ProcessingPlantMetricsAgent;
import net.fhirfactory.dricats.core.oam.metrics.collectors.ProcessingPlantMetricsAgentAccessor;
import net.fhirfactory.dricats.core.participants.cache.DistributedTaskSubscriptionMap;
import net.fhirfactory.dricats.core.participants.manager.LocalParticipantManager;
import net.fhirfactory.dricats.core.tasks.management.local.router.daemons.GlobalPetasosTaskContinuityWatchdog;
import net.fhirfactory.dricats.core.tasks.management.local.router.daemons.GlobalPetasosTaskRecoveryWatchdog;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.ClusterServiceDeliverySubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.datatypes.ParameterNameValuePairType;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.interfaces.SolutionNodeFactoryInterface;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.internals.fhir.r4.internal.topics.FHIRElementTopicFactory;
import net.fhirfactory.dricats.util.PegacornEnvironmentProperties;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ProcessingPlant extends RouteBuilder implements ProcessingPlantRoleSupportInterface, ProcessingPlantInterface, PetasosAuditEventGranularityLevelInterface, CapabilityFulfillmentInterface {

    ConcurrentHashMap<String, CapabilityFulfillmentInterface> capabilityDeliveryServices;
    private ProcessingPlantSoftwareComponent meAsASoftwareComponent;
    private String instanceQualifier;
    private boolean isInitialised;
    private ProcessingPlantMetricsAgent metricsAgent;
    private PetasosAuditEventGranularityLevelEnum processingPlantAuditLevel;
    @Inject
    private TopologyIM topologyIM;

    @Inject
    private PegacornEnvironmentProperties environmentProperties;

    @Inject
    private GlobalPetasosTaskContinuityWatchdog petasosTaskContinuityWatchdog;

    @Inject
    private GlobalPetasosTaskRecoveryWatchdog petasosTaskRecoveryWatchdog;

    @Inject
    private JGroupsIntegrationPointNamingUtilities componentNameUtilities;

    @Inject
    private ProcessingPlantPetasosParticipantHolder participantHolder;

    @Inject
    private FHIRElementTopicFactory fhirElementTopicFactory;

    @Inject
    private TaskPathwayManagementServiceInterface taskPathwayManagementService;

    @Inject
    private LocalParticipantManager localParticipantManager;

    @Inject
    private PegacornReferenceProperties pegacornReferenceProperties;

    @Inject
    private ProcessingPlantMetricsAgentAccessor metricsAgentAccessor;

    @Inject
    private PetasosMetricAgentFactory metricAgentFactory;

    @Inject
    private DistributedTaskSubscriptionMap distributedTaskSubscriptionMap;

    //
    // Constructor(s)
    //

    public ProcessingPlant() {
        super();
        this.capabilityDeliveryServices = new ConcurrentHashMap<>();
        this.isInitialised = false;
        this.instanceQualifier = UUID.randomUUID().toString();
        this.processingPlantAuditLevel = PetasosAuditEventGranularityLevelEnum.AUDIT_LEVEL_COARSE;
    }

    //
    // Abstract Methods
    //

    abstract protected ClusterServiceDeliverySubsystemPropertyFile specifyPropertyFile();

    abstract protected Logger specifyLogger();

    abstract protected PegacornTopologyFactoryInterface specifyTopologyFactory();

    abstract protected SolutionNodeFactoryInterface specifySolutionNodeFactory();

    abstract protected void executePostConstructActivities();

    //
    // Getters (and Setters)
    //

    protected PegacornReferenceProperties getPegacornReferenceProperties() {
        return (pegacornReferenceProperties);
    }

    protected Logger getLogger() {
        return (specifyLogger());
    }

    protected ClusterServiceDeliverySubsystemPropertyFile getPropertyFile() {
        return (specifyPropertyFile());
    }

    protected TaskPathwayManagementServiceInterface getTaskPathwayManagementService() {
        return (taskPathwayManagementService);
    }

    protected LocalParticipantManager getLocalPetasosParticipantCacheIM() {
        return (localParticipantManager);
    }

    protected FHIRElementTopicFactory getFHIRElementTopicFactory() {
        return (fhirElementTopicFactory);
    }

    public ProcessingPlantPetasosParticipantHolder getParticipantHolder() {
        return (participantHolder);
    }

    @Override
    public PegacornTopologyFactoryInterface getTopologyFactory() {
        return (specifyTopologyFactory());
    }

    @Override
    public String getHostName() {
        return (getMeAsASoftwareComponent().getAssignedDNSName());
    }

    public void setHostName(String hostName) {
        getMeAsASoftwareComponent().setAssignedDNSName(hostName);
    }

    public TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    public TopologyNodeFunctionFDN getNodeToken() {
        return (this.meAsASoftwareComponent.getNodeFunctionFDN());
    }

    public PetasosAuditEventGranularityLevelEnum getProcessingPlantAuditLevel() {
        return processingPlantAuditLevel;
    }

    protected ProcessingPlantMetricsAgent getMetricsAgent() {
        return (metricsAgent);
    }

    protected DistributedTaskSubscriptionMap getDistributedTaskSubscriptionMap() {
        return (this.distributedTaskSubscriptionMap);
    }

    //
    // Post Construct
    //

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initalise(): Entry");
        if (!isInitialised) {
            getLogger().info("ProcessingPlant::initialise(): Initialising....");
            getLogger().info("ProcessingPlant::initialise(): [TopologyIM Initialisation] Start");
            getTopologyIM().initialise();
            getLogger().info("ProcessingPlant::initialise(): [TopologyIM Initialisation] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Topology Factory Initialisation] Start");
            getTopologyFactory().initialise();
            getLogger().info("ProcessingPlant::initialise(): [Topology Factory Initialisation] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Solution Node Factory Initialisation] Start");
            specifySolutionNodeFactory().initialise();
            getLogger().info("ProcessingPlant::initialise(): [Solution Node Factory Initialisation] Finish");
            getLogger().info("ProcessingPlant::initialise(): [ProcessingPlant Resolution] Start");
            resolveProcessingPlant();
            getLogger().info("ProcessingPlant::initialise(): [ProcessingPlant Resolution] softwareComponet->{}", meAsASoftwareComponent);
            getLogger().info("ProcessingPlant::initialise(): [ProcessingPlant Resolution] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Capability Delivery Services Map Initialisation] Start");
            getLogger().info("ProcessingPlant::initialise(): [POD Name Resolution and Assignment] Start");
            String myPodName = environmentProperties.getMandatoryProperty("MY_POD_NAME");
            setHostName(myPodName);
            getLogger().info("ProcessingPlant::initialise(): [POD Name Resolution and Assignment] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Executing other PostConstruct Activities] Start");
            executePostConstructActivities();
            getLogger().info("ProcessingPlant::initialise(): [Executing other PostConstruct Activities] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Check for My PetasosPartcipant] Start");
            if (!getParticipantHolder().hasMyProcessingPlantPetasosParticipant()) {
                getLogger().info("ProcessingPlant::initialise(): [Check for My PetasosPartcipant] Registering my PetasosParticipant!");
                PetasosParticipantRegistration petasosParticipantRegistration = localParticipantManager.registerPetasosParticipant(getMeAsASoftwareComponent(), new HashSet<>(), new HashSet<>());
                getParticipantHolder().setMyProcessingPlantPetasosParticipant(petasosParticipantRegistration.getParticipant());
            }
            if (localParticipantManager.getLocalParticipantRegistration(getParticipantHolder().getMyProcessingPlantPetasosParticipant().getParticipantName()) == null) {
                PetasosParticipantRegistration petasosParticipantRegistration = localParticipantManager.registerPetasosParticipant(getMeAsASoftwareComponent(), new HashSet<>(), new HashSet<>());
                if (petasosParticipantRegistration == null) {
                    getLogger().error("ProcessingPlant::initialise(): [Check for My PetasosPartcipant] Cannot add my participant to local participant cache!");
                }
            }
            getLogger().info("ProcessingPlant::initialise(): [Check for My PetasosPartcipant] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Audit Event Level Derivation] Start");
            this.processingPlantAuditLevel = deriveAuditEventGranularityLevel();
            getLogger().info("ProcessingPlant::initialise(): [Audit Event Level Derivation] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Initialise Task Continuity Watchdog] Start");
            this.petasosTaskContinuityWatchdog.initialise();
            getLogger().info("ProcessingPlant::initialise(): [Initialise Task Continuity Watchdog] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Initialise Task Recovery Watchdog] Start");
            this.petasosTaskRecoveryWatchdog.initialise();
            getLogger().info("ProcessingPlant::initialise(): [Initialise Task Recovery Watchdog] Finish");
            getLogger().info("ProcessingPlant::initialise(): [Initialise Metrics Agent] Start");
            this.metricsAgent = metricAgentFactory.newProcessingPlantMetricsAgent(this, getMeAsASoftwareComponent().getComponentID(), getSubsystemParticipantName());
            metricsAgentAccessor.setMetricsAgent(this.metricsAgent);
            getLogger().info("ProcessingPlant::initialise(): [Initialise Metrics Agent] Finish");
            isInitialised = true;
            getLogger().info("StandardProcessingPlatform::initialise(): Done...");
        } else {
            getLogger().debug(".initialise(): Already initialised, nothing to do!");
        }
        getLogger().debug(".initialise(): Exit");
    }

    //
    // Overridden Business Methods
    //

    @Override
    public void initialisePlant() {
        initialise();
    }

    //
    // Topology Detail
    //

    private void resolveProcessingPlant() {
        getLogger().debug(".resolveProcessingPlant(): Entry");
        String processingPlantName = getPropertyFile().getSubsystemInstant().getParticipantName();
        String processingPlantVersion = getPropertyFile().getSubsystemInstant().getProcessingPlantVersion();
        getLogger().debug(".resolveProcessingPlant(): Getting ProcessingPlant->{}, version->{}", processingPlantName, processingPlantVersion);
        getLogger().trace(".resolveProcessingPlant(): Resolving list of available ProcessingPlants");
        String rdnNameValue = componentNameUtilities.buildProcessingPlantName(processingPlantName, componentNameUtilities.getCurrentUUID());
        List<SoftwareComponent> topologyNodes = topologyIM.nodeSearch(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT, rdnNameValue, processingPlantVersion);
        if (getLogger().isTraceEnabled()) {
            if (topologyNodes == null) {
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return a null list");
            }
            if (topologyNodes.isEmpty()) {
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return an empty list");
            }
            if (topologyNodes.size() > 1) {
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return a list containing more than 1 entry!");
            }
        }
        getLogger().trace(".resolveProcessingPlant(): Matching to my Name/Version");
        if (topologyNodes.isEmpty() || topologyNodes.size() > 1) {
            throw new RuntimeException("Unable to resolve ProcessingPlant");
        }
        this.meAsASoftwareComponent = (ProcessingPlantSoftwareComponent) topologyNodes.get(0);
        getLogger().debug(".resolveProcessingPlant(): Exit, Resolved ProcessingPlant, processingPlant->{}", meAsASoftwareComponent);
    }

    //
    // Dummy Route to ensure Startup
    //

    @Override
    public void configure() throws Exception {
        String processingPlantName = getFriendlyName();

        from("timer://" + processingPlantName + "?delay=1000&repeatCount=1")
                .routeId("ProcessingPlant::" + processingPlantName)
                .log(LoggingLevel.DEBUG, "Starting....");
    }

    private String getFriendlyName() {
        getLogger().debug(".getFriendlyName(): Entry");
        String nodeName = this.getMeAsASoftwareComponent().getComponentRDN().getNodeName();
        String nodeVersion = this.getMeAsASoftwareComponent().getComponentRDN().getNodeVersion();
        String friendlyName = nodeName + "(" + nodeVersion + ")";
        return (nodeName);
    }

    @Override
    public ProcessingPlantSoftwareComponent getMeAsASoftwareComponent() {
        return (this.meAsASoftwareComponent);
    }

    public TopologyNodeFDN getProcessingPlantNodeFDN() {
        return (this.meAsASoftwareComponent.getComponentFDN());
    }

    @Override
    public WorkshopSoftwareComponent getWorkshop(String workshopName, String version) {
        getLogger().debug(".getWorkshop(): Entry, workshopName --> {}, version --> {}", workshopName, version);
        boolean found = false;
        WorkshopSoftwareComponent foundWorkshop = null;
        for (TopologyNodeFDN containedWorkshopFDN : this.meAsASoftwareComponent.getWorkshops()) {
            WorkshopSoftwareComponent containedWorkshop = (WorkshopSoftwareComponent) topologyIM.getNode(containedWorkshopFDN);
            TopologyNodeRDN testRDN = new TopologyNodeRDN(PegacornSystemComponentTypeTypeEnum.WORKSHOP, workshopName, version);
            if (testRDN.equals(containedWorkshop.getComponentRDN())) {
                found = true;
                foundWorkshop = containedWorkshop;
                break;
            }
        }
        if (found) {
            getLogger().debug(".getWorkshop(): Exit, workshop found!");
            return (foundWorkshop);
        }
        getLogger().debug(".getWorkshop(): Exit, workshop not found!");
        return (null);
    }

    public WorkshopSoftwareComponent getWorkshop(String workshopName) {
        getLogger().debug(".getWorkshop(): Entry, workshopName --> {}", workshopName);
        String version = this.meAsASoftwareComponent.getComponentRDN().getNodeVersion();
        WorkshopSoftwareComponent workshop = getWorkshop(workshopName, version);
        getLogger().debug(".getWorkshop(): Exit");
        return (workshop);
    }

    @Override
    public String getSimpleFunctionName() {
        TopologyNodeRDN functionRDN = this.getMeAsASoftwareComponent().getNodeFunctionFDN().extractRDNForNodeType(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT);
        String functionName = functionRDN.getNodeName();
        return (functionName);
    }

    @Override
    public String getSimpleInstanceName() {
        String instanceName = getSimpleFunctionName() + "(" + instanceQualifier + ")";
        return (instanceName);
    }

    @Override
    public NetworkSecurityZoneEnum getNetworkZone() {
        NetworkSecurityZoneEnum securityZone = this.getMeAsASoftwareComponent().getSecurityZone();
        return (securityZone);
    }

    @Override
    public String getSubsystemParticipantName() {
        return (this.getMeAsASoftwareComponent().getSubsystemParticipantName());
    }

    @Override
    public String getSubsystemName() {
        return (this.getMeAsASoftwareComponent().getSubsystemParticipantName());
    }

    @Override
    public String getDeploymentSite() {
        TopologyNodeRDN siteRDN = this.getMeAsASoftwareComponent().getComponentFDN().extractRDNForNodeType(PegacornSystemComponentTypeTypeEnum.SITE);
        String siteName = siteRDN.getNodeName();
        return (siteName);
    }


    @Override
    public void registerCapabilityFulfillmentService(String capabilityName, CapabilityFulfillmentInterface fulfillmentInterface) {
        getLogger().debug(".registerCapabilityFulfillmentService(): Entry, capabilityName->{}", capabilityName);
        if (fulfillmentInterface == null) {
            getLogger().debug(".registerCapabilityFulfillmentService(): Exit, Capability Fulfillment Interface is NULL");
            return;
        }
        this.capabilityDeliveryServices.put(capabilityName, fulfillmentInterface);
        getLogger().debug(".registerCapabilityFulfillmentService(): Exit, Capability Fulillment Interface registered");
    }

    @Override
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request) {
        getLogger().debug(".executeTask(): Entry, request->{}", request);
        if (request == null) {
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setInstantCompleted(Instant.now());
            response.setSuccessful(false);
            getLogger().debug(".executeTask(): Exit, request is null, response->{}", response);
            return (response);
        }
        String capabilityName = request.getRequiredCapabilityName();
        CapabilityFulfillmentInterface interfaceToUse = this.capabilityDeliveryServices.get(capabilityName);
        if (interfaceToUse == null) {
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setInstantCompleted(Instant.now());
            response.setSuccessful(false);
            response.setInScope(false);
            getLogger().debug(".executeTask(): Exit, not registered capability, response->{}", response);
            return (response);
        }
        CapabilityUtilisationResponse capabilityUtilisationResponse = interfaceToUse.executeTask(request);
        getLogger().debug(".executeTask(): Exit, capabilityUtilisationResponse->{}", capabilityUtilisationResponse);
        return (capabilityUtilisationResponse);
    }

    @Override
    public boolean isITOpsNode() {
        return (false);
    }

    @Override
    public PetasosAuditEventGranularityLevelEnum getAuditEventGranularityLevel() {
        return (this.processingPlantAuditLevel);
    }

    //
    // Get ProcessingPlant Audit Level
    //

    protected PetasosAuditEventGranularityLevelEnum deriveAuditEventGranularityLevel() {
        List<ParameterNameValuePairType> otherDeploymentParameters = getPropertyFile().getDeploymentMode().getOtherDeploymentParameters();
        if (otherDeploymentParameters != null) {
            for (ParameterNameValuePairType currentNameValuePair : otherDeploymentParameters) {
                if (currentNameValuePair.getParameterName().equalsIgnoreCase(PetasosPropertyConstants.AUDIT_LEVEL_PARAMETER_NAME)) {
                    String parameterValue = currentNameValuePair.getParameterValue();
                    PetasosAuditEventGranularityLevelEnum petasosAuditEventGranularityLevelEnum = PetasosAuditEventGranularityLevelEnum.fromDisplayName(parameterValue);
                    if (petasosAuditEventGranularityLevelEnum == null) {
                        getLogger().warn(".deriveAuditEventGranularityLevel(): Unable to derive PetasosAuditEventGranularityLevelEnum, setting to AUDIT_LEVEL_COARSE");
                        return (PetasosAuditEventGranularityLevelEnum.AUDIT_LEVEL_COARSE);
                    } else {
                        return (petasosAuditEventGranularityLevelEnum);
                    }
                }
            }
        }
        getLogger().warn(".deriveAuditEventGranularityLevel(): Unable to derive PetasosAuditEventGranularityLevelEnum, setting to AUDIT_LEVEL_COARSE");
        return (PetasosAuditEventGranularityLevelEnum.AUDIT_LEVEL_COARSE);
    }

    //
    // Remote Subscription Functions
    //

    protected void subscribeToRemoteDataParcels(List<DataParcelTypeDescriptor> triggerEventList, String sourceSubsystemParticipantName) {
        getLogger().info(".subscribeToRemoteDataParcels(): Entry, sourceSubsystemParticipantName->{}", sourceSubsystemParticipantName);
        if (triggerEventList.isEmpty()) {
            getLogger().warn(".subscribeToRemoteDataParcels(): Exit, not subscribing to any remote content (subscriptionList is empty)");
            return;
        }
        if (StringUtils.isEmpty(sourceSubsystemParticipantName)) {
            getLogger().warn(".subscribeToRemoteDataParcels(): Exit, not subscribing to any remote content (sourceParticipantName is empty)");
            return;
        }
        getLogger().trace(".subscribeToRemoteDataParcels(): We have entries in the subscription list, processing");
        for (DataParcelTypeDescriptor currentTriggerEvent : triggerEventList) {
            getLogger().info(".subscribeToRemoteDataParcels(): currentTriggerEvent->{}", currentTriggerEvent);
            DataParcelTypeDescriptor container = new DataParcelTypeDescriptor();
            container.setVersion(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelDefiner(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelCategory(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelSubCategory(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelResource(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelSegment(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelAttribute(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelDiscriminatorType(DataParcelManifest.WILDCARD_CHARACTER);
            container.setDataParcelDiscriminatorValue(DataParcelManifest.WILDCARD_CHARACTER);
            getLogger().info(".subscribeToRemoteDataParcels(): container->{}", container);
            TaskWorkItemSubscriptionType manifest = new TaskWorkItemSubscriptionType();
            manifest.setContentDescriptor(currentTriggerEvent);
            manifest.setContainerDescriptor(container);
            manifest.setNormalisationStatus(DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_ANY);
            manifest.setValidationStatus(DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATION_ANY);
            manifest.setEnforcementPointApprovalStatus(PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_POSITIVE);
            manifest.setDataParcelFlowDirection(DataParcelDirectionEnum.INFORMATION_FLOW_CORE_DISTRIBUTION);
            manifest.setInterSubsystemDistributable(true);
            manifest.setDataParcelType(DataParcelTypeEnum.GENERAL_DATA_PARCEL_TYPE);
            manifest.setExternalSourceSystem(DataParcelManifest.WILDCARD_CHARACTER);

            PetasosParticipantId previousParticipantId = new PetasosParticipantId();
            previousParticipantId.setSubsystemName(sourceSubsystemParticipantName);
            previousParticipantId.setName(DataParcelManifest.WILDCARD_CHARACTER);
            previousParticipantId.setVersion(DataParcelManifest.WILDCARD_CHARACTER);
            manifest.setPreviousParticipant(previousParticipantId);

            PetasosParticipantId originParticipantId = new PetasosParticipantId();
            originParticipantId.setSubsystemName(sourceSubsystemParticipantName);
            originParticipantId.setName(DataParcelManifest.WILDCARD_CHARACTER);
            originParticipantId.setVersion(DataParcelManifest.WILDCARD_CHARACTER);
            manifest.setOriginParticipant(originParticipantId);

            getDistributedTaskSubscriptionMap().addSubscription(manifest);
        }
        getLogger().info(".subscribeToRemoteDataParcels(): Registration Processing Plant Petasos Participant ... :)");
    }


}
