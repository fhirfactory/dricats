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
package net.fhirfactory.dricats.core.tasks.management.local.router.daemons;

import net.fhirfactory.dricats.core.interfaces.tasks.PetasosTaskLifetimeExtensionInterface;
import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.PetasosFulfillmentTask;
import net.fhirfactory.dricats.core.model.task.PetasosTask;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.wup.PetasosTaskJobCard;
import net.fhirfactory.dricats.core.tasks.management.daemon.DaemonBase;
import net.fhirfactory.dricats.core.tasks.caches.processingplant.LocalFulfillmentTaskCache;
import net.fhirfactory.dricats.core.tasks.caches.shared.ParticipantSharedActionableTaskCache;
import net.fhirfactory.dricats.core.tasks.caches.shared.ParticipantSharedTaskJobCardCache;
import net.fhirfactory.dricats.core.oam.metrics.collectors.ProcessingPlantMetricsAgentAccessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
public class LocalPetasosTaskCleanupWatchdog extends DaemonBase implements PetasosTaskLifetimeExtensionInterface {
    private static final Logger LOG = LoggerFactory.getLogger(LocalPetasosTaskCleanupWatchdog.class);

    private Long minimumAgeForTaskRetirement;

    private Long TASK_CLEANUP_CHECK_INITIAL_DELAY = 60000L; // milliseconds
    private Long TASK_CLEANUP_CHECK_PERIOD = 15000L; // milliseconds
    private Long MINIMUM_TASK_AGE_FOR_RETIREMENT = 600L; // Seconds

    private Instant actionableTaskCheckInstant;
    private Instant taskJobCardCheckInstant;
    private Instant fulfillmentTaskCheckInstant;

    private boolean initialised;

    @Inject
    private ParticipantSharedActionableTaskCache actionableTaskDM;

    @Inject
    private ParticipantSharedTaskJobCardCache taskJobCardDM;

    @Inject
    private ProcessingPlantMetricsAgentAccessor processingPlantMetricsAgentAccessor;

    @Inject
    private LocalFulfillmentTaskCache fulfillmentTaskCache;

    @Inject
    private ProcessingPlantInterface processingPlant;

    //
    // Constructor(s)
    //

    public LocalPetasosTaskCleanupWatchdog() {
        this.actionableTaskCheckInstant = Instant.EPOCH;
        this.taskJobCardCheckInstant = Instant.EPOCH;
        this.fulfillmentTaskCheckInstant = Instant.EPOCH;
        this.initialised = false;
        this.minimumAgeForTaskRetirement = MINIMUM_TASK_AGE_FOR_RETIREMENT;
    }

