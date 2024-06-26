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
package net.fhirfactory.dricats.platform.edge.ask.base;

import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.messaging.wup.MessageBasedWUPEndpointContainer;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFDN;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.topology.endpoints.base.IPCTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.interact.ExternalSystemIPCAdapter;
import net.fhirfactory.dricats.core.model.topology.endpoints.interact.StandardInteractClientTopologyEndpointPort;
import net.fhirfactory.dricats.core.model.topology.nodes.external.ConnectedExternalSystemTopologyNode;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.platform.edge.ask.base.http.InternalFHIRClientProxy;

import javax.inject.Inject;
import java.util.ArrayList;

public abstract class InternalFHIRClientServicesBase extends InternalFHIRClientProxy {

    private IParser fhirParser;

    @Inject
    private TopologyIM topologyIM;

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    @Inject
    private ProcessingPlantInterface processingPlant;

    @Inject
    private PegacornCommonInterfaceNames interfaceNames;

    //
    // Constructor(s)
    //

    public InternalFHIRClientServicesBase() {
        super();
    }

    //
    // Post Construct
    //

    protected void postConstructActivities() {
        fhirParser = getFHIRContextUtility().getJsonParser().setPrettyPrint(true);
    }

    //
    // Getters (and Setters)
    //

    protected IParser getFHIRParser() {
        return (fhirParser);
    }

    //
    // Business Methods
    //


    @Override
    protected String deriveTargetEndpointDetails() {
        getLogger().debug(".deriveTargetEndpointDetails(): Entry");
        MessageBasedWUPEndpointContainer endpoint = new MessageBasedWUPEndpointContainer();
        StandardInteractClientTopologyEndpointPort clientTopologyEndpoint = (StandardInteractClientTopologyEndpointPort) getTopologyEndpoint(interfaceNames.getEdgeAskEndpointName());
        ConnectedExternalSystemTopologyNode targetSystem = clientTopologyEndpoint.getTargetSystem();
        ExternalSystemIPCAdapter externalSystemIPCAdapter = (ExternalSystemIPCAdapter) targetSystem.getTargetPorts().get(0);
        String http_type = null;
        if (externalSystemIPCAdapter.isEncrypted()) {
            http_type = "https";
        } else {
            http_type = "http";
        }
        String dnsName = externalSystemIPCAdapter.getTargetPortDNSName();
        String portNumber = Integer.toString(externalSystemIPCAdapter.getTargetPortValue());
        String endpointDetails = http_type + "://" + dnsName + ":" + portNumber + systemWideProperties.getPegacornInternalFhirResourceR4Path();
        getLogger().info(".deriveTargetEndpointDetails(): Exit, endpointDetails --> {}", endpointDetails);
        return (endpointDetails);
    }

    protected IPCTopologyEndpoint getTopologyEndpoint(String topologyEndpointName) {
        getLogger().debug(".getTopologyEndpoint(): Entry, topologyEndpointName->{}", topologyEndpointName);
        ArrayList<TopologyNodeFDN> endpointFDNs = processingPlant.getMeAsASoftwareComponent().getEndpoints();
        for (TopologyNodeFDN currentEndpointFDN : endpointFDNs) {
            IPCTopologyEndpoint endpointTopologyNode = (IPCTopologyEndpoint) topologyIM.getNode(currentEndpointFDN);
            if (endpointTopologyNode.getEndpointConfigurationName().contentEquals(topologyEndpointName)) {
                getLogger().debug(".getTopologyEndpoint(): Exit, node found -->{}", endpointTopologyNode);
                return (endpointTopologyNode);
            }
        }
        getLogger().debug(".getTopologyEndpoint(): Exit, Could not find node!");
        return (null);
    }
}
