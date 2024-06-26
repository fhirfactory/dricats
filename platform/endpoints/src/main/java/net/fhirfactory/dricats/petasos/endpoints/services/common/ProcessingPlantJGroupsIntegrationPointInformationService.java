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
package net.fhirfactory.dricats.petasos.endpoints.services.common;

import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDNToken;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.endpoint.JGroupsIntegrationPointNamingUtilities;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.participant.ProcessingPlantPetasosParticipantHolder;
import net.fhirfactory.dricats.core.model.topology.nodes.DefaultWorkshopSetEnum;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.petasos.endpoints.map.JGroupsIntegrationPointSharedMap;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProcessingPlantJGroupsIntegrationPointInformationService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingPlantJGroupsIntegrationPointInformationService.class);
    private static String PETASOS_EDGE_MESSAGE_FORWARDER_WUP_NAME = "PetasosIPCMessageForwardWUP";
    private static String EDGE_FORWARDER_WUP_VERSION = "1.0.0";
    private static Long ENDPOINT_VALIDATION_START_DELAY = 60000L;
    private static Long ENDPOINT_VALIDATION_PERIOD = 10000L;
    @Inject
    private JGroupsIntegrationPointNamingUtilities endpointNameUtilities;
    @Inject
    private ProcessingPlantPetasosParticipantHolder myProcessingPlantPetasosParticipant;
    private boolean initialised;
    @Inject
    private ProcessingPlantInterface processingPlant;

    @Inject
    private TopologyIM topologyIM;

    @Inject
    private PegacornCommonInterfaceNames interfaceNames;

    @Inject
    private JGroupsIntegrationPointSharedMap endpointMap;

    @Inject
    private ProcessingPlantJGroupsIntegrationPointWatchdog coreSubsystemPetasosEndpointsWatchdog;

    //
    // Constructor
    //

    public ProcessingPlantJGroupsIntegrationPointInformationService() {
        this.initialised = false;
    }

    //
    // PostConstruct initialisation
    //

    @PostConstruct
    public void initialise() {
        if (initialised) {
            return;
        }
        initialised = true;
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected ProcessingPlantInterface getProcessingPlant() {
        return (processingPlant);
    }

    protected TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    public Long getEndpointValidationStartDelay() {
        return ENDPOINT_VALIDATION_START_DELAY;
    }

    public Long getEndpointValidationPeriod() {
        return ENDPOINT_VALIDATION_PERIOD;
    }

    //
    // Deriving My Role
    //

    protected TopologyNodeFDNToken deriveAssociatedForwarderFDNToken() {
        getLogger().info(".deriveAssociatedForwarderFDNToken(): Entry");
        TopologyNodeFDN workshopNodeFDN = deriveWorkshopFDN();
        TopologyNodeFDN wupNodeFDN = SerializationUtils.clone(workshopNodeFDN);
        wupNodeFDN.appendTopologyNodeRDN(new TopologyNodeRDN(PegacornSystemComponentTypeTypeEnum.WUP, PETASOS_EDGE_MESSAGE_FORWARDER_WUP_NAME, EDGE_FORWARDER_WUP_VERSION));
        TopologyNodeFDNToken associatedForwarderWUPToken = wupNodeFDN.getToken();
        return (associatedForwarderWUPToken);
    }

    //
    // Building the FDN
    //

    private TopologyNodeFDN deriveWorkshopFDN() {
        TopologyNodeFDN processingPlantFDN = getProcessingPlant().getMeAsASoftwareComponent().getComponentFDN();
        TopologyNodeFDN futureWorkshopFDN = SerializationUtils.clone(processingPlantFDN);
        TopologyNodeRDN newRDN = new TopologyNodeRDN(PegacornSystemComponentTypeTypeEnum.WORKSHOP, DefaultWorkshopSetEnum.EDGE_WORKSHOP.getWorkshop(), getProcessingPlant().getMeAsASoftwareComponent().getComponentRDN().getNodeVersion());
        futureWorkshopFDN.appendTopologyNodeRDN(newRDN);
        return (futureWorkshopFDN);
    }
}
