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
package net.fhirfactory.dricats.ponos.workshops.workflow.participantgrid;

import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantControlStatusEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantRegistration;
import net.fhirfactory.dricats.core.tasks.management.daemon.DaemonBase;
import net.fhirfactory.dricats.ponos.workshops.datagrid.cache.ParticipantCacheServices;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.ParticipantPersistenceServices;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
public class ParticipantManager extends DaemonBase {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantManager.class);
    private static final Long PARTICIPANT_WATCHDOG_DAEMON_STARTUP_DELAY = 120000L;
    private static final Long PARTICIPANT_WATCHDOG_DAEMON_EXECUTION_PERIOD = 15000L;
    private static final Long PARTICIPANT_STARTUP_DAEMON_STARTUP_DELAY = 60000L;
    private boolean initialised;
    private Instant watchDogDaemonActivityInstant;
    private boolean watchDogDaemonIsRunning;
    private boolean startupDaemonCompleted;
    private Instant startupStartInstant;
    private Instant startupCompletionInstant;
    @Inject
    private ParticipantCacheServices participantCache;

    @Inject
    private ParticipantPersistenceServices participantPersistenceServices;

    //
    // Constructor(s)
    //

    public ParticipantManager() {
        this.initialised = false;
        this.watchDogDaemonActivityInstant = Instant.EPOCH;
        this.watchDogDaemonIsRunning = false;
        this.startupDaemonCompleted = false;
        this.startupCompletionInstant = null;
        this.startupStartInstant = null;
    }

    //
    // PostConstruct
    //

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initialise(): Entry");
        if (isInitialised()) {
            getLogger().debug(".initialise(): Exit, nothing to do, already initialised");
        } else {
            getLogger().info(".initialise(): [ParticipantManager Initialisation] Start");
            getLogger().info(".initialise(): [ParticipantManager Initialisation][Schedule Startup Daemon] Start");
            scheduleParticipantStartupDaemon();
            getLogger().info(".initialise(): [ParticipantManager Initialisation][Schedule Startup Daemon] Finish");
            getLogger().info(".initialise(): [ParticipantManager Initialisation][Schedule Watchdog Daemon] Start");
            scheduleParticipantWatchdogDaemon();
            getLogger().info(".initialise(): [ParticipantManager Initialisation][Schedule Watchdog Daemon] Finish");
            getLogger().info(".initialise(): [ParticipantManager Initialisation] Finish");
        }
        getLogger().debug(".initialise(): Exit");
    }

    //
    // Getters (and Setters)
    //

    protected ParticipantPersistenceServices getParticipantPersistenceServices() {
        return (participantPersistenceServices);
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

    protected boolean isStartupDaemonCompleted() {
        return startupDaemonCompleted;
    }

    protected void setStartupDaemonCompleted(boolean startupDaemonCompleted) {
        this.startupDaemonCompleted = startupDaemonCompleted;
    }

    protected boolean isWatchDogDaemonIsRunning() {
        return watchDogDaemonIsRunning;
    }

    protected void setWatchDogDaemonIsRunning(boolean watchDogDaemonIsRunning) {
        this.watchDogDaemonIsRunning = watchDogDaemonIsRunning;
    }

    protected Logger getLogger() {
        return (LOG);
    }

    protected boolean isInitialised() {
        return initialised;
    }

    protected void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    protected Instant getWatchDogDaemonActivityInstant() {
        return watchDogDaemonActivityInstant;
    }

    protected void setWatchDogDaemonActivityInstant(Instant watchDogDaemonActivityInstant) {
        this.watchDogDaemonActivityInstant = watchDogDaemonActivityInstant;
    }

    protected ParticipantCacheServices getParticipantCache() {
        return participantCache;
    }

    //
    // Startup Daemon
    //

    public void scheduleParticipantStartupDaemon() {
        getLogger().debug(".scheduleParticipantStartupDaemon(): Entry");
        TimerTask participantStartupDaemonTask = new TimerTask() {
            public void run() {
                getLogger().debug(".participantStartupDaemonTask(): Entry");
                participantStartupDaemon();
                getLogger().debug(".participantStartupDaemonTask(): Exit");
            }
        };
        Timer timer = new Timer("ParticipantStartupDaemonTimer");
        timer.schedule(participantStartupDaemonTask, PARTICIPANT_STARTUP_DAEMON_STARTUP_DELAY);
        getLogger().debug(".scheduleParticipantStartupDaemon(): Exit");
    }

    public void participantStartupDaemon() {
        getLogger().debug(".participantStartupDaemon(): Entry");
        setStartupStartInstant(Instant.now());

        try {
            List<PetasosParticipant> participants = getParticipantPersistenceServices().loadAllParticipants();
            if (!participants.isEmpty()) {
                for (PetasosParticipant participant : participants) {
                    restoreParticipantRegistration(participant);
                }
            }
        } catch (Exception ex) {
            getLogger().warn(".participantStartupDaemon(): Problem ->", ex);
        }

        setStartupDaemonCompleted(true);
        setStartupCompletionInstant(Instant.now());
        getLogger().debug(".participantStartupDaemon(): Exit");
    }

    //
    // Watchdog Daemon
    //

    public void scheduleParticipantWatchdogDaemon() {
        getLogger().debug(".scheduleParticipantWatchdogDaemon(): Entry");
        TimerTask participantWatchdogDaemonTask = new TimerTask() {
            public void run() {
                getLogger().debug(".participantWatchdogDaemonTask(): Entry");
                if (!isWatchDogDaemonIsRunning()) {
                    participantWatchdogDaemon();
                }
                getLogger().debug(".participantWatchdogDaemonTask(): Exit");
            }
        };
        Timer timer = new Timer("ParticipantWatchdogDaemonTimer");
        timer.schedule(participantWatchdogDaemonTask, PARTICIPANT_WATCHDOG_DAEMON_STARTUP_DELAY, PARTICIPANT_WATCHDOG_DAEMON_EXECUTION_PERIOD);
        getLogger().debug(".scheduleParticipantWatchdogDaemon(): Exit");
    }

    public void participantWatchdogDaemon() {
        getLogger().debug(".participantWatchdogDaemon(): Entry");
        setWatchDogDaemonIsRunning(true);
        setWatchDogDaemonActivityInstant(Instant.now());

        try {

        } catch (Exception ex) {
            getLogger().warn(".participantWatchdogDaemon(): Problem ->", ex);
        }

        setWatchDogDaemonIsRunning(false);
        getLogger().debug(".participantWatchdogDaemon(): Exit");
    }

    //
    // Business Methods
    //

    public PetasosParticipantRegistration restoreParticipantRegistration(PetasosParticipant participant) {
        PetasosParticipantRegistration existingRegistration = getParticipantCache().getPetasosParticipantRegistration(participant.getParticipantId().getName());
        if (existingRegistration == null) {
            existingRegistration = addToRegistrationCache(participant);
        } else {
            getParticipantCache().updateParticipant(participant);
        }
        existingRegistration.setRegistrationUpdateInstant(Instant.now());
        existingRegistration.setRegistrationPersistenceInstant(Instant.now());
        return (existingRegistration);
    }

    public PetasosParticipantRegistration registerParticipantInstance(PetasosParticipant participant) {
        PetasosParticipantRegistration existingRegistration = getParticipantCache().getPetasosParticipantRegistration(participant.getParticipantId().getName());
        if (existingRegistration == null) {
            existingRegistration = addToRegistrationCache(participant);
            if (isStartupDaemonCompleted()) {
                getParticipantCache().setParticipantStatus(participant.getParticipantId().getName(), participant.getParticipantControlStatus());
                storeParticipantRegistration(existingRegistration);
            } else {
                participant.setParticipantControlStatus(PetasosParticipantControlStatusEnum.PARTICIPANT_IS_SUSPENDED);
                getParticipantCache().setParticipantStatus(participant.getParticipantId().getName(), PetasosParticipantControlStatusEnum.PARTICIPANT_IS_SUSPENDED);
            }
        }
        return (existingRegistration);
    }

    public PetasosParticipantRegistration updateParticipantInstance(PetasosParticipant participant) {
        getLogger().debug(".updateParticipantInstance(): Entry, participant->{}", participant);
        PetasosParticipantRegistration existingRegistration = getParticipantCache().getPetasosParticipantRegistration(participant.getParticipantId().getName());
        if (existingRegistration == null) {
            existingRegistration = addToRegistrationCache(participant);
            if (isStartupDaemonCompleted()) {
                getParticipantCache().setParticipantStatus(participant.getParticipantId().getName(), participant.getParticipantControlStatus());
                storeParticipantRegistration(existingRegistration);
            } else {
                participant.setParticipantControlStatus(PetasosParticipantControlStatusEnum.PARTICIPANT_IS_SUSPENDED);
                getParticipantCache().setParticipantStatus(participant.getParticipantId().getName(), PetasosParticipantControlStatusEnum.PARTICIPANT_IS_SUSPENDED);
            }
        }
        getLogger().debug(".updateParticipantInstance(): Exit, existingRegistration->{}", existingRegistration);
        return (existingRegistration);
    }

    protected boolean storeParticipantRegistration(PetasosParticipantRegistration participantRegistration) {
        boolean isPersisted = getParticipantPersistenceServices().saveParticipant(participantRegistration.getParticipant());
        if (isPersisted) {
            participantRegistration.setRegistrationPersistenceInstant(Instant.now());
        }
        return (isPersisted);
    }

    protected boolean storeParticipantRegistration(String participantName) {
        PetasosParticipantRegistration registration = getParticipantCache().getPetasosParticipantRegistration(participantName);
        boolean isPersisted = false;
        if (registration != null) {
            synchronized (getParticipantCache().getParticipantRegistrationLock(participantName)) {
                isPersisted = getParticipantPersistenceServices().saveParticipant(registration.getParticipant());
                if (isPersisted) {
                    registration.setRegistrationPersistenceInstant(Instant.now());
                }
            }
        }
        return (isPersisted);
    }

    public PetasosParticipantRegistration addToRegistrationCache(PetasosParticipant participant) {
        PetasosParticipantRegistration registration = getParticipantCache().addToRegistrationCache(participant, false, false);
        return (registration);
    }

    public PetasosParticipantControlStatusEnum setParticipantControlStatus(String participantName, PetasosParticipantControlStatusEnum controlStatus) {
        if (StringUtils.isNotEmpty(participantName) && controlStatus != null) {
            boolean isAnUpdate = getParticipantCache().setParticipantStatus(participantName, controlStatus);
            if (isAnUpdate) {
                storeParticipantRegistration(participantName);
            }
            return (controlStatus);
        } else {
            return (null);
        }
    }

}
