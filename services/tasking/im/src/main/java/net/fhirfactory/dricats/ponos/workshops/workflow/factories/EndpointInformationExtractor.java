/*
 * The MIT License
 *
 * Copyright 2022 Mark A. Hunter (ACT Health).
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
package net.fhirfactory.dricats.ponos.workshops.workflow.factories;

import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkUnitProcessorSoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author m.a.hunter@outlook.com
 */
@ApplicationScoped
public class EndpointInformationExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(EndpointInformationExtractor.class);


    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    //
    // Business Methods
    //

    public ComponentIdType getEndpointComponentId(PetasosActionableTask actionableTask) {
        getLogger().debug(".getEndpointComponentId(): Entry, actionableTask->{}", actionableTask);

        ComponentIdType componentId = null;

        if (actionableTask != null) {
            if (actionableTask.hasTaskFulfillment()) {
                if (actionableTask.getTaskFulfillment().hasFulfillerWorkUnitProcessor()) {
                    componentId = actionableTask.getTaskFulfillment().getFulfillerWorkUnitProcessor().getComponentID();
                }
            }
        }

        getLogger().debug(".getEndpointComponentId(): Exit, componentId->{}", componentId);
        return (componentId);
    }

    public String getEndpointParticipantName(PetasosActionableTask actionableTask, boolean isIngres) {
        getLogger().debug(".getEndpointParticipantName(): Entry, actionableTask->{}, isIngres->{}", actionableTask, isIngres);

        String participantName = null;
        if (actionableTask != null) {
            if (actionableTask.hasTaskFulfillment()) {
                if (actionableTask.getTaskFulfillment().hasFulfillerWorkUnitProcessor()) {
                    WorkUnitProcessorSoftwareComponent wupSoftwareComponent = (WorkUnitProcessorSoftwareComponent) actionableTask.getTaskFulfillment().getFulfillerWorkUnitProcessor();
                    participantName = getEndpointParticipantName(wupSoftwareComponent, isIngres);
                }
            }
        }

        getLogger().debug(".getEndpointParticipantName(): Exit, participantName->{}", participantName);
        return (participantName);
    }

    public String getEndpointParticipantName(WorkUnitProcessorSoftwareComponent endpointWUP, boolean isIngres) {
        getLogger().debug(".getEndpointParticipantName(): Entry, endpointWUP->{}", endpointWUP);

        String participantName = null;
        if (isIngres) {
            IPCTopologyEndpoint ingresEndpoint = endpointWUP.getIngresEndpoint();
            if (ingresEndpoint != null) {
                if (ingresEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.MLLP_SERVER)) {
                    participantName = ingresEndpoint.getParticipantName();
                } else if (ingresEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.HTTP_API_SERVER)) {
                    participantName = ingresEndpoint.getParticipantName();
                } else if (ingresEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SOURCE)) {
                    participantName = ingresEndpoint.getParticipantName();
                }
            }
        } else {
            IPCTopologyEndpoint egressEndpoint = endpointWUP.getEgressEndpoint();
            if (egressEndpoint != null) {
                if (egressEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.MLLP_CLIENT)) {
                    participantName = egressEndpoint.getParticipantName();
                } else if (egressEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.HTTP_API_CLIENT)) {
                    participantName = egressEndpoint.getParticipantName();
                } else if (egressEndpoint.getEndpointType().equals(PetasosEndpointTopologyTypeEnum.FILE_SHARE_SINK)) {
                    participantName = egressEndpoint.getParticipantName();
                }
            }
        }

        getLogger().debug(".getEndpointParticipantName(): Exit, participantName->{}", participantName);
        return (participantName);
    }
}
