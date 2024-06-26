/*
 * Copyright (c) 2020 MAHun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of subscription software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and subscription permission notice shall be included in all
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
package net.fhirfactory.dricats.core.tasks.management.routing;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.core.tasks.management.common.TaskDistributionDecisionEngineBase;
import net.fhirfactory.dricats.core.participants.cache.LocalPetasosParticipantCacheDM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class LocalTaskDistributionDecisionEngine extends TaskDistributionDecisionEngineBase {
    private static final Logger LOG = LoggerFactory.getLogger(LocalTaskDistributionDecisionEngine.class);

    @Inject
    private LocalPetasosParticipantCacheDM localPetasosParticipantCacheDM;


    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected LocalPetasosParticipantCacheDM getLocalPetasosParticipantCacheDM() {
        return (localPetasosParticipantCacheDM);
    }

    //
    // More sophisticated SubscriberList derivation
    //

    public boolean hasRemoteServiceName(PetasosParticipant subscriber) {
        getLogger().debug(".hasRemoteServiceName(): Entry, subscriber->{}", subscriber.getSubsystemParticipantName());
        if (subscriber == null) {
            return (false);
        }
        if (StringUtils.isEmpty(subscriber.getSubsystemParticipantName())) {
            return (false);
        }
        getLogger().debug(".hasRemoteServiceName(): Entry, processingPlant.getSubsystemParticipantName()->{}", getProcessingPlant().getSubsystemParticipantName());
        if (subscriber.getSubsystemParticipantName().contentEquals(getProcessingPlant().getSubsystemParticipantName())) {
            return (false);
        }
        return (true);
    }

    public boolean hasIntendedTarget(DataParcelManifest parcelManifest) {
        if (parcelManifest == null) {
            return (false);
        }
        if (StringUtils.isEmpty(parcelManifest.getIntendedTargetSystem())) {
            return (false);
        }
        return (true);
    }

    public boolean hasAtLeastOneSubscriber(DataParcelManifest parcelManifest) {
        if (parcelManifest == null) {
            return (false);
        }
        if (deriveSubscriberList(parcelManifest).isEmpty()) {
            return (false);
        }
        return (true);
    }

    public List<PetasosParticipant> deriveSubscriberList(DataParcelManifest parcelManifest) {
        getLogger().debug(".deriveSubscriberList(): Entry, parcelManifest->{}", parcelManifest);
        if (getLogger().isDebugEnabled()) {
            if (parcelManifest.hasContentDescriptor()) {
                String messageToken = parcelManifest.getContentDescriptor().toFDN().getToken().toTag();
                getLogger().debug(".deriveSubscriberList(): parcel.ContentDescriptor->{}", messageToken);
            }
        }
        List<PetasosParticipant> subscriberList = new ArrayList<>();

        Set<PetasosParticipant> participants = getLocalPetasosParticipantCacheDM().getAllPetasosParticipants();

        List<String> alreadySubscribedSubsystemParticipants = new ArrayList<>();

        for (PetasosParticipant currentParticipant : participants) {
            getLogger().trace(".deriveSubscriberList(): Processing participant->{}/{}", currentParticipant.getParticipantName(), currentParticipant.getSubsystemParticipantName());
            for (TaskWorkItemSubscriptionType currentSubscription : currentParticipant.getSubscriptions()) {
                if (applySubscriptionFilter(currentSubscription, parcelManifest)) {
                    if (!subscriberList.contains(currentParticipant)) {
                        subscriberList.add(currentParticipant);
                        getLogger().debug(".deriveSubscriberList(): Adding.... ");
                    }
                    if (StringUtils.isNotEmpty(currentParticipant.getSubsystemParticipantName())) {
                        alreadySubscribedSubsystemParticipants.add(currentParticipant.getSubsystemParticipantName());
                    }
                    break;
                }
            }
        }

        getLogger().debug(".getSubscriberList(): Exit!");
        return (subscriberList);
    }

    protected boolean isRemoteParticipant(PetasosParticipant participant) {
        if (participant == null) {
            return (false);
        }
        if (StringUtils.isEmpty(participant.getSubsystemParticipantName())) {
            return (false);
        }
        if (getProcessingPlant().getSubsystemParticipantName().contentEquals(participant.getSubsystemParticipantName())) {
            return (false);
        }
        return (true);
    }

    //
    // Filter Implementation
    //

    public boolean applySubscriptionFilter(TaskWorkItemSubscriptionType subscription, DataParcelManifest testManifest) {
        getLogger().trace(".applySubscriptionFilter(): Entry, subscription->{}, testManifest->{}", subscription, testManifest);

        boolean containerIsEqual = containerDescriptorIsEqual(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: containerIsEqual->{}", containerIsEqual);

        boolean contentIsEqual = contentDescriptorIsEqual(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: contentIsEqual->{}", contentIsEqual);

        boolean containerOnlyIsEqual = containerDescriptorOnlyEqual(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: containerOnlyIsEqual->{}", containerOnlyIsEqual);

        boolean matchedNormalisation = normalisationMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedNormalisation->{}", matchedNormalisation);

        boolean matchedValidation = validationMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedValidation->{}", matchedValidation);

        boolean matchedManifestType = manifestTypeMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedManifestType->{}", matchedManifestType);

        boolean matchedSource = sourceSystemMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedSource->{}", matchedSource);

        boolean matchedTarget = targetSystemMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedTarget->{}", matchedTarget);

        boolean matchedPEPStatus = enforcementPointApprovalStatusMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedPEPStatus->{}", matchedPEPStatus);

        boolean matchedDistributionStatus = isDistributableMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedDistributionStatus->{}", matchedDistributionStatus);

        boolean matchedDirection = parcelFlowDirectionMatches(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: matchedDirection->{}", matchedDirection);

        boolean matchedOrigin = originParticipantFilter(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: originParticipant->{}", matchedOrigin);

        boolean matchedPrevious = previousParticipantFiler(testManifest, subscription);
        getLogger().trace(".applySubscriptionFilter(): Checking for equivalence/match: previousParticipant->{}", matchedPrevious);

        boolean goodEnoughMatch = containerIsEqual
                && contentIsEqual
                && matchedNormalisation
                && matchedValidation
                && matchedManifestType
                && matchedSource
                && matchedTarget
                && matchedPEPStatus
                && matchedDirection
                && matchedDistributionStatus
                && matchedOrigin
                && matchedPrevious;
        getLogger().trace(".filter(): Checking for equivalence/match: goodEnoughMatch->{}", goodEnoughMatch);

        boolean containerBasedOKMatch = containerOnlyIsEqual
                && matchedNormalisation
                && matchedValidation
                && matchedManifestType
                && matchedSource
                && matchedTarget
                && matchedPEPStatus
                && matchedDirection
                && matchedDistributionStatus;
        getLogger().trace(".filter(): Checking for equivalence/match: containerBasedOKMatch->{}", containerBasedOKMatch);

        boolean passesFilter = goodEnoughMatch || containerBasedOKMatch;
        getLogger().trace(".filter(): Exit, passesFilter->{}", passesFilter);
        return (passesFilter);
    }
}
