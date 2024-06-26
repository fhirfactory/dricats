/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.encapsulatorroutes;

import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.encapsulatorroutes.beans.HealthcareServiceStimulusRegistrationBean;
import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.encapsulatorroutes.beans.HealthcareServiceUoWRegistrationBean;
import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.encapsulatorroutes.common.CDTTypeBaseBehaviourEncapsulatorRouteWUP;
import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.encapsulatorroutes.common.beans.UoW2StimulusListBean;
import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.orchestrator.HealthcareServiceCDTOrchestrator;
import net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.orchestrator.common.CDTOrchestratorBase;
import net.fhirfactory.pegacorn.internals.communicate.workflow.model.CDTTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.platform.WUPContainerEgressGatekeeper;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.platform.WUPContainerEgressProcessor;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.platform.WUPContainerIngresGatekeeper;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.platform.WUPContainerIngresProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HealthcareServiceCDTTypeWUPBehaviourEncapsulator extends CDTTypeBaseBehaviourEncapsulatorRouteWUP {
    private static final Logger LOG = LoggerFactory.getLogger(HealthcareServiceCDTTypeWUPBehaviourEncapsulator.class);
    private static final String HEALTHCARE_SERVICE_TWIN_TYPE_STIMULI_COLLECTOR_WUP_VERSION = "1.0.0";
    @Inject
    HealthcareServiceCDTOrchestrator orchestrator;

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    @Override
    protected String specifyTwinTypeName() {
        return (CDTTypeEnum.COMMUNICATE_TWIN_HEALTHCARE_SERVICE.getTwinType());
    }

    @Override
    protected String specifyTwinTypeVersion() {
        return (HEALTHCARE_SERVICE_TWIN_TYPE_STIMULI_COLLECTOR_WUP_VERSION);
    }

    @Override
    protected CDTOrchestratorBase getOrchestrator() {
        return (orchestrator);
    }

    @Override
    public void configure() throws Exception {
        getLogger().debug(".configure(): Entry!, for wupNode --> {}", getWUPFunctionToken());
        getLogger().debug("EndpointTwinTypeStimuliCollectorWUP :: EndPointWUPContainerIngresProcessorIngres --> {}", getRouteElementNameSet().getEndPointWUPContainerIngresProcessorIngres());
        getLogger().debug("EndpointTwinTypeStimuliCollectorWUP :: EndPointWUPContainerIngresProcessorEgress --> {}", getRouteElementNameSet().getEndPointWUPContainerIngresProcessorEgress());
        getLogger().debug("EndpointTwinTypeStimuliCollectorWUP :: EndPointWUPContainerIngresGatekeeperIngres --> {}", getRouteElementNameSet().getEndPointWUPContainerIngresGatekeeperIngres());
        getLogger().debug("EndpointTwinTypeStimuliCollectorWUP :: EndPointWUPIngresConduitIngres --> {}", getRouteElementNameSet().getEndPointWUPIngresConduitIngres());
        getLogger().debug("EndpointTwinTypeStimuliCollectorWUP :: EndPointWUPIngres --> {}", getRouteElementNameSet().getEndPointWUPIngres());

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerIngresProcessorIngres())
                .routeId(getRouteElementNameSet().getRouteWUPContainerIngressProcessor())
                .bean(WUPContainerIngresProcessor.class, "ingresContentProcessor(*, Exchange," + this.getTopologyNode().getComponentFDN().getToken().getTokenValue() + ")")
                .to(getRouteElementNameSet().getEndPointWUPContainerIngresProcessorEgress());

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerIngresProcessorEgress())
                .routeId(getRouteElementNameSet().getRouteIngresProcessorEgress2IngresGatekeeperIngres())
                .to(getRouteElementNameSet().getEndPointWUPContainerIngresGatekeeperIngres());

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerIngresGatekeeperIngres())
                .routeId(getRouteElementNameSet().getRouteWUPContainerIngresGateway())
                .bean(WUPContainerIngresGatekeeper.class, "ingresGatekeeper(*, Exchange," + this.getTopologyNode().getComponentFDN().getToken().getTokenValue() + ")");

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPIngresConduitIngres())
                .routeId(getRouteElementNameSet().getRouteCoreWUP())
                .bean(HealthcareServiceUoWRegistrationBean.class, "registerUoW")
                .split().method(UoW2StimulusListBean.class, "convertUoWContent2StimulusList")
                .bean(HealthcareServiceStimulusRegistrationBean.class, "registerStimulus");

        // --> Goes into Behaviour Sets

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerEgressProcessorIngres())
                .routeId(getRouteElementNameSet().getRouteWUPContainerEgressProcessor())
                .bean(WUPContainerEgressProcessor.class, "egressContentProcessor(*, Exchange," + this.getTopologyNode().getComponentFDN().getToken().getTokenValue() + ")")
                .to(getRouteElementNameSet().getEndPointWUPContainerEgressProcessorEgress());

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerEgressProcessorEgress())
                .routeId(getRouteElementNameSet().getRouteWUPEgressProcessorEgress2WUPEgressGatekeeperIngres())
                .to(getRouteElementNameSet().getEndPointWUPContainerEgressGatekeeperIngres());

        fromWithStandardExceptionHandling(getRouteElementNameSet().getEndPointWUPContainerEgressGatekeeperIngres())
                .routeId(getRouteElementNameSet().getRouteWUPContainerEgressGateway())
                .bean(WUPContainerEgressGatekeeper.class, "egressGatekeeper(*, Exchange," + this.getTopologyNode().getComponentFDN().getToken().getTokenValue() + ")");
    }
}
