/*
 * The MIT License
 *
 * Copyright 2020 Mark A. Hunter.
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

package net.fhirfactory.dricats.deployment.topology.factories.archetypes;

import net.fhirfactory.dricats.core.model.topology.endpoints.interact.ClusteredInteractServerTopologyEndpointPort;
import net.fhirfactory.dricats.core.model.topology.endpoints.interact.StandardInteractClientTopologyEndpointPort;
import net.fhirfactory.dricats.core.model.topology.endpoints.mllp.MLLPClientEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.mllp.MLLPServerEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractClusteredServerPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.PetasosEnabledSubsystemTopologyFactory;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.endpoints.MLLPTopologyEndpointFactory;

import javax.inject.Inject;

/**
 * @author Mark A Hunter
 */
public abstract class MITaFSubsystemTopologyFactory extends PetasosEnabledSubsystemTopologyFactory {

    @Inject
    private MLLPTopologyEndpointFactory mllpTopologyEndpointFactory;

    //
    // Getters (and Setters)
    //

    protected MLLPTopologyEndpointFactory getMLLPTopologyEndpointFactory() {
        return (mllpTopologyEndpointFactory);
    }

    //
    // Build an MLLP Server Endpoint
    //

    protected ClusteredInteractServerTopologyEndpointPort newMLLPServerEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, InteractClusteredServerPortSegment mllpServerPort) {
        getLogger().debug(".createMLLPServerEndpoint(): Entry, endpointProvider->{}, mllpServerPort->{}", endpointProvider, mllpServerPort);
        if (mllpServerPort == null) {
            getLogger().debug(".createMLLPServerEndpoint(): Exit, no port to add");
            return (null);
        }
        MLLPServerEndpoint mllpServerEndpoint = getMLLPTopologyEndpointFactory().newMLLPServerEndpoint(getPropertyFile(), endpointProvider, endpointFunctionName, mllpServerPort);
        getLogger().info(".createMLLPServerEndpoint(): Exit, endpoint added->{}", mllpServerEndpoint);
        return (mllpServerEndpoint);
    }

    //
    // Build an MLLP Client Endpoint
    //

    protected StandardInteractClientTopologyEndpointPort newMLLPClientEndpoint(EndpointProviderInterface endpointProvider, String endpointFunctionName, InteractClientPortSegment mllpClientPort) {
        getLogger().debug(".newMLLPClientEndpoint(): Entry, endpointProvider->{}, mllpClientPort->{}", endpointProvider, mllpClientPort);
        if (mllpClientPort == null) {
            getLogger().debug(".newMLLPClientEndpoint(): Exit, no port to add");
            return (null);
        }
        MLLPClientEndpoint mllpClientEndpoint = getMLLPTopologyEndpointFactory().newMLLPClientEndpoint(getPropertyFile(), endpointProvider, endpointFunctionName, mllpClientPort);
        getLogger().debug(".newMLLPClientEndpoint(): Exit, endpoint added");
        return (mllpClientEndpoint);
    }
}
