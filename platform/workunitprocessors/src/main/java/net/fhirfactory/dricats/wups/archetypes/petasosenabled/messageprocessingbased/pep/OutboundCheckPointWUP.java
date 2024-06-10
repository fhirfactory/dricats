/*
 * Copyright (c) 2021 Mark A. Hunter
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
/*
 * Copyright (c) 2021 Mark A. Hunter
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
package net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased.pep;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.interfaces.pathway.PetasosInterSubsystemSubscriptionInterface;
import net.fhirfactory.dricats.core.interfaces.topology.WorkshopInterface;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelDirectionEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.core.participants.cache.DistributedTaskSubscriptionMap;
import net.fhirfactory.dricats.core.participants.manager.LocalParticipantManager;
import net.fhirfactory.dricats.workshops.PolicyEnforcementWorkshop;
import net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased.MOAStandardWUP;
import net.fhirfactory.dricats.wups.archetypes.petasosenabled.messageprocessingbased.pep.beans.CoreDistributionPolicyEnforcementPoint;

import javax.inject.Inject;
import java.util.*;

public abstract class OutboundCheckPointWUP extends MOAStandardWUP implements PetasosInterSubsystemSubscriptionInterface {

    private static final int REMOTE_SUBSCRIPTION_DAEMON_MAXIMUM_EXECUTION_COUNT = 10;
    private static final Long REMOTE_SUBSCRIPTION_DAEMON_STARTUP_DELAY = 45000L;
    private static final Long REMOTE_SUBSCRIPTION_DAEMON_CHECK_PERIOD = 10000L;
    private int startupDaemonExecutionCount;
    @Inject
    private PolicyEnforcementWorkshop policyEnforcementWorkshop;

    @Inject
    private LocalParticipantManager localParticipantManager;

    @Inject
    private PegacornReferenceProperties referenceProperties;

    @Inject
    private DistributedTaskSubscriptionMap distributedTaskSubscriptionMap;

    //
    // Constructor(s)
    //

    public OutboundCheckPointWUP() {
        super();
        this.startupDaemonExecutionCount = 0;
    }

    //
    // Post Construct
    //

    @Override
    protected void executePostInitialisationActivities() {
        scheduleRemoteContentSubscriptionDaemon();
    }

    //
    // Remote Tasks Subscription Daemon
    //

    private void scheduleRemoteContentSubscriptionDaemon() {
        getLogger().debug(".scheduleRemoteContentSubscriptionDaemon(): Entry");
        Timer timer = new Timer("RemoteSubscriptionDaemonTimer");
        TimerTask remoteSubscriptionDaemonTimerTask = new TimerTask() {
            public void run() {
                getLogger().debug(".remoteSubscriptionDaemonTimerTask(): Entry");
                if (startupDaemonExecutionCount < REMOTE_SUBSCRIPTION_DAEMON_MAXIMUM_EXECUTION_COUNT) {
                    remoteContentSubscriptionDaemon();
                    startupDaemonExecutionCount++;
                } else {
                    timer.cancel();
                }
                getLogger().debug(".remoteSubscriptionDaemonTimerTask(): Exit");
            }
        };
        timer.schedule(remoteSubscriptionDaemonTimerTask, REMOTE_SUBSCRIPTION_DAEMON_STARTUP_DELAY, REMOTE_SUBSCRIPTION_DAEMON_CHECK_PERIOD);
        getLogger().debug(".scheduleRemoteContentSubscriptionDaemon(): Exit");
    }

    private void remoteContentSubscriptionDaemon() {
        PetasosParticipant participant = getMeAsAPetasosParticipant();
        List<TaskWorkItemSubscriptionType> subscriptions = distributedTaskSubscriptionMap.getSubscriptions();
        for (TaskWorkItemSubscriptionType currentSubscription : subscriptions) {
            currentSubscription.setDataParcelFlowDirection(DataParcelDirectionEnum.INFORMATION_FLOW_CORE_DISTRIBUTION);
            if (!participant.getSubscriptions().contains(currentSubscription)) {
                participant.getSubscriptions().add(currentSubscription);
            }
        }
        getLocalParticipantManager().updatePetasosParticipant(participant);
    }

    //
    // Business Methods
    //

    @Override
    protected List<DataParcelManifest> specifySubscriptionTopics() {
        getLogger().debug(".specifySubscriptionTopics(): Entry");
        List<DataParcelManifest> subscriptionList = new ArrayList<>();
        getLogger().debug(".specifySubscriptionTopics(): Exit");
        return (subscriptionList);
    }

    @Override
    public void subscribeToRemoteSubsystems(Set<DataParcelManifest> subscriptionSet) {
        getLogger().debug(".specifySubscriptionTopics(): Entry");
        if (subscriptionSet == null || subscriptionSet.isEmpty()) {
            getLogger().debug(".specifySubscriptionTopics(): Exit, subscriptionSet is null or empty, ");
            return;
        }

        PetasosParticipant participant = getMeAsAPetasosParticipant();
        for (DataParcelManifest currentSubscription : subscriptionSet) {
            currentSubscription.setDataParcelFlowDirection(DataParcelDirectionEnum.INFORMATION_FLOW_CORE_DISTRIBUTION);
            if (!participant.getSubscriptions().contains(currentSubscription)) {
                TaskWorkItemSubscriptionType workItemSubscription = new TaskWorkItemSubscriptionType(currentSubscription);
                participant.getSubscriptions().add(workItemSubscription);
            }
        }

        getLocalParticipantManager().updatePetasosParticipant(participant);
    }

    @Override
    public void configure() throws Exception {
        getLogger().info("{}:: ingresFeed() --> {}", getClass().getName(), ingresFeed());
        getLogger().info("{}:: egressFeed() --> {}", getClass().getName(), egressFeed());

        fromIncludingPetasosServices(ingresFeed())
                .routeId(getNameSet().getRouteCoreWUP())
                .bean(CoreDistributionPolicyEnforcementPoint.class, "enforceOutboundPolicy")
                .to(egressFeed());
    }


    @Override
    protected String specifyWUPInstanceName() {
        return (getClass().getSimpleName());
    }

    @Override
    protected WorkshopInterface specifyWorkshop() {
        return (policyEnforcementWorkshop);
    }

    @Override
    protected String specifyParticipantDisplayName() {
        return (PetasosPropertyConstants.OUTBOUND_CHECKPOINT_WUP_NAME);
    }

    @Override
    protected List<DataParcelManifest> declarePublishedTopics() {
        return (new ArrayList<>());
    }

    protected LocalParticipantManager getLocalParticipantManager() {
        return (localParticipantManager);
    }
}
