package net.fhirfactory.dricats.core.interfaces.capabilities;

public interface CapabilityFulfillmentManagementInterface {
    public void registerCapabilityFulfillmentService(String capabilityName, CapabilityFulfillmentInterface fulfillmentInterface);
}
