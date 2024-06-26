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
package net.fhirfactory.dricats.core.oam.topology.factories;

import net.fhirfactory.dricats.core.model.endpoint.PetasosEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkUnitProcessorSoftwareComponent;
import net.fhirfactory.dricats.core.model.ui.resources.summaries.EndpointSummary;
import net.fhirfactory.dricats.core.model.ui.resources.summaries.WorkUnitProcessorSummary;
import net.fhirfactory.dricats.core.oam.topology.factories.common.PetasosMonitoredComponentFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PetasosMonitoredWorkUnitProcessorFactory extends PetasosMonitoredComponentFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosMonitoredWorkUnitProcessorFactory.class);

    @Inject
    private PetasosEndpointSummaryFactory endpointFactory;

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    public WorkUnitProcessorSummary newWorkUnitProcessor(String workshopParticipantName, WorkUnitProcessorSoftwareComponent wupTopologyNode) {
        getLogger().debug(".newWorkUnitProcessor(): wupTopologyNode->{}", wupTopologyNode);
        WorkUnitProcessorSummary wup = new WorkUnitProcessorSummary();
        wup = (WorkUnitProcessorSummary) newPetasosMonitoredComponent(wup, wupTopologyNode);
        wup.setWorkshopParticipantName(workshopParticipantName);
        if (wupTopologyNode.getEgressEndpoint() != null) {
            IPCTopologyEndpoint egressEndpoint = wupTopologyNode.getEgressEndpoint();
            try {
                EndpointSummary egressMonitoredEndpoint = (EndpointSummary) endpointFactory.newEndpoint(wup.getParticipantName(), egressEndpoint);
                wup.addEndpoint(egressMonitoredEndpoint);
            } catch (Exception ex) {
                getLogger().warn(".newWorkUnitProcessor(): Unable to create EndpointSummary for ->{}, error->{} ", egressEndpoint, ExceptionUtils.getMessage(ex));
            }

        }
        if (wupTopologyNode.getIngresEndpoint() != null) {
            IPCTopologyEndpoint ingresEndpoint = wupTopologyNode.getIngresEndpoint();
            try {
                EndpointSummary ingresMonitoredEndpoint = (EndpointSummary) endpointFactory.newEndpoint(wup.getParticipantName(), ingresEndpoint);
                wup.addEndpoint(ingresMonitoredEndpoint);
            } catch (Exception ex) {
                getLogger().warn(".newWorkUnitProcessor(): Unable to create EndpointSummary for ->{}, error->{} ", ingresEndpoint, ExceptionUtils.getMessage(ex));
            }
        }
        if (!wupTopologyNode.getServiceEndpoints().isEmpty()) {
            for (PetasosEndpoint currentEndpoint : wupTopologyNode.getServiceEndpoints().values()) {
                try {
                    EndpointSummary jgroupsEndpoint = (EndpointSummary) endpointFactory.newEndpoint(wup.getParticipantName(), currentEndpoint);
                    wup.addEndpoint(jgroupsEndpoint);
                } catch (Exception ex) {
                    getLogger().warn(".newWorkUnitProcessor(): Unable to create EndpointSummary for ->{}, error->{} ", currentEndpoint, ExceptionUtils.getMessage(ex));
                }
            }
        }
        getLogger().debug(".newWorkUnitProcessor(): wup->{}", wup);
        return (wup);
    }
}
