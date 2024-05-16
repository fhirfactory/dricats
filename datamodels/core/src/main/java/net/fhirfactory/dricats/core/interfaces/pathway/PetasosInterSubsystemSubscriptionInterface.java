package net.fhirfactory.dricats.core.interfaces.pathway;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;

import java.util.Set;

public interface PetasosInterSubsystemSubscriptionInterface {
    public void subscribeToRemoteSubsystems(Set<DataParcelManifest> subscriptionSet);
}
