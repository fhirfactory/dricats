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
package net.fhirfactory.dricats.core.oam.topology.cache;

import net.fhirfactory.dricats.core.model.oam.topology.reporting.PetasosMonitoredTopologyGraph;
import net.fhirfactory.dricats.core.oam.metrics.cache.common.LocalOAMCacheBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PetasosLocalTopologyReportingDM extends LocalOAMCacheBase {

    private static final Logger LOG = LoggerFactory.getLogger(PetasosLocalTopologyReportingDM.class);

    private PetasosMonitoredTopologyGraph currentState;
    private PetasosMonitoredTopologyGraph reportedState;

    //
    // Constructor(s)
    //

    public PetasosLocalTopologyReportingDM() {
        super();
        LOG.debug(".PetasosLocalTopologyReportingDM(): Constructor initialisation");
        this.currentState = null;
        this.reportedState = null;
    }

    //
    // Getters and Setters
    //

    public PetasosMonitoredTopologyGraph getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PetasosMonitoredTopologyGraph currentState) {
        this.currentState = currentState;
        refreshCurrentStateUpdateInstant();
    }

    public void stateReported() {
        this.reportedState = this.currentState;
        refreshReportedStateUpdateInstant();
    }

    public PetasosMonitoredTopologyGraph getReportedState() {
        return reportedState;
    }

    public void setReportedState(PetasosMonitoredTopologyGraph reportedState) {
        this.reportedState = reportedState;
        refreshCurrentStateUpdateInstant();
    }
}
