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
package net.fhirfactory.dricats.ponos.workshops.datagrid.cache;

import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantControlStatusEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantRegistration;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantRegistrationStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.ponos.workshops.datagrid.cache.core.PonosReplicatedCacheServices;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.ParticipantPersistenceServices;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ParticipantCacheServices {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantCacheServices.class);
    private final Object petasosParticipantRegistrationCacheLock;
    private final Object participantStatusLock;
    private final Object participantActivityTimestampMapLock;
    private final Object participantCacheSizeMapLock;
    private boolean initialised;
    // Cache<participantName, participantRegistration>
    private Cache<String, PetasosParticipantRegistration> participantRegistrationCache;
    private ConcurrentHashMap<String, Object> participantRegistrationLock;
    private ConcurrentHashMap<String, PetasosParticipantControlStatusEnum> participantStatusMap;
    private ConcurrentHashMap<String, Instant> participantActivityTimestampMap;
    private ConcurrentHashMap<String, Integer> participantCacheSizeMap;
    @Inject
    private PonosReplicatedCacheServices replicatedCacheServices;

    @Inject
    private ParticipantPersistenceServices participantPersistenceServices;

    //
    // Constructor(s)
    //

    public ParticipantCacheServices() {
        this.initialised = false;
        this.petasosParticipantRegistrationCacheLock = new Object();
        this.participantRegistrationCache = null;
        this.participantStatusMap = new ConcurrentHashMap<>();
        this.participantRegistrationLock = new ConcurrentHashMap<>();
        this.participantStatusLock = new Object();
        this.participantActivityTimestampMap = new ConcurrentHashMap<>();
        this.participantActivityTimestampMapLock = new Object();
        this.participantCacheSizeMap = new ConcurrentHashMap<>();
        this.participantCacheSizeMapLock = new Object();
    }

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initialise(): Entry");
        if (!initialised) {
            getLogger().info(".initialise(): Initialisation Start");

            getLogger().info(".initialise(): [Initialising Caches] Start");
            participantRegistrationCache = replicatedCacheServices.getCacheManager().createCache("PetasosParticipantRegistrationCache", replicatedCacheServices.getCacheConfigurationBuild());
            getLogger().info(".initialise(): [Initialising Caches] End");

            this.initialised = true;
        }
        getLogger().debug(".initialised(): Exit");
    }

    //
    // Participant Cache
    //


    /**
     * @param participant
     * @return
     */
    public PetasosParticipantRegistration addToRegistrationCache(PetasosParticipant participant, boolean managementOverride, boolean fromPersistence) {
        getLogger().debug(".registerParticipant(): Entry, participant->{}", participant);

        getLogger().trace(".registerParticipant(): First, we check the content of the passed-in parameter");
        if (participant == null) {
            getLogger().debug("registerParticipant(): Exit, participant is null, returning -null-");
            return (null);
        }
        if (StringUtils.isEmpty(participant.getParticipantName())) {
            getLogger().debug("registerParticipant(): Exit, participant name is empty, returning -null-");
            return (null);
        }
        getLogger().trace(".registerParticipant(): Now, check to see if participant (instance) is already cached and, if so, do nothing!");
        if (!getParticipantRegistrationCache().containsKey(participant.getParticipantName())) {
            getLogger().trace(".registerParticipant(): participant is not in Map, so add it!");
            Object participantRegistrationLock = getParticipantRegistrationLock().get(participant.getParticipantId().getName());
            if (participantRegistrationLock == null) {
                participantRegistrationLock = new Object();
                getParticipantRegistrationLock().put(participant.getParticipantId().getName(), participantRegistrationLock);
            }
            PetasosParticipantRegistration registration = new PetasosParticipantRegistration();
            registration.setParticipant(participant);
            registration.setRegistrationPersistenceInstant(Instant.EPOCH);
            registration.setRegistrationCommentary("Publisher Registered");
            registration.setRegistrationUpdateInstant(Instant.now());
            registration.setRegistrationStatus(PetasosParticipantRegistrationStatusEnum.PETASOS_PARTICIPANT_REGISTRATION_ACTIVE);
            registration.getParticipantInstances().add(participant.getComponentID());
            synchronized (this.petasosParticipantRegistrationCacheLock) {
                getParticipantRegistrationCache().put(participant.getParticipantName(), registration);
            }
            getLogger().debug("registerParticipant(): Exit, added new registration, registration->{}", registration);
            return (registration);
        } else {
            Object participantRegistrationLock = getParticipantRegistrationLock().get(participant.getParticipantId().getName());
            if (participantRegistrationLock == null) {
                participantRegistrationLock = new Object();
                getParticipantRegistrationLock().put(participant.getParticipantId().getName(), participantRegistrationLock);
            }
            getLogger().debug("registerParticipant(): Exit, participant already registered, return -null-");
            return (null);
        }
    }

    public Object getParticipantRegistrationLock(String participantName) {
        if (StringUtils.isNotEmpty(participantName)) {
            Object participantRegistrationLock = getParticipantRegistrationLock().get(participantName);
            if (participantRegistrationLock == null) {
                participantRegistrationLock = new Object();
                getParticipantRegistrationLock().put(participantName, participantRegistrationLock);
            }
            return (participantRegistrationLock);
        } else {
            getLogger().warn(".getParticipantRegistrationLock(): Cannot get lock for participant, participantName is empty");
            return (null);
        }
    }

    /**
     * @param participant
     * @return
     */
    public PetasosParticipantRegistration deregisterPetasosParticipant(PetasosParticipant participant) {
        getLogger().debug(".deregisterPetasosParticipant(): Entry, participant->{}", participant);
        if (participant == null) {
            getLogger().debug("deregisterPetasosParticipant(): Exit, participant is null");
            return (null);
        }
        PetasosParticipantRegistration registration = deregisterPetasosParticipant(participant.getParticipantName());
        getLogger().debug(".deregisterPetasosParticipant(): Exit, registration->{}", registration);
        return (registration);
    }

    /**
     * @param participantName
     * @return
     */
    public PetasosParticipantRegistration deregisterPetasosParticipant(String participantName) {
        getLogger().debug(".deregisterPetasosParticipant(): Entry, participantName->{}", participantName);
        if (StringUtils.isEmpty(participantName)) {
            getLogger().debug("deregisterPetasosParticipant(): Exit, participant is empty");
            return (null);
        }
        PetasosParticipantRegistration registration = getPetasosParticipantRegistration(participantName);
        synchronized (this.petasosParticipantRegistrationCacheLock) {
            getParticipantRegistrationCache().remove(participantName);
        }
        getLogger().debug(".deregisterPetasosParticipant(): Exit, registration->{}", registration);
        return (registration);
    }

    /**
     * @param participant
     * @return
     */
    public PetasosParticipantRegistration updateParticipant(PetasosParticipant participant) {
        getLogger().debug(".updateParticipant(): Entry, participant->{}", participant);
        if (participant == null) {
            getLogger().debug(".updateParticipant(): Exit, participant is null, returning null");
            return (null);
        }
        if (participant.getParticipantId() == null) {
            getLogger().debug(".updateParticipant(): Exit, participant has no participantId, returning null");
            return (null);
        }
        if (StringUtils.isEmpty(participant.getParticipantId().getName())) {
            getLogger().debug(".updateParticipant(): Exit, participantId has no name, returning null");
            return (null);
        }
        boolean isUpdate = false;
        PetasosParticipantRegistration existingRegistration = null;
        synchronized (getParticipantRegistrationLock(participant.getParticipantId().getName())) {
            existingRegistration = getPetasosParticipantRegistration(participant.getParticipantName());
            if (existingRegistration != null) {
                if (participant.getComponentID() != null) {
                    if (!existingRegistration.getParticipantInstances().contains(participant.getComponentID())) {
                        existingRegistration.getParticipantInstances().add(participant.getComponentID());
                        isUpdate = true;
                    }
                }
                for (TaskWorkItemSubscriptionType currentSubscription : participant.getSubscriptions()) {
                    if (!existingRegistration.getParticipant().getSubscriptions().contains(currentSubscription)) {
                        existingRegistration.getParticipant().getSubscriptions().add(currentSubscription);
                        isUpdate = true;
                    }
                }
                if (isUpdate) {
                    existingRegistration.setRegistrationUpdateInstant(Instant.now());
                }
            }
        }
        getLogger().debug(".updateParticipant(): Exit, existingRegistration->{}", existingRegistration);
        return (existingRegistration);
    }

    /**
     * @param participantName
     * @return
     */
    public PetasosParticipantRegistration getPetasosParticipantRegistration(String participantName) {
        getLogger().debug(".getPetasosParticipantRegistration(): Entry, participantName->{}", participantName);
        if (StringUtils.isEmpty(participantName)) {
            getLogger().debug(".getPetasosParticipantRegistration(): Exit, participantName is empty, returning null");
            return (null);
        }
        PetasosParticipantRegistration petasosParticipantRegistration = getParticipantRegistrationCache().get(participantName);
        getLogger().debug(".getPetasosParticipantRegistration(): Exit, petasosParticipantRegistration->{}", petasosParticipantRegistration);
        return (petasosParticipantRegistration);
    }

    //
    // Participant Status
    //

    public boolean setParticipantStatus(String participantName, PetasosParticipantControlStatusEnum participantStatus) {
        boolean isUpdated = false;
        if (StringUtils.isNotEmpty(participantName) && participantStatus != null) {
            synchronized (participantStatusLock) {
                participantStatusMap.put(participantName, participantStatus);
            }
            synchronized (getParticipantRegistrationLock(participantName)) {
                if (getParticipantRegistrationCache().containsKey(participantName)) {
                    PetasosParticipantRegistration petasosParticipantRegistration = getParticipantRegistrationCache().get(participantName);
                    if (!petasosParticipantRegistration.getParticipant().getParticipantControlStatus().equals(participantStatus)) {
                        petasosParticipantRegistration.getParticipant().setParticipantControlStatus(participantStatus);
                        isUpdated = true;
                    }
                }
            }
        }
        getLogger().debug(".setParticipantStatus(): Exit, isUpdate->{}", isUpdated);
        return (isUpdated);
    }

    public PetasosParticipantControlStatusEnum getParticipantStatus(String participantName) {
        if (StringUtils.isEmpty(participantName)) {
            return (PetasosParticipantControlStatusEnum.PARTICIPANT_IS_DISABLED);
        }
        PetasosParticipantControlStatusEnum participantStatus = participantStatusMap.get(participantName);
        return (participantStatus);
    }

    public void touchParticipantActivityTimestamp(String participantName) {
        if (StringUtils.isNotEmpty(participantName)) {
            synchronized (participantActivityTimestampMapLock) {
                participantActivityTimestampMap.put(participantName, Instant.now());
            }
        }
    }

    public Instant getParticipantActivityTimestamp(String participantName) {
        if (StringUtils.isEmpty(participantName)) {
            return (Instant.EPOCH);
        }
        Instant activityInstant = participantActivityTimestampMap.get(participantName);
        if (activityInstant == null) {
            activityInstant = Instant.EPOCH;
        }
        return (activityInstant);
    }

    public void setParticipantCacheSize(String participantName, Integer size) {
        if (StringUtils.isNotEmpty(participantName) && size != null) {
            synchronized (participantCacheSizeMapLock) {
                participantCacheSizeMap.put(participantName, size);
            }
        }
    }

    public Integer getParticipantCacheSize(String participantName) {
        if (StringUtils.isEmpty(participantName)) {
            return (-1);
        }
        Integer size = participantCacheSizeMap.get(participantName);
        if (size == null) {
            size = 0;
        }
        return (size);
    }

    //
    // Simple Registration Check
    //

    public boolean isPetasosParticipantRegistered(PetasosParticipant publisher) {
        return false;
    }

    //
    // Macro
    //

    /**
     * @return
     */
    public Set<PetasosParticipantRegistration> getAllRegistrations() {
        Set<PetasosParticipantRegistration> registrationSet = new HashSet<>();
        synchronized (petasosParticipantRegistrationCacheLock) {
            registrationSet.addAll(getParticipantRegistrationCache().values());
        }
        return (registrationSet);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected Cache<String, PetasosParticipantRegistration> getParticipantRegistrationCache() {
        return participantRegistrationCache;
    }

    protected Object getPetasosParticipantRegistrationCacheLock() {
        return petasosParticipantRegistrationCacheLock;
    }

    protected ConcurrentHashMap<String, Object> getParticipantRegistrationLock() {
        return (participantRegistrationLock);
    }

}
