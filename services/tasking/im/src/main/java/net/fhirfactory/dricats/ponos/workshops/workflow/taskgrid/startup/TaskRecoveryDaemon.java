/*
 * Copyright (c) 2022 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.startup;

import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import net.fhirfactory.dricats.core.model.task.queue.ParticipantTaskQueueEntry;
import net.fhirfactory.dricats.core.tasks.management.daemon.DaemonBase;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.ParticipantPersistenceServices;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.services.TaskRouterFHIRTaskService;
import net.fhirfactory.dricats.ponos.workshops.datagrid.queues.CentralTaskQueueMap;
import net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.status.TaskGridStatus;
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
public class TaskRecoveryDaemon extends DaemonBase {
    private static final Logger LOG = LoggerFactory.getLogger(TaskRecoveryDaemon.class);
    private static final Long TASK_RECOVERY_DAEMON_STARTUP_DELAY = 30000L;
    private static final Integer MAXIMUM_NUMBER_OF_TASKS_LOADED_PER_ITERATION = 100;
    private boolean startupDaemonCompleted;
    private Instant startupStartInstant;
    private Instant startupCompletionInstant;
    @Inject
    private TaskGridStatus taskGridStatus;

    @Inject
    private CentralTaskQueueMap taskQueueMap;

    @Inject
    private TaskRouterFHIRTaskService taskPersistenceService;

    @Inject
    private ParticipantPersistenceServices participantPersistenceServices;


    //
    // Constructor(s)
    //

    public TaskRecoveryDaemon() {
        this.startupDaemonCompleted = false;
        this.startupCompletionInstant = null;
        this.startupStartInstant = null;
    }

    //
    // Post Construct
    //

    @PostConstruct
    public void initialise() {

    }

    //
    // Getters (and Setters)
    //

    protected boolean isStartupDaemonCompleted() {
        return startupDaemonCompleted;
    }

    protected void setStartupDaemonCompleted(boolean startupDaemonCompleted) {
        this.startupDaemonCompleted = startupDaemonCompleted;
    }

    protected Instant getStartupStartInstant() {
        return startupStartInstant;
    }

    protected void setStartupStartInstant(Instant startupStartInstant) {
        this.startupStartInstant = startupStartInstant;
    }

    protected Instant getStartupCompletionInstant() {
        return startupCompletionInstant;
    }

    protected void setStartupCompletionInstant(Instant startupCompletionInstant) {
        this.startupCompletionInstant = startupCompletionInstant;
    }

    protected Logger getLogger() {
        return (LOG);
    }

    protected TaskGridStatus getTaskGridStatus() {
        return (taskGridStatus);
    }

    protected TaskRouterFHIRTaskService getTaskPersistenceService() {
        return (taskPersistenceService);
    }

    protected ParticipantPersistenceServices getParticipantPersistenceServices() {
        return (participantPersistenceServices);
    }

    protected CentralTaskQueueMap getTaskQueueMap() {
        return (taskQueueMap);
    }

    //
    // Startup Daemon
    //

    public void scheduleTaskRecoveryDaemon() {
        getLogger().debug(".scheduleTaskRecoveryDaemon(): Entry");
        TimerTask taskRecoveryDaemonTask = new TimerTask() {
            public void run() {
                getLogger().debug(".taskRecoveryDaemonTask(): Entry");
                taskRecoveryDaemon();
                getLogger().debug(".taskRecoveryDaemonTask(): Exit");
            }
        };
        Timer timer = new Timer("TaskRecoveryDaemonTimer");
        timer.schedule(taskRecoveryDaemonTask, TASK_RECOVERY_DAEMON_STARTUP_DELAY);
        getLogger().debug(".scheduleTaskRecoveryDaemon(): Exit");
    }

    public void taskRecoveryDaemon() {
        getLogger().debug(".taskRecoveryDaemon(): Entry");
        setStartupStartInstant(Instant.now());
        getTaskGridStatus().setStartupStartInstant(Instant.now());

        try {
            List<PetasosParticipant> participants = getParticipantPersistenceServices().loadAllParticipants();
            if (!participants.isEmpty()) {
                for (PetasosParticipant currentParticipant : participants) {
                    PetasosParticipantId currentParticipantId = currentParticipant.getParticipantId();
                    if (currentParticipantId != null) {
                        getLogger().info(".requeuePendingTasks(): [Re-Queueing Tasks for Participant->{}] Start", currentParticipantId.getName());
                        Boolean successRequeue = requeuePendingTasks(currentParticipantId);
                        if (successRequeue) {
                            getLogger().info(".requeuePendingTasks(): [Re-Queueing Tasks for Participant->{}] Finish, Success", currentParticipantId.getName());
                        } else {
                            getLogger().info(".requeuePendingTasks(): [Re-Queueing Tasks for Participant->{}] Finish, Failed", currentParticipantId.getName());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getLogger().warn(".taskRecoveryDaemon(): Problem ->", ex);
        }

        setStartupDaemonCompleted(true);
        setStartupCompletionInstant(Instant.now());
        getTaskGridStatus().setStartupComplete(true);
        getTaskGridStatus().setStartupCompletionInstant(Instant.now());
        getLogger().debug(".taskRecoveryDaemon(): Exit");
    }

    private boolean requeuePendingTasks(PetasosParticipantId participantId) {
        getLogger().debug(".requeuePendingTasks(): Entry, participantId->{}, participantResourceId->{}", participantId);
        if (participantId == null) {
            getLogger().debug(".requeuePendingTasks(): ParticipantId is null, returning -false-");
            return (false);
        }
        if (StringUtils.isEmpty(participantId.getName())) {
            getLogger().debug(".requeuePendingTasks(): ParticipantId.getName() is empty/null, returning -false-");
            return (false);
        }
        Set<ParticipantTaskQueueEntry> participantTaskQueueEntries = getTaskPersistenceService().loadPendingActionableTasks(participantId);
        if (participantTaskQueueEntries == null) {
            getLogger().warn(".requeuePendingTasks(): could not retrieve pending tasks - returning -false-");
            return (false);
        }
        int queuedTaskCount = 0;
        for (ParticipantTaskQueueEntry currentQueueEntry : participantTaskQueueEntries) {
            boolean taskQueued = getTaskQueueMap().addEntry(participantId.getName(), currentQueueEntry);
            if (!taskQueued) {
                getLogger().warn(".requeuePendingTasks(): Could not queue task, queueEntry->{}", currentQueueEntry);
            } else {
                queuedTaskCount++;
            }
        }
        getLogger().info(".requeuePendingTasks(): Exit, participantId->{}, queuedTaskCount->{}", participantId, queuedTaskCount);
        return (true);
    }
}
