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
import net.fhirfactory.pegacorn.internals.communicate.workflow.model.stimulus.CDTStimulusPackage;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CDTInstanceWorkQueues {
    private ConcurrentHashMap<CDTIdentifier, ConcurrentLinkedQueue<CDTStimulusPackage>> twinInstanceStimulusQueue;
    private ConcurrentHashMap<CDTIdentifier, Object> twinInstanceQueueLock;

    public CDTInstanceWorkQueues() {
        twinInstanceStimulusQueue = new ConcurrentHashMap<>();
        this.twinInstanceQueueLock = new ConcurrentHashMap<>();
    }

    /**
     * This method supports the addition of new activity elements (StimulusPackage).
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @param newStimuli             The StimulusPackage to be added the the TwinInstance's activity queue.
     */
    public void addStimulus2Queue(CDTIdentifier twinInstanceIdentifier, CDTStimulusPackage newStimuli) {
        if (!twinInstanceQueueLock.containsKey(twinInstanceIdentifier)) {
            twinInstanceQueueLock.put(twinInstanceIdentifier, new Object());
        }
        Object twinQueueLock = twinInstanceQueueLock.get(twinInstanceIdentifier);
        synchronized (twinQueueLock) {
            if (twinInstanceStimulusQueue.containsKey(twinInstanceIdentifier)) {
                Queue<CDTStimulusPackage> twinStimuli = twinInstanceStimulusQueue.get(twinInstanceIdentifier);
                if (!twinStimuli.contains(newStimuli)) {
                    twinStimuli.add(newStimuli);
                }
            } else {
                ConcurrentLinkedQueue<CDTStimulusPackage> stimuliQueue = new ConcurrentLinkedQueue<>();
                stimuliQueue.add(newStimuli);
                twinInstanceStimulusQueue.put(twinInstanceIdentifier, stimuliQueue);
            }
        }
    }

    /**
     * This method supports the extraction (pop'ing) of the next-in-line activity element from the queue. It also
     * removes that element from the queue.
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @return A StimulusPackage from the FIFO queue for the identified Digital Twin.
     */
    public CDTStimulusPackage getNextStimulusPackage(CDTIdentifier twinInstanceIdentifier) {
        if (!twinInstanceQueueLock.containsKey(twinInstanceIdentifier)) {
            twinInstanceQueueLock.put(twinInstanceIdentifier, new Object());
        }
        Object twinQueueLock = twinInstanceQueueLock.get(twinInstanceIdentifier);
        CDTStimulusPackage nextStimulusPackage = null;
        synchronized (twinQueueLock) {
            if (twinInstanceStimulusQueue.containsKey(twinInstanceIdentifier)) {
                Queue<CDTStimulusPackage> twinStimuli = twinInstanceStimulusQueue.get(twinInstanceIdentifier);
                if (!twinStimuli.isEmpty()) {
                    nextStimulusPackage = twinStimuli.poll();
                    if (twinStimuli.isEmpty()) {
                        twinInstanceStimulusQueue.remove(twinInstanceIdentifier);
                    }
                }
            }
        }
        return (nextStimulusPackage);
    }


    /**
     * This method is used to ascertain if their is activity (StimulusPackage) items within the
     * Digital Twin's activity queue.
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @return A count of the number of StimulusPackage elements (Activity) waiting for the Twin Instance to process.
     */
    public int getStimulusCount(CDTIdentifier twinInstanceIdentifier) {
        if (twinInstanceStimulusQueue.containsKey(twinInstanceIdentifier)) {
            Queue<CDTStimulusPackage> twinStimuli = twinInstanceStimulusQueue.get(twinInstanceIdentifier);
            if (!twinStimuli.isEmpty()) {
                return (twinStimuli.size());
            }
        }
        // If we get to this point, there is no Stimuli for the identifier twinInstanceIdentifier
        // so return ( 0 ).
        return (0);
    }

    public Set<CDTIdentifier> getTwinsWithQueuedWork() {
        if (twinInstanceStimulusQueue.isEmpty()) {
            return (new HashSet<>());
        }
        HashSet<CDTIdentifier> twinIdSet = new HashSet<>();
        Enumeration<CDTIdentifier> twinsWithQueuedTraffic = twinInstanceStimulusQueue.keys();
        while (twinsWithQueuedTraffic.hasMoreElements()) {
            twinIdSet.add(twinsWithQueuedTraffic.nextElement());
        }
        return (twinIdSet);
    }

}
