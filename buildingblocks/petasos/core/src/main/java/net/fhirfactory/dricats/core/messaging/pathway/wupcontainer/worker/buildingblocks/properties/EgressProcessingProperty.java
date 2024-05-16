package net.fhirfactory.dricats.core.messaging.pathway.wupcontainer.worker.buildingblocks.properties;

import net.fhirfactory.dricats.core.model.generalid.FDNToken;

public class EgressProcessingProperty {
    private boolean hasBeenProcessed;
    private FDNToken instanceName;

    public boolean isHasBeenProcessed() {
        return hasBeenProcessed;
    }

    public void setHasBeenProcessed(boolean hasBeenProcessed) {
        this.hasBeenProcessed = hasBeenProcessed;
    }

    public FDNToken getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(FDNToken instanceName) {
        this.instanceName = instanceName;
    }
}