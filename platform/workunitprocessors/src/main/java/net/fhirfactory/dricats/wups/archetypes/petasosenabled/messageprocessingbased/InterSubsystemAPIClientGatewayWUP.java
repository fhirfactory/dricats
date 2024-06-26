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

package net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.messaging.wup.APIClientWUPEndpointContainer;
import net.fhirfactory.dricats.core.messaging.wup.MessageBasedWUPEndpointContainer;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.wup.valuesets.WUPArchetypeEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.RouteDefinition;

import java.util.ArrayList;
import java.util.List;

public abstract class InterSubsystemAPIClientGatewayWUP extends MOAStandardWUP {

    private MessageBasedWUPEndpointContainer ingresEndpoint;

    public InterSubsystemAPIClientGatewayWUP() {
        super();
//        getLogger().debug(".MessagingIngresGatewayWUP(): Entry, Default constructor");
    }

    protected abstract String specifyIngresTopologyEndpointName();

    protected abstract String specifyIngresEndpointVersion();

    protected abstract APIClientWUPEndpointContainer specifyAPIClientEndpoint();

    @Override
    protected WUPArchetypeEnum specifyWUPArchetype() {
        return (WUPArchetypeEnum.WUP_NATURE_MESSAGE_EXTERNAL_INGRES_POINT);
    }

    @Override
    protected MessageBasedWUPEndpointContainer specifyEgressEndpoint() {
        getLogger().debug(".specifyEgressTopologyEndpoint(): Entry");
        MessageBasedWUPEndpointContainer egressEndpoint = new MessageBasedWUPEndpointContainer();
        egressEndpoint.setFrameworkEnabled(true);
        egressEndpoint.setEndpointSpecification(this.getNameSet().getEndPointWUPEgress());
        getLogger().debug(".specifyEgressTopologyEndpoint(): Exit");
        return (egressEndpoint);
    }

    @Override
    protected MessageBasedWUPEndpointContainer specifyIngresEndpoint() {
        getLogger().debug(".specifyIngresTopologyEndpoint(): Entry");
        MessageBasedWUPEndpointContainer ingressEndpoint = new MessageBasedWUPEndpointContainer();
        ingressEndpoint.setFrameworkEnabled(true);
        ingressEndpoint.setEndpointSpecification(this.getNameSet().getEndPointWUPIngres());
        getLogger().debug(".specifyIngresTopologyEndpoint(): Exit");
        return (ingressEndpoint);
    }

    /**
     * The Ingres Message Gateway doesn't subscribe to ANY topics as it receives it's
     * input from an external system.
     *
     * @return An empty Set<TopicToken>
     */
    @Override
    protected List<DataParcelManifest> specifySubscriptionTopics() {
        List<DataParcelManifest> subTopics = new ArrayList<>();
        return (subTopics);
    }

    /**
     * @param uri
     * @return the RouteBuilder.from(uri) with all exceptions logged but not handled
     */
    protected RouteDefinition fromInteractIngresService(String uri) {
        NodeDetailInjector nodeDetailInjector = new NodeDetailInjector();
        SourceSystemDetailInjector sourceSystemDetailInjector = new SourceSystemDetailInjector();
        RouteDefinition route = fromWithStandardExceptionHandling(uri);
        route
                .process(sourceSystemDetailInjector)
                .process(nodeDetailInjector)
        ;
        return route;
    }

    protected String getSourceSystemName() {
        String sourceSystemName = getIngresEndpoint().getEndpointTopologyNode().getConnectedSystemName();
        return (sourceSystemName);
    }

    protected class SourceSystemDetailInjector implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            getLogger().debug("SourceSystemDetailInjector.process(): Entry");
            boolean alreadyInPlace = false;
            if (exchange.hasProperties()) {
                String sourceSystem = exchange.getProperty(PetasosPropertyConstants.WUP_INTERACT_INGRES_SOURCE_SYSTEM_NAME, String.class);
                if (sourceSystem != null) {
                    alreadyInPlace = true;
                }
            }
            if (!alreadyInPlace) {
                exchange.setProperty(PetasosPropertyConstants.WUP_INTERACT_INGRES_SOURCE_SYSTEM_NAME, getSourceSystemName());
            }
        }
    }
}
