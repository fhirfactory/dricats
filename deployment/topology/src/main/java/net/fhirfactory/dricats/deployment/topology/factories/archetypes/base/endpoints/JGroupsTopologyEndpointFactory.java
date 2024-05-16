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
import net.fhirfactory.dricats.core.model.endpoint.JGroupsIntegrationPointNamingUtilities;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointFunctionTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosIntegrationPointNameEnum;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapterDefinition;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.JGroupsIntegrationPoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.datatypes.JGroupsAdapter;
import net.fhirfactory.dricats.core.model.topology.mode.ConcurrencyModeEnum;
import net.fhirfactory.dricats.core.model.topology.mode.NetworkSecurityZoneEnum;
import net.fhirfactory.dricats.core.model.topology.mode.ResilienceModeEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.ProcessingPlantSoftwareComponent;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.PetasosEnabledSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.InterfaceDefinitionSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups.JGroupsInterZoneRepeaterClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups.JGroupsKubernetesPodPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.common.TopologyFactoryHelpersBase;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class JGroupsTopologyEndpointFactory extends TopologyFactoryHelpersBase {
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsTopologyEndpointFactory.class);

    @Inject
    private JGroupsIntegrationPointNamingUtilities jgroupNamingUtilities;

    @Inject
    private TopologyIM topologyIM;

    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    protected JGroupsIntegrationPointNamingUtilities getJGroupsNamingUtilities() {
        return (jgroupNamingUtilities);
    }

    protected TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    //
    // Business Methods
    //

    public void addJGroupsEndpoint(ProcessingPlantSoftwareComponent processingPlantNode,
                                   BaseSubsystemPropertyFile propertyFile,
                                   EndpointProviderInterface endpointProvider,
                                   JGroupsKubernetesPodPortSegment jgroupsIPCSegment,
                                   PetasosEndpointTopologyTypeEnum petasosEndpointType,
                                   PetasosEndpointFunctionTypeEnum function, String name,
                                   ResilienceModeEnum resilienceMode,
                                   ConcurrencyModeEnum concurrencyMode) {
        getLogger().debug(".addJGroupsEndpoint(): Entry, endpointProvider->{}, jgroupsIPCSegment->{}, petasosEndpointType->{}, function->{}, name->{}", endpointProvider, jgroupsIPCSegment, petasosEndpointType, function, name);
        PetasosEnabledSubsystemPropertyFile petasosEnabledSubsystemPropertyFile = (PetasosEnabledSubsystemPropertyFile) propertyFile;
        JGroupsIntegrationPoint jgroupsIP = new JGroupsIntegrationPoint();
        if (jgroupsIPCSegment == null) {
            getLogger().debug(".addJGroupsEndpoint(): Exit, no jgroupsIPCSegment to add");
            return;
        }
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jgroupsIP.setComponentRDN(nodeRDN);
        jgroupsIP.setEndpointConfigurationName(jgroupsIPCSegment.getName());
        jgroupsIP.setActualHostIP(getActualHostIP());
        jgroupsIP.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        jgroupsIP.setEndpointType(petasosEndpointType);
        NetworkSecurityZoneEnum securityZone = NetworkSecurityZoneEnum.fromDisplayName(petasosEnabledSubsystemPropertyFile.getDeploymentZone().getSecurityZoneName());
        jgroupsIP.setSecurityZone(securityZone);
        jgroupsIP.setNameSpace(petasosEnabledSubsystemPropertyFile.getDeploymentZone().getNameSpace());
        jgroupsIP.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        jgroupsIP.setComponentSystemRole(SoftwareComponentConnectivityContextEnum.COMPONENT_ROLE_SUBSYSTEM_EDGE);
        jgroupsIP.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jgroupsIP.setServer(true);
        jgroupsIP.setResilienceMode(resilienceMode);
        jgroupsIP.setConcurrencyMode(concurrencyMode);
        jgroupsIP.setContainingNodeFDN(endpointProvider.getComponentFDN());
        String subsystemParticipantName = petasosEnabledSubsystemPropertyFile.getSubsystemInstant().getParticipantName();
        jgroupsIP.setSubsystemParticipantName(subsystemParticipantName);
        String site = petasosEnabledSubsystemPropertyFile.getSubsystemInstant().getSite();
        jgroupsIP.setDeploymentSite(site);
        jgroupsIP.setInterfaceFunction(function);
        String channelName = getJGroupsNamingUtilities().buildChannelName(site, securityZone.getDisplayName(), subsystemParticipantName, function.getDisplayName(), getJGroupsNamingUtilities().getCurrentUUID());
        jgroupsIP.setChannelName(channelName);
        jgroupsIP.setEnablingProcessingPlantId(processingPlantNode.getComponentID());
        String clusterName = propertyFile.getSubsystemInstant().getClusterServiceName();
        jgroupsIP.setEndpointDescription(jgroupsIPCSegment.getPortType());
        if (StringUtils.isEmpty(clusterName)) {
            getLogger().error(".addInterZoneRepeaterJGroupsIntegrationPoint(): Bad definition of Petasos IPC Endpoint->{}", jgroupsIPCSegment.getName());
            jgroupsIP.setParticipantName("unknown");
        } else {
            jgroupsIP.setParticipantName(clusterName);
        }
        jgroupsIP.setEndpointDescription(jgroupsIPCSegment.getPortType());

        JGroupsAdapter adapter = new JGroupsAdapter();
        adapter.setPortNumber(jgroupsIPCSegment.getServerPort());
        adapter.setHostName(jgroupsIPCSegment.getServerHostname());
        adapter.setEncrypted(jgroupsIPCSegment.isEncrypted());
        for (InterfaceDefinitionSegment currentSegment : jgroupsIPCSegment.getSupportedInterfaceProfiles()) {
            IPCAdapterDefinition currentInterfaceDefinition = new IPCAdapterDefinition();
            currentInterfaceDefinition.setInterfaceFormalName(currentSegment.getInterfaceDefinitionName());
            currentInterfaceDefinition.setInterfaceFormalVersion(currentSegment.getInterfaceDefinitionVersion());
            adapter.getSupportedInterfaceDefinitions().add(currentInterfaceDefinition);
        }
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
        jgroupsIP.setJGroupsAdapter(adapter);
        endpointProvider.addEndpoint(jgroupsIP.getComponentFDN());
        getLogger().trace(".addJGroupsEndpoint(): Add the InterZone JGroups IPC Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), jgroupsIP);
        getLogger().debug(".addJGroupsEndpoint(): Exit, endpoint added");
    }

    /**
     * This method creates a JGroupsIntegrationPoint (JChannel) for a given integration function (e.g. tasking,
     * subscriptions, messaging, interception, etc.) - populating it with a combination of ProcessingPlant sourced
     * information and PropertyFile() based information as passed in the JGroupsInterZoneRepeaterClientPortSegment
     * data element.
     *
     * @param endpointProvider    The entity to which this integrationPoint should be "added"
     * @param jgroupsIPCSegment   The PropertyFile segment describing the key attributes of this integrationPoint
     * @param petasosEndpointType What sort of PetasosEndpointType should be assigned to the integrationPoint
     * @param function            What is it's integration "function" (i.e. tasking, subscriptions, messaging, etc.)
     * @param name                What name should be given to the create integrationPoint (SoftwareComponent)
     */
    public void newInterZoneRepeaterJGroupsIntegrationPoint(ProcessingPlantSoftwareComponent processingPlantNode,
                                                            BaseSubsystemPropertyFile propertyFile,
                                                            EndpointProviderInterface endpointProvider,
                                                            JGroupsInterZoneRepeaterClientPortSegment jgroupsIPCSegment,
                                                            PetasosEndpointTopologyTypeEnum petasosEndpointType,
                                                            PetasosEndpointFunctionTypeEnum function, String name,
                                                            ResilienceModeEnum resilienceMode,
                                                            ConcurrencyModeEnum concurrencyMode) {
        getLogger().debug(".addJGroupsIntegrationPoint(): Entry, endpointProvider->{}, jgroupsIPCSegment->{}, petasosEndpointType->{}, function->{}, name->{}", endpointProvider, jgroupsIPCSegment, petasosEndpointType, function, name);
        PetasosEnabledSubsystemPropertyFile petasosEnabledSubsystemPropertyFile = (PetasosEnabledSubsystemPropertyFile) propertyFile;
        JGroupsIntegrationPoint jgroupsIP = new JGroupsIntegrationPoint();
        if (jgroupsIPCSegment == null) {
            getLogger().debug(".addJGroupsIntegrationPoint(): Exit, no jgroupsIPCSegment to add");
            return;
        }
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jgroupsIP.setComponentRDN(nodeRDN);
        jgroupsIP.setEndpointConfigurationName(jgroupsIPCSegment.getName());
        jgroupsIP.setActualHostIP(getActualHostIP());
        jgroupsIP.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        jgroupsIP.setEndpointType(petasosEndpointType);
        NetworkSecurityZoneEnum securityZone = NetworkSecurityZoneEnum.fromDisplayName(petasosEnabledSubsystemPropertyFile.getDeploymentZone().getSecurityZoneName());
        jgroupsIP.setSecurityZone(securityZone);
        jgroupsIP.setNameSpace(petasosEnabledSubsystemPropertyFile.getDeploymentZone().getNameSpace());
        jgroupsIP.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        jgroupsIP.setComponentSystemRole(SoftwareComponentConnectivityContextEnum.COMPONENT_ROLE_SUBSYSTEM_EDGE);
        jgroupsIP.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        jgroupsIP.setServer(true);
        jgroupsIP.setResilienceMode(resilienceMode);
        jgroupsIP.setConcurrencyMode(concurrencyMode);
        jgroupsIP.setContainingNodeFDN(endpointProvider.getComponentFDN());
        String subsystemParticipantName = petasosEnabledSubsystemPropertyFile.getSubsystemInstant().getParticipantName();
        jgroupsIP.setSubsystemParticipantName(subsystemParticipantName);
        String site = petasosEnabledSubsystemPropertyFile.getSubsystemInstant().getSite();

        jgroupsIP.setDeploymentSite(site);
        jgroupsIP.setInterfaceFunction(function);
        String channelName = getJGroupsNamingUtilities().buildChannelName(site, securityZone.getDisplayName(), subsystemParticipantName, function.getDisplayName(), getJGroupsNamingUtilities().getCurrentUUID());
        jgroupsIP.setChannelName(channelName);
        getLogger().info(".addJGroupsIntegrationPoint(): channelName->{}", jgroupsIP.getChannelName());
        jgroupsIP.setEnablingProcessingPlantId(processingPlantNode.getComponentID());
        PetasosIntegrationPointNameEnum endpointName = PetasosIntegrationPointNameEnum.fromConfigName(jgroupsIPCSegment.getName());
        if (endpointName == null) {
            getLogger().error(".addInterZoneRepeaterJGroupsIntegrationPoint(): Bad definition of Petasos IPC Endpoint->{}", jgroupsIPCSegment.getName());
            jgroupsIP.setParticipantName("unknown");
        } else {
            jgroupsIP.setParticipantName(endpointProvider.getParticipantName() + "." + endpointName.getParticipantName());
        }
        jgroupsIP.setEndpointDescription(jgroupsIPCSegment.getPortType());
        jgroupsIP.setParticipantDisplayName(jgroupsIP.getParticipantName());
        JGroupsAdapter adapter = new JGroupsAdapter();
        adapter.setPortNumber(jgroupsIPCSegment.getTargetPortValue());
        adapter.setHostName(jgroupsIPCSegment.getTargetHostName());
        adapter.setEncrypted(jgroupsIPCSegment.isEncrypted());
        for (InterfaceDefinitionSegment currentSegment : jgroupsIPCSegment.getSupportedInterfaceProfiles()) {
            IPCAdapterDefinition currentInterfaceDefinition = new IPCAdapterDefinition();
            currentInterfaceDefinition.setInterfaceFormalName(currentSegment.getInterfaceDefinitionName());
            currentInterfaceDefinition.setInterfaceFormalVersion(currentSegment.getInterfaceDefinitionVersion());
            adapter.getSupportedInterfaceDefinitions().add(currentInterfaceDefinition);
        }
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_MULTISITE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_STANDALONE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_KUBERNETES_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_CLUSTERED);
        adapter.getSupportedDeploymentModes().add(ResilienceModeEnum.RESILIENCE_MODE_MULTISITE_CLUSTERED);
        jgroupsIP.setJGroupsAdapter(adapter);
        endpointProvider.addEndpoint(jgroupsIP.getComponentFDN());
        getLogger().trace(".addJGroupsIntegrationPoint(): Add the InterZone JGroups IPC Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), jgroupsIP);
        getLogger().info(".addJGroupsIntegrationPoint(): Exit, integrationPoint added, jgroupsIP->{}", jgroupsIP);
    }
}
