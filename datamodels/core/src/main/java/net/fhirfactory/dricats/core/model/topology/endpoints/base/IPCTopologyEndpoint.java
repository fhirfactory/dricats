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
package net.fhirfactory.dricats.core.model.topology.endpoints.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.endpoint.PetasosEndpoint;
import net.fhirfactory.dricats.core.model.topology.endpoints.adapters.base.IPCAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IPCTopologyEndpoint extends PetasosEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(IPCTopologyEndpoint.class);


    private String connectedSystemName;
    private boolean server;
    private ComponentIdType implementingWUP;
    private List<IPCAdapter> adapterList;
    private String endpointConfigurationName;

    //
    // Constructor(s)
    //

    public IPCTopologyEndpoint() {
        super();

        this.connectedSystemName = null;
        this.server = true;
        this.implementingWUP = null;
        this.adapterList = new ArrayList<>();
        this.endpointConfigurationName = null;
    }

    public IPCTopologyEndpoint(IPCTopologyEndpoint ori) {
        super(ori);
        this.adapterList = new ArrayList<>();
        if (ori.hasConnectedSystemName()) {
            setConnectedSystemName(ori.getConnectedSystemName());
        }
        if (ori.hasEndpointConfigurationName()) {
            setEndpointConfigurationName(ori.getEndpointConfigurationName());
        }
        if (ori.hasImplementingWUP()) {
            setImplementingWUP(ori.getImplementingWUP());
        }
        setServer(ori.isServer());
        if (!adapterList.isEmpty()) {
            this.adapterList.addAll(ori.getAdapterList());
        }
    }

    //
    // Getters and Setters
    //

    @JsonIgnore
    public boolean hasEndpointConfigurationName() {
        boolean hasValue = this.endpointConfigurationName != null;
        return (hasValue);
    }

    public String getEndpointConfigurationName() {
        return endpointConfigurationName;
    }

    public void setEndpointConfigurationName(String endpointConfigurationName) {
        this.endpointConfigurationName = endpointConfigurationName;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    @JsonIgnore
    public boolean hasImplementingWUP() {
        boolean hasValue = this.implementingWUP != null;
        return (hasValue);
    }

    public ComponentIdType getImplementingWUP() {
        return implementingWUP;
    }

    public void setImplementingWUP(ComponentIdType implementingWUP) {
        this.implementingWUP = implementingWUP;
    }

    @JsonIgnore
    public boolean hasConnectedSystemName() {
        boolean hasValue = this.connectedSystemName != null;
        return (hasValue);
    }

    public String getConnectedSystemName() {
        return connectedSystemName;
    }

    public void setConnectedSystemName(String connectedSystemName) {
        this.connectedSystemName = connectedSystemName;
    }


    public List<IPCAdapter> getAdapterList() {
        return adapterList;
    }

    public void setAdapterList(List<IPCAdapter> adapterList) {
        this.adapterList = adapterList;
    }

    @JsonIgnore
    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    //
    // to String
    //

    @Override
    public String toString() {
        return "IPCTopologyEndpoint{" +
                "componentFDN=" + getComponentFDN() +
                ", kubernetesDeployed=" + isKubernetesDeployed() +
                ", otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", concurrencyMode=" + getConcurrencyMode() +
                ", resilienceMode=" + getResilienceMode() +
                ", securityZone=" + getSecurityZone() +
                ", componentID=" + getComponentID() +
                ", nodeFunctionFDN=" + getNodeFunctionFDN() +
                ", componentType=" + getComponentType() +
                ", containingNodeFDN=" + getContainingNodeFDN() +
                ", componentRDN=" + getComponentRDN() +
                ", metrics=" + getMetrics() +
                ", componentSystemRole=" + getComponentSystemRole() +
                ", endpointType=" + getEndpointType() +
                ", connectedSystemName=" + connectedSystemName +
                ", implementingWUP=" + implementingWUP +
                ", server=" + isServer() +
                ", adapterList=" + adapterList +
                ", endpointConfigurationName=" + endpointConfigurationName +
                '}';
    }
}