    //
    // Post Construct
    //

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initialise(): Entry");
        if (this.initialised) {
            getLogger().debug(".initialise(): Exit, already initialised, nothing to do!");
            return;
        } else {
            getLogger().info("GlobalPetasosTaskContinuityWatchdog::initialise(): Starting initialisation");
            String minimum_task_age_for_retirement = processingPlant.getMeAsASoftwareComponent().getOtherConfigurationParameter("MINIMUM_TASK_AGE_FOR_RETIREMENT");
            if (StringUtils.isNotEmpty(minimum_task_age_for_retirement)) {
                try {
                    minimumAgeForTaskRetirement = Long.valueOf(minimum_task_age_for_retirement);
                } catch (Exception ex) {
                    getLogger().debug(".initialise(): Unable to resolve age for retirement, using default. Error->", ex);
                }
            }
            scheduleActionableTaskCacheWatchdog();
            scheduleFulfillmentTaskCacheWatchdog();
            scheduleJobCardCacheWatchdog();
            getLogger().info("GlobalPetasosTaskContinuityWatchdog::initialise(): Finished initialisation");
            this.initialised = true;
            getLogger().debug(".initialise(): Exit");
        }
    }

    //
    // Scheduling & Initialisation
    //

    public void scheduleActionableTaskCacheWatchdog() {
        getLogger().debug(".scheduleActionableTaskCacheWatchdog(): Entry");
        TimerTask actionableTaskCleanupActivity = new TimerTask() {
            public void run() {
                getLogger().debug(".actionableTaskCleanupActivity(): Entry");
                actionableTaskCleanup();
                getLogger().debug(".actionableTaskCleanupActivity(): Exit");
            }
        };
        Timer timer = new Timer("actionableTaskCleanupActivityTimer");
        timer.schedule(actionableTaskCleanupActivity, TASK_CLEANUP_CHECK_INITIAL_DELAY, TASK_CLEANUP_CHECK_PERIOD);
        getLogger().debug(".scheduleActionableTaskCacheWatchdog(): Exit");
    }

    public void scheduleFulfillmentTaskCacheWatchdog() {
        getLogger().debug(".scheduleFulfillmentTaskCacheWatchdog(): Entry");
        TimerTask fulfillmentTaskCleanupActivity = new TimerTask() {
            public void run() {
                getLogger().debug(".fulfillmentTaskCleanupActivity(): Entry");
                fulfillmentTaskCleanup();
                getLogger().debug(".fulfillmentTaskCleanupActivity(): Exit");
            }
        };
        Timer timer = new Timer("fulfillmentTaskCleanupActivityTimer");
        timer.schedule(fulfillmentTaskCleanupActivity, TASK_CLEANUP_CHECK_INITIAL_DELAY, TASK_CLEANUP_CHECK_PERIOD);
        getLogger().debug(".scheduleFulfillmentTaskCacheWatchdog(): Exit");
    }

    public void scheduleJobCardCacheWatchdog() {
        getLogger().debug(".scheduleJobCardCacheWatchdog(): Entry");
        TimerTask jobCardCleanupActivity = new TimerTask() {
            public void run() {
                getLogger().debug(".jobCardCleanupActivity(): Entry");
                jobCardCleanup();
                getLogger().debug(".jobCardCleanupActivity(): Exit");
            }
        };
        Timer timer = new Timer("JobCardTaskCleanupActivityTimer");
        timer.schedule(jobCardCleanupActivity, TASK_CLEANUP_CHECK_INITIAL_DELAY, TASK_CLEANUP_CHECK_PERIOD);
        getLogger().debug(".scheduleJobCardCacheWatchdog(): Exit");
    }

    //
    // Actionable Task Controller / Watchdog
    //

    protected void actionableTaskCleanup() {
        getLogger().debug(".actionableTaskCleanup(): Entry");
        Set<String> allTaskIds = getActionableTaskDM().getAllTaskIds();
        processingPlantMetricsAgentAccessor.getMetricsAgent().updateLocalCacheStatus("ActionableTaskCacheSharedCache", allTaskIds.size());
        for (String currentTaskId : allTaskIds) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(".actionableTaskCleanup(): Checking task {}", currentTaskId);
            }
            boolean unregisterTask = false;
            synchronized (actionableTaskDM.getTaskLock(currentTaskId)) {
                PetasosActionableTask currentActionableTask = getActionableTaskDM().getTask(currentTaskId);
                if (currentActionableTask.getTaskCompletionSummary() != null) {
                    if (currentActionableTask.getTaskCompletionSummary().isFinalised()) {
                        unregisterTask = true;
                    }
                }
                if (!unregisterTask) {
                    if (currentActionableTask.getTaskFulfillment() != null) {
                        if (currentActionableTask.getTaskFulfillment().hasStatus()) {
                            switch (currentActionableTask.getTaskFulfillment().getStatus()) {
                                case FULFILLMENT_EXECUTION_STATUS_UNREGISTERED:
                                case FULFILLMENT_EXECUTION_STATUS_REGISTERED:
                                case FULFILLMENT_EXECUTION_STATUS_INITIATED:
                                case FULFILLMENT_EXECUTION_STATUS_ACTIVE:
                                case FULFILLMENT_EXECUTION_STATUS_ACTIVE_ELSEWHERE:

                                    break;
                                case FULFILLMENT_EXECUTION_STATUS_CANCELLED:
                                case FULFILLMENT_EXECUTION_STATUS_NO_ACTION_REQUIRED:
                                case FULFILLMENT_EXECUTION_STATUS_FINISHED:
                                case FULFILLMENT_EXECUTION_STATUS_FAILED:
                                case FULFILLMENT_EXECUTION_STATUS_FINISHED_ELSEWHERE:
                                    if (currentActionableTask.getUpdateInstant() == null) {
                                        currentActionableTask.setUpdateInstant(currentActionableTask.getCreationInstant());
                                    }
                                    Long age = Instant.now().getEpochSecond() - currentActionableTask.getUpdateInstant().getEpochSecond();
                                    if (age > minimumAgeForTaskRetirement) {
                                        unregisterTask = true;
                                    }
                                    break;
                                case FULFILLMENT_EXECUTION_STATUS_FINALISED:
                                case FULFILLMENT_EXECUTION_STATUS_FINALISED_ELSEWHERE:
                                    unregisterTask = true;
                                    break;
                            }
                        }
                    }
                }
            }
            if (unregisterTask) {
                getLogger().debug(".actionableTaskCleanup(): Task {} is finalised, removing from shared cache... start", currentTaskId);
                PetasosActionableTask unregisteredActionableTask = actionableTaskDM.removeTask(currentTaskId);
                getLogger().debug(".actionableTaskCleanup(): Task {} is finalised, removing from shared cache... done...");
            }
        }
        // Some metrics for the ActionableTaskCache
        Integer taskCacheSize = getActionableTaskDM().getCacheSize();
        getLogger().debug(".actionableTaskCleanup(): ActionableTaskCacheSize->{}", taskCacheSize);
        processingPlantMetricsAgentAccessor.getMetricsAgent().updateLocalCacheStatus("SharedActionableTaskCache", taskCacheSize);
        processingPlantMetricsAgentAccessor.getMetricsAgent().touchWatchDogActivityIndicator("ActionableTaskWatchDog");
        getLogger().debug(".actionableTaskCleanup(): Exit");
    }

    //
    // Task Job Card Controller / Watchdog
    //

    protected void jobCardCleanup() {
        getLogger().debug(".jobCardCleanup(): Entry");
        List<String> allTaskIds = getTaskJobCardDM().getJobCardTaskIdList();
        int size = allTaskIds.size();
        Long nowInSeconds = Instant.now().getEpochSecond();
        for (String currentTaskId : allTaskIds) {
            if (getLogger().isInfoEnabled()) {
                getLogger().debug(".jobCardCleanup(): Checking task {}", currentTaskId);
            }
            PetasosTaskJobCard jobCard = getTaskJobCardDM().getJobCard(currentTaskId);
            if (jobCard != null) {
                Long age = nowInSeconds - jobCard.getCreationInstant().getEpochSecond();
                if (age > minimumAgeForTaskRetirement) {
                    taskJobCardDM.removeJobCard(jobCard);
                    size -= 1;
                }
            }
        }
        // Some metrics for the JobCardCache
        getLogger().debug(".jobCardCleanup(): jobCardCacheSize->{}", size);
        processingPlantMetricsAgentAccessor.getMetricsAgent().updateLocalCacheStatus("TaskJobCardCacheSize", size);
        processingPlantMetricsAgentAccessor.getMetricsAgent().touchWatchDogActivityIndicator("JobCardWatchDog");
        getLogger().debug(".jobCardCleanup(): Exit");
    }

    //
    // Fulfillment Task Controller / Watchdog
    //

    protected void fulfillmentTaskCleanup() {
        getLogger().debug(".fulfillmentTaskCleanup(): Entry");
        List<PetasosFulfillmentTask> allTasks = fulfillmentTaskCache.getFulfillmentTaskList();
        for (PetasosFulfillmentTask currentTask : allTasks) {
            if (getLogger().isInfoEnabled()) {
                getLogger().debug(".fulfillmentTaskCleanup(): Checking task {}", currentTask);
            }
            boolean unregisterTask = false;
            if (currentTask.getTaskFulfillment() != null) {
                if (currentTask.getTaskFulfillment().hasStatus()) {
                    switch (currentTask.getTaskFulfillment().getStatus()) {
                        case FULFILLMENT_EXECUTION_STATUS_UNREGISTERED:
                        case FULFILLMENT_EXECUTION_STATUS_REGISTERED:
                        case FULFILLMENT_EXECUTION_STATUS_INITIATED:
                        case FULFILLMENT_EXECUTION_STATUS_ACTIVE:
                        case FULFILLMENT_EXECUTION_STATUS_ACTIVE_ELSEWHERE:
                        case FULFILLMENT_EXECUTION_STATUS_CANCELLED:
                        case FULFILLMENT_EXECUTION_STATUS_NO_ACTION_REQUIRED:
                        case FULFILLMENT_EXECUTION_STATUS_FINISHED:
                        case FULFILLMENT_EXECUTION_STATUS_FAILED:
                        case FULFILLMENT_EXECUTION_STATUS_FINISHED_ELSEWHERE:
                            if (currentTask.getUpdateInstant() == null) {
                                currentTask.setUpdateInstant(currentTask.getCreationInstant());
                            }
                            Long age = Instant.now().getEpochSecond() - currentTask.getUpdateInstant().getEpochSecond();
                            if (age > minimumAgeForTaskRetirement) {
                                unregisterTask = true;
                            }
                            break;
                        case FULFILLMENT_EXECUTION_STATUS_FINALISED:
                        case FULFILLMENT_EXECUTION_STATUS_FINALISED_ELSEWHERE:
                            unregisterTask = true;
                            break;
                    }
                }
            }
            if (unregisterTask) {
                TaskIdType fulfillmentTaskId = currentTask.getTaskId();
                TaskIdType actionableTaskId = currentTask.getActionableTaskId();
                getLogger().debug(".fulfillmentTaskCleanup(): Task {} is finalised, removing from shared cache... start", fulfillmentTaskId);
                PetasosTask unregisteredTask = fulfillmentTaskCache.removeTask(fulfillmentTaskId);
                getLogger().debug(".fulfillmentTaskCleanup(): Task {} is finalised, removing from shared cache... done...", fulfillmentTaskId);
                if (actionableTaskId != null) {
                    PetasosTaskJobCard jobCard = taskJobCardDM.getJobCard(actionableTaskId);
                    if (jobCard != null) {
                        taskJobCardDM.removeJobCard(jobCard);
                    }
                }
            }
        }
        // Some metrics for the FulfillmentCache
        Integer taskCacheSize = fulfillmentTaskCache.getTaskCacheSize();
        getLogger().debug(".fulfillmentTaskCleanup(): fulfillmentTaskCacheSize->{}", taskCacheSize);
        processingPlantMetricsAgentAccessor.getMetricsAgent().updateLocalCacheStatus("LocalFulfillmentCache", taskCacheSize);
        processingPlantMetricsAgentAccessor.getMetricsAgent().touchWatchDogActivityIndicator("FulfillmentTaskWatchDog");
        getLogger().debug(".fulfillmentTaskCleanup(): Exit");
    }

    //
    // Watchdog Issue
    //

    @Override
    public void extendAllTaskLifetimes() {
        List<PetasosFulfillmentTask> allFulfillmentIds = fulfillmentTaskCache.getFulfillmentTaskList();
        for (PetasosFulfillmentTask currentTask : allFulfillmentIds) {
            currentTask.setUpdateInstant(Instant.now());
        }

        Set<String> allActionableTaskIds = getActionableTaskDM().getAllTaskIds();
        for (String currentTaskId : allActionableTaskIds) {
            Object taskLock = actionableTaskDM.getTaskLock(currentTaskId);
            if (taskLock != null) {
                synchronized (taskLock) {
                    PetasosActionableTask currentActionableTask = getActionableTaskDM().getTask(currentTaskId);
                    if (currentActionableTask != null) {
                        currentActionableTask.setUpdateInstant(Instant.now());
                    }
                }
            }
        }
    }

    //
    // Getters and Setters
    //

    public Instant getActionableTaskCheckInstant() {
        return actionableTaskCheckInstant;
    }

    public void setActionableTaskCheckInstant(Instant actionableTaskCheckInstant) {
        this.actionableTaskCheckInstant = actionableTaskCheckInstant;
    }

    public Instant getTaskJobCardCheckInstant() {
        return taskJobCardCheckInstant;
    }

    public void setTaskJobCardCheckInstant(Instant taskJobCardCheckInstant) {
        this.taskJobCardCheckInstant = taskJobCardCheckInstant;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    public ParticipantSharedActionableTaskCache getActionableTaskDM() {
        return actionableTaskDM;
    }

    public ParticipantSharedTaskJobCardCache getTaskJobCardDM() {
        return taskJobCardDM;
    }

    protected Logger getLogger() {
        return (LOG);
    }
}
