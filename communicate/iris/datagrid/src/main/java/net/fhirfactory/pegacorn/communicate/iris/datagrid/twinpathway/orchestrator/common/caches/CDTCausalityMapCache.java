/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.orchestrator.common.caches;

import net.fhirfactory.pegacorn.internals.communicate.workflow.model.CDTIdentifier;
import net.fhirfactory.pegacorn.internals.communicate.workflow.model.behaviours.CDTBehaviourIdentifier;
import net.fhirfactory.pegacorn.internals.communicate.workflow.model.behaviours.CDTBehaviourProcessingOfStimulusStatusEnum;
import net.fhirfactory.pegacorn.internals.communicate.workflow.model.stimulus.CDTStimulusIdentifier;
import net.fhirfactory.pegacorn.core.model.petasos.uow.UoWIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class CDTCausalityMapCache {
    private static final Logger LOG = LoggerFactory.getLogger(CDTCausalityMapCache.class);

    private ConcurrentHashMap<UoWIdentifier, ConcurrentHashMap<CDTStimulusIdentifier, ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>>>> causalityMap;

    public CDTCausalityMapCache() {
        causalityMap = new ConcurrentHashMap<>();
    }

    public void addUoW(UoWIdentifier uowId) {
        LOG.debug(".addUoW(): Entry, uowId (UoWIdentifier) --> {}", uowId);
        if (uowId == null) {
            LOG.debug(".addUoW(): Exit, Provided UoW Identifier is null");
            return;
        }
        if (causalityMap.containsKey(uowId)) {
            LOG.debug(".addUoW(): Exit, Already contains the identifier UoW Identifier, so doing nothing");
            return;
        }
        ConcurrentHashMap<CDTStimulusIdentifier, ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>>> stimulusProcessingStatus = new ConcurrentHashMap<>();
        causalityMap.put(uowId, stimulusProcessingStatus);
        LOG.debug(".addUoW(): Exit");
    }

    public void associateStimulus2UoW(CDTStimulusIdentifier stimulusId, UoWIdentifier uowId) {
        LOG.debug(".associateStimulus2UoW(): Entry, stimulusId (StimulusIdentifier) --> {}, uowId (UoWIdentifier) --> {}", stimulusId, uowId);
        if (uowId == null || stimulusId == null) {
            LOG.debug(".associateStimulus2UoW(): Exit, Provided Stimulus Identifier or UoW Identifier is null");
            return;
        }
        if (!causalityMap.containsKey(uowId)) {
            LOG.trace(".associateStimulus2UoW(): Map doesn't contain UoW, adding it");
            addUoW(uowId);
        }
        ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>> stimulusProcessingStatus = new ConcurrentHashMap<>();
        causalityMap.get(uowId).put(stimulusId, stimulusProcessingStatus);
        LOG.debug(".associateStimulus2UoW(): Exit");
    }

    public void associateDigitalTwin2Stimulus2UoW(CDTIdentifier twinId, CDTStimulusIdentifier stimulusId, UoWIdentifier uowId) {
        LOG.debug(".associateDigitalTwin2Stimulus2UoW(): Entry, twinId (DigitalTwinIdentifier) --> {}, stimulusId (StimulusIdentifier) --> {}, uowId (UoWIdentifier) --> {}", twinId, stimulusId, uowId);
        if (twinId == null || stimulusId == null || uowId == null) {
            LOG.debug(".associateDigitalTwin2Stimulus2UoW(): Either UoW Identifier, Stimulus Identifier or the Twin Identifier are null --> doing nothing");
            return;
        }
        if (!causalityMap.containsKey(uowId)) {
            LOG.trace(".associateDigitalTwin2Stimulus2UoW(): Map doesn't contain UoW Identifier, so adding");
            addUoW(uowId);
        }
        associateStimulus2UoW(stimulusId, uowId);
        ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>> stimulusProcessingStatus = causalityMap.get(uowId).get(stimulusId);
        if (stimulusProcessingStatus.containsKey(twinId)) {
            LOG.debug(".associateDigitalTwin2Stimulus2UoW(): Exit, Digital Twin is already associated to this Stimulus (and this UoW)");
            return;
        }
        LOG.trace(".associateDigitalTwin2Stimulus2UoW(): Associating the Twin Identifier to the specified Stimulus Identifier");
        ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum> perDigitalTwinProcessingStatus = new ConcurrentHashMap<>();
        stimulusProcessingStatus.put(twinId, perDigitalTwinProcessingStatus);
        LOG.debug(".associateDigitalTwin2Stimulus2UoW(): Exit");
    }

    public void associateBehaviour2DigitalTwin2Stimulus2UoW(CDTBehaviourIdentifier behaviourId, CDTIdentifier twinId, CDTStimulusIdentifier stimulusId, UoWIdentifier uowId) {
        LOG.debug(".associateBehaviour2DigitalTwin2Stimulus2UoW(): Entry, behaviourId (BehaviourIdentifier) --> {}, twinId (DigitalTwinIdentifier) --> {}, stimulusId (StimulusIdentifier) --> {}", behaviourId, twinId, stimulusId);
        if (twinId == null || stimulusId == null || behaviourId == null || uowId == null) {
            LOG.debug(".associateBehaviour2DigitalTwin2Stimulus2UoW(): Either Behaviour Identifier, Stimulus Identifier or the Twin Identifier are null --> doing nothing");
            return;
        }
        associateDigitalTwin2Stimulus2UoW(twinId, stimulusId, uowId);
        ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum> perBehaviourStatus = causalityMap.get(uowId).get(stimulusId).get(twinId);
        if (perBehaviourStatus.containsKey(behaviourId)) {
            LOG.debug(".associateBehaviour2DigitalTwin2Stimulus2UoW(): Exit, Map already contains association between this Behaviour Identifier, for this Twin Identifier and the Stimulus Identifier");
        }
        LOG.trace(".associateBehaviour2DigitalTwin2Stimulus2UoW(): Associating the Behaviour Identifier to the Twin Identifier - assigning processing status to PROCESSING_STATUS_QUEUED");
        perBehaviourStatus.put(behaviourId, CDTBehaviourProcessingOfStimulusStatusEnum.PROCESSING_STATUS_QUEUED);
        LOG.debug(".associateBehaviour2DigitalTwin2Stimulus2UoW(): Exit");
    }

    public void setProcessingStatus(CDTBehaviourProcessingOfStimulusStatusEnum newStatus, CDTBehaviourIdentifier behaviourId, CDTIdentifier twinId, CDTStimulusIdentifier stimulusId, UoWIdentifier uowId) {
        LOG.debug(".updateProcessingStatus(): Entry, newStatus --> {}, behaviourId (BehaviourIdentifier) --> {}, twinId (DigitalTwinIdentifier) --> {}, stimulusId (StimulusIdentifier) --> {}", newStatus, behaviourId, twinId, stimulusId);
        if (newStatus == null || twinId == null || stimulusId == null || behaviourId == null || uowId == null) {
            LOG.debug(".associateBehaviour2DigitalTwin2Stimulus(): Either newStatus, Behaviour Identifier, Stimulus Identifier or the Twin Identifier are null --> doing nothing");
            return;
        }
        associateBehaviour2DigitalTwin2Stimulus2UoW(behaviourId, twinId, stimulusId, uowId);
        causalityMap.get(uowId).get(stimulusId).get(twinId).put(behaviourId, newStatus);
        LOG.debug(".updateProcessingStatus(): Exit");
    }

    public boolean checkForCompletionOfProcessingByAllBehavioursForAllTwins(UoWIdentifier uowId) {
        LOG.debug(".checkForCompletionOfProcessingByAllBehavioursForAllTwins(): Entry, uowId (UoWIdentifier) --> {}", uowId);
        if (uowId == null) {
            LOG.debug(".checkForCompletionOfProcessingByAllBehavioursForAllTwins(): Exit, provided uowId is null, therefore doesn't exist, therefore any work for them must be finished!");
            return (true);
        }
        ConcurrentHashMap<CDTStimulusIdentifier, ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>>> perStimulusProcessingStatus = causalityMap.get(uowId);
        Enumeration<CDTStimulusIdentifier> stimulusSet = perStimulusProcessingStatus.keys();
        while (stimulusSet.hasMoreElements()) {
            CDTStimulusIdentifier currentStimulus = stimulusSet.nextElement();
            ConcurrentHashMap<CDTIdentifier, ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum>> perTwinProcessingStatus = perStimulusProcessingStatus.get(currentStimulus);
            Enumeration<CDTIdentifier> twinSet = perTwinProcessingStatus.keys();
            while (twinSet.hasMoreElements()) {
                CDTIdentifier currentTwin = twinSet.nextElement();
                ConcurrentHashMap<CDTBehaviourIdentifier, CDTBehaviourProcessingOfStimulusStatusEnum> behaviourStatusMap = perTwinProcessingStatus.get(currentTwin);
                Enumeration<CDTBehaviourIdentifier> behaviourSet = behaviourStatusMap.keys();
                while (behaviourSet.hasMoreElements()) {
                    CDTBehaviourProcessingOfStimulusStatusEnum currentStatus = behaviourStatusMap.get(behaviourSet.nextElement());
                    if (currentStatus != CDTBehaviourProcessingOfStimulusStatusEnum.PROCESSING_STATUS_FINISHED) {
                        LOG.debug(".checkForCompletionOfProcessingByAllBehavioursForAllTwins(): Exit, At least one piece of processing is left --> returning false");
                        return (false);
                    }
                }
            }
        }
        LOG.debug(".checkForCompletionOfProcessingByAllBehavioursForAllTwins(): Exit, Appears all processing is done --> returning true");
        return (true);
    }

    public void purgeUoWFromMap(UoWIdentifier uowId) {
        LOG.debug(".purgeUoWFromMap(): Entry, uowId (UoWIdentifier) --> {}", uowId);
        if (uowId == null) {
            return;
        }
        causalityMap.remove(uowId);
        LOG.debug(".purgeUoWFromMap(): Exit");
    }
}
