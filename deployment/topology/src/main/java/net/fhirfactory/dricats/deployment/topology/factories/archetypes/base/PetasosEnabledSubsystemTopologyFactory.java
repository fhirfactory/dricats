package net.fhirfactory.dricats.deployment.topology.factories.archetypes.base;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.endpoint.JGroupsIntegrationPointNamingUtilities;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointFunctionTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosIntegrationPointNameEnum;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.answer.StandardEdgeAnswerHTTPEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.ask.StandardEdgeAskHTTPEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.PetasosEnabledSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups.JGroupsInterZoneRepeaterClientPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.endpoints.HTTPTopologyEndpointFactory;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.endpoints.JGroupsTopologyEndpointFactory;
import net.fhirfactory.dricats.util.PegacornProperties;

import javax.inject.Inject;

/**
 * The PetasosEnabledSubsystemTopologyFactory adds the Ports/IntegrationPoints/Endpoints associated with delivering
 * Petasos based services within a ClusterService and/or ProcessingPlant. Predominantly it adds the Edge service ports
 * (EdgeAnswer, a FHIR Server; EdgeAsk, a FHIR Client; JGroupsIntegrationPoints, etc)
 */
public abstract class PetasosEnabledSubsystemTopologyFactory extends PegacornTopologyFactoryBase {

    @Inject
    private PegacornReferenceProperties pegacornReferenceProperties;

    @Inject
    private JGroupsIntegrationPointNamingUtilities jgroupNamingUtilities;

    @Inject
    private PegacornProperties pegacornProperties;

    @Inject
    private HTTPTopologyEndpointFactory httpTopologyEndpointFactory;

    @Inject
    private JGroupsTopologyEndpointFactory jgroupsTopologyEndpointFactory;

    //
    // Constructor(s)
    //

    public PetasosEnabledSubsystemTopologyFactory() {
        super();
    }

    //
    // Getters (and Setters)
    //

    protected HTTPTopologyEndpointFactory getHTTPTopologyEndpointFactory() {
        return (httpTopologyEndpointFactory);
    }

    protected JGroupsTopologyEndpointFactory getJGroupsTopologyEndpointFactory() {
        return (jgroupsTopologyEndpointFactory);
    }

    //
    // Build EdgeAnswer Port (if there)
    //

    protected void addEdgeAnswerPort(EndpointProviderInterface endpointProvider) {
        getLogger().debug(".addEdgeAnswerPort(): Entry");
        PetasosEnabledSubsystemPropertyFile petasosEnabledSubsystemPropertyFile = (PetasosEnabledSubsystemPropertyFile) getPropertyFile();
        StandardEdgeAnswerHTTPEndpoint edgeAnswerPort = new StandardEdgeAnswerHTTPEndpoint();
        ClusteredHTTPServerPortSegment edgeAnswerPortConfig = petasosEnabledSubsystemPropertyFile.getEdgeAnswer();
        PetasosIntegrationPointNameEnum petasosIntegrationPointName = PetasosIntegrationPointNameEnum.fromConfigName(edgeAnswerPortConfig.getName());
        if (petasosIntegrationPointName == null) {
            throw (new IllegalArgumentException("edgeAnswer does not have an appropriate name defined --> should be:" + PetasosIntegrationPointNameEnum.PETASOS_EDGE_ANSWER.getConfigName()));
        }
        httpTopologyEndpointFactory.newHTTPServerTopologyEndpoint(getPropertyFile(), endpointProvider, petasosIntegrationPointName.getParticipantName(), edgeAnswerPortConfig);
        getLogger().debug(".addEdgeAnswerPort(): Exit, endpoint added");
    }

    protected void addEdgeAskPort(EndpointProviderInterface endpointProvider) {
        getLogger().debug(".addEdgeAskPort(): Entry");
        PetasosEnabledSubsystemPropertyFile petasosEnabledSubsystemPropertyFile = (PetasosEnabledSubsystemPropertyFile) getPropertyFile();
        StandardEdgeAskHTTPEndpoint edgeAskPort = new StandardEdgeAskHTTPEndpoint();
        HTTPClientPortSegment httpClientPortConfigSegment = petasosEnabledSubsystemPropertyFile.getEdgeAsk();
        if (httpClientPortConfigSegment == null) {
            getLogger().debug(".addEdgeAskPort(): Exit, no httpClientPortConfigSegment to add");
            return;
        }
        String name = PetasosIntegrationPointNameEnum.PETASOS_EDGE_ANSWER.getParticipantName();
        httpTopologyEndpointFactory.newHTTPClientTopologyEndpoint(getPropertyFile(), endpointProvider, name, httpClientPortConfigSegment);
        getLogger().debug(".addEdgeAskPort(): Exit, endpoint added");
    }

