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
package net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.routing;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelDirectionEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantRegistration;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.core.tasks.management.common.TaskDistributionDecisionEngineBase;
import net.fhirfactory.dricats.ponos.workshops.datagrid.cache.ParticipantCacheServices;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class GlobalTaskDistributionDecisionEngine extends TaskDistributionDecisionEngineBase {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalTaskDistributionDecisionEngine.class);

    @Inject
    private ParticipantCacheServices participantCache;


    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    protected ParticipantCacheServices getParticipantCache() {
        return (participantCache);
    }


    //
    // More sophisticated SubscriberList derivation
    //


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
        if (getLogger().isTraceEnabled()) {
            if (parcelManifest.hasContentDescriptor()) {
                String messageToken = parcelManifest.getContentDescriptor().toFDN().getToken().toTag();
                getLogger().trace(".deriveSubscriberList(): parcel.ContentDescriptor->{}", messageToken);
            }
        }
        List<PetasosParticipant> subscriberList = new ArrayList<>();

        Set<PetasosParticipantRegistration> participants = getParticipantCache().getAllRegistrations();
        getLogger().trace(".deriveSubscriberList(): participants.size()->{}", participants.size());

        for (PetasosParticipantRegistration currentParticipantRegistration : participants) {
            getLogger().trace(".deriveSubscriberList(): Processing participant->{}/{}", currentParticipantRegistration.getParticipant().getParticipantName(), currentParticipantRegistration.getParticipant().getSubsystemParticipantName());
            for (TaskWorkItemSubscriptionType currentSubscription : currentParticipantRegistration.getParticipant().getSubscriptions()) {
                if (applySubscriptionFilter(currentSubscription, parcelManifest)) {
                    if (!subscriberList.contains(currentParticipantRegistration)) {
                        subscriberList.add(currentParticipantRegistration.getParticipant());
                        getLogger().trace(".deriveSubscriberList(): Adding.... ");
                    }
                    break;
                }
            }
        }

        getLogger().debug(".deriveSubscriberList(): Exit!");
        return (subscriberList);
    }

    protected boolean isRemoteParticipant(PetasosParticipant participant) {
        if (participant == null) {
            return (false);
        }
        if (StringUtils.isEmpty(participant.getSubsystemParticipantName())) {
            return (false);
        }
        if (processingPlant.getSubsystemParticipantName().contentEquals(participant.getSubsystemParticipantName())) {
            return (false);
        }
        return (true);
    }

    //
    // Filter Implementation
    //

    public boolean applySubscriptionFilter(TaskWorkItemSubscriptionType subscription, DataParcelManifest testManifest) {
        getLogger().debug(".applySubscriptionFilter(): Entry, subscription->{}, testManifest->{}", subscription, testManifest);

        if (!subscription.getDataParcelFlowDirection().equals(DataParcelDirectionEnum.INFORMATION_FLOW_CORE_DISTRIBUTION)) {
            getLogger().debug(".applySubscriptionFilter(): Not a Core-Distribution Subscription, exit");
            return (false);
        }

        if (!testManifest.getDataParcelFlowDirection().equals(DataParcelDirectionEnum.INFORMATION_FLOW_CORE_DISTRIBUTION)) {
            getLogger().debug(".applySubscriptionFilter(): Not a Core-Distribution Task, exit");
            return (false);
        }

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
                && matchedDistributionStatus;
        getLogger().trace(".filter(): Checking for equivalence/match: containerBasedOKMatch->{}", containerBasedOKMatch);

        boolean passesFilter = goodEnoughMatch || containerBasedOKMatch;
        getLogger().trace(".filter(): Exit, passesFilter->{}", passesFilter);
        return (passesFilter);
    }
}
