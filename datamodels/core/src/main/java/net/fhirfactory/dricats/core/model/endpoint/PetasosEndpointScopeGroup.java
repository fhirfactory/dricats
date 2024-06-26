package net.fhirfactory.dricats.core.model.endpoint;

import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointChannelScopeEnum;

import java.io.Serializable;

public class PetasosEndpointScopeGroup implements Serializable {
    PetasosEndpointChannelScopeEnum endpointScope;
    PetasosEndpoint ipcEndpoint;
    PetasosEndpoint oamDiscoveryEndpoint;
    PetasosEndpoint oamPubSubEndpoint;

    public PetasosEndpointChannelScopeEnum getEndpointScope() {
        return endpointScope;
    }

    public void setEndpointScope(PetasosEndpointChannelScopeEnum endpointScope) {
        this.endpointScope = endpointScope;
    }

    public PetasosEndpoint getIpcEndpoint() {
        return ipcEndpoint;
    }

    public void setIpcEndpoint(PetasosEndpoint ipcEndpoint) {
        this.ipcEndpoint = ipcEndpoint;
    }

    public PetasosEndpoint getOamDiscoveryEndpoint() {
        return oamDiscoveryEndpoint;
    }

    public void setOamDiscoveryEndpoint(PetasosEndpoint oamDiscoveryEndpoint) {
        this.oamDiscoveryEndpoint = oamDiscoveryEndpoint;
    }

    public PetasosEndpoint getOamPubSubEndpoint() {
        return oamPubSubEndpoint;
    }

    public void setOamPubSubEndpoint(PetasosEndpoint oamPubSubEndpoint) {
        this.oamPubSubEndpoint = oamPubSubEndpoint;
    }

    @Override
    public String toString() {
        return "PetasosEndpointScopeGroup{" +
                "endpointScope=" + endpointScope +
                ", ipcEndpoint=" + ipcEndpoint +
                ", oamDiscoveryEndpoint=" + oamDiscoveryEndpoint +
                ", oamPubSubEndpoint=" + oamPubSubEndpoint +
                '}';
    }
}