    /**
     * This method adds ALL the JGroupsIntegrationPoints to the supplied endpointProvider. Note that, in most cases,
     * the JGroupsIntegrationPoints DO NOT expose a kubernetes service, but rather interact with an interzone repeater
     * as a client or via kube-ping based services at the POD level.
     *
     * @param endpointProvider The endpointProvider to which the JGroupsIntegrationPoints should be added (from a Topology
     *                         management perspective). Typically a ProcessingPlant.
     */
    protected void addAllJGroupsEndpoints(EndpointProviderInterface endpointProvider) {
        PetasosEnabledSubsystemPropertyFile petasosEnabledSubsystemPropertyFile = (PetasosEnabledSubsystemPropertyFile) getPropertyFile();

        //
        // Petasos Notifications Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneTaskForwarder = petasosEnabledSubsystemPropertyFile.getPetasosIPCMessagingEndpoint();
        String interZoneTaskForwarderName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_NOTIFICATIONS_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneTaskForwarder, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_NOTIFICATIONS_ENDPOINT, interZoneTaskForwarderName, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Topology Services Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneTopology = petasosEnabledSubsystemPropertyFile.getPetasosTopologyDiscoveryEndpoint();
        String interZoneTopologyName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TOPOLOGY_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneTopology, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TOPOLOGY_ENDPOINT, interZoneTopologyName, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Subscription Services Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneSubscriptions = petasosEnabledSubsystemPropertyFile.getPetasosSubscriptionsEndpoint();
        String interZoneSubscriptionName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_SUBSCRIPTIONS_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneSubscriptions, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_SUBSCRIPTIONS_ENDPOINT, interZoneSubscriptionName, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Audit Services Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneAudit = petasosEnabledSubsystemPropertyFile.getPetasosAuditServicesEndpoint();
        String interZoneAuditName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_AUDIT_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneAudit, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_AUDIT_ENDPOINT, interZoneAuditName, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Interception Services Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneInterception = petasosEnabledSubsystemPropertyFile.getPetasosInterceptionEndpoint();
        String interZoneInterceptionName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_INTERCEPTION_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneInterception, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_INTERCEPTION_ENDPOINT, interZoneInterceptionName, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Task Grid Services Client Endpoint (Uno)
        JGroupsInterZoneRepeaterClientPortSegment interZoneTasking = petasosEnabledSubsystemPropertyFile.getPetasosTaskServicesEndpoint();
        String unoTaskServices = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_DISTRIBUTION_GRID_CLIENT_ENDPOINT_UNO.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneTasking, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_DISTRIBUTION_GRID_CLIENT_ENDPOINT_UNO, unoTaskServices, getResilienceMode(), getConcurrenceMode());
        // Petasos Task Grid Services Client Endpoint (Duo)
        String duoTaskServices = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_DISTRIBUTION_GRID_CLIENT_ENDPOINT_DUO.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneTasking, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_DISTRIBUTION_GRID_CLIENT_ENDPOINT_DUO, duoTaskServices, getResilienceMode(), getConcurrenceMode());
        // Petasos Task Grid Capability Endpoint
        String capabilityExecutionEndpoint = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_CAPABILITY_EXECUTION_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneTasking, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_TASK_CAPABILITY_EXECUTION_ENDPOINT, capabilityExecutionEndpoint, getResilienceMode(), getConcurrenceMode());

        //
        // Petasos Metrics Services Endpoint
        JGroupsInterZoneRepeaterClientPortSegment interZoneMetrics = petasosEnabledSubsystemPropertyFile.getPetasosMetricsEndpoint();
        String interZoneMetricsName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_METRICS_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, interZoneMetrics, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_METRICS_ENDPOINT, interZoneMetricsName, getResilienceMode(), getConcurrenceMode());

        //
        // Multizone Infinispan
        JGroupsInterZoneRepeaterClientPortSegment multiZoneInfinispan = petasosEnabledSubsystemPropertyFile.getMultiuseInfinispanEndpoint();
        String multiZoneInfinispanName = getInterfaceNames().getEndpointName(PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_INFINISPAN_ENDPOINT.getDisplayName());
        getJGroupsTopologyEndpointFactory().newInterZoneRepeaterJGroupsIntegrationPoint(getProcessingPlantNode(), getPropertyFile(), endpointProvider, multiZoneInfinispan, PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT, PetasosEndpointFunctionTypeEnum.PETASOS_INFINISPAN_ENDPOINT, multiZoneInfinispanName, getResilienceMode(), getConcurrenceMode());
    }
}
