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
package net.fhirfactory.dricats.core.model.ui.resources.summaries;

import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkshopSummary extends SoftwareComponentSummary {

    private Map<ComponentIdType, WorkUnitProcessorSummary> workUnitProcessors;

    //
    // Constructor(s)
    //

    public WorkshopSummary() {
        super();
        this.workUnitProcessors = new ConcurrentHashMap<>();
    }

    //
    // Getters and Setters
    //

    public Map<ComponentIdType, WorkUnitProcessorSummary> getWorkUnitProcessors() {
        return workUnitProcessors;
    }

    public void setWorkUnitProcessors(Map<ComponentIdType, WorkUnitProcessorSummary> workUnitProcessors) {
        this.workUnitProcessors = workUnitProcessors;
    }

    public void removeWorkUnitProcessor(ComponentIdType componentID) {
        if (workUnitProcessors.containsKey(componentID)) {
            workUnitProcessors.remove(componentID);
        }
    }

    public void addWorkUnitProcessor(WorkUnitProcessorSummary wup) {
        removeWorkUnitProcessor(wup.getComponentID());
        workUnitProcessors.put(wup.getComponentID(), wup);
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "WorkshopSummary{" +
                "participantName='" + getParticipantName() + '\'' +
                ", subsystemParticipantName='" + getSubsystemParticipantName() + '\'' +
                ", topologyNodeFDN=" + getTopologyNodeFDN() +
                ", componentID=" + getComponentID() +
                ", nodeVersion='" + getNodeVersion() + '\'' +
                ", nodeType=" + getNodeType() +
                ", concurrencyMode='" + getConcurrencyMode() + '\'' +
                ", resilienceMode='" + getResilienceMode() + '\'' +
                ", workUnitProcessors=" + workUnitProcessors +
                ", lastSynchronisationInstant=" + getLastSynchronisationInstant() +
                ", lastActivityInstant=" + getLastActivityInstant() +
                ", resourceId='" + getResourceId() + '\'' +
                '}';
    }
}
