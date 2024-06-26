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
package net.fhirfactory.dricats.core.model.oam.metrics.reporting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.oam.topology.valuesets.PetasosMonitoredComponentTypeEnum;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

public class PetasosComponentMetricSet implements Serializable {
    private HashMap<String, PetasosComponentMetric> metrics;
    private ComponentIdType metricSourceComponentId;
    private Instant reportingInstant;

    private PetasosMonitoredComponentTypeEnum componentType;
    private String sourceParticipantName;

    //
    // Contructor(s)
    //

    public PetasosComponentMetricSet() {
        this.metrics = new HashMap<>();
        this.metricSourceComponentId = null;
        this.sourceParticipantName = null;
        this.reportingInstant = Instant.now();
    }

    public PetasosComponentMetricSet(ComponentIdType sourceId, PetasosMonitoredComponentTypeEnum componentType) {
        metrics = new HashMap<>();
        this.metricSourceComponentId = sourceId;
        this.sourceParticipantName = null;
        this.componentType = componentType;
        this.reportingInstant = Instant.now();
    }

    //
    // Getters and Setters
    //

    public Instant getReportingInstant() {
        return reportingInstant;
    }

    public void setReportingInstant(Instant reportingInstant) {
        this.reportingInstant = reportingInstant;
    }

    public HashMap<String, PetasosComponentMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(HashMap<String, PetasosComponentMetric> metrics) {
        this.metrics = metrics;
    }

    public ComponentIdType getMetricSourceComponentId() {
        return metricSourceComponentId;
    }

    public void setMetricSourceComponentId(ComponentIdType metricSourceComponentId) {
        this.metricSourceComponentId = metricSourceComponentId;
    }

    public String getSourceParticipantName() {
        return sourceParticipantName;
    }

    public void setSourceParticipantName(String sourceParticipantName) {
        this.sourceParticipantName = sourceParticipantName;
    }

    public PetasosMonitoredComponentTypeEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(PetasosMonitoredComponentTypeEnum componentType) {
        this.componentType = componentType;
    }

    //
    // Other Helper Methods
    //

    @JsonIgnore
    public PetasosComponentMetric getMetric(String name) {
        if (getMetrics().containsKey(name)) {
            PetasosComponentMetric foundMetric = getMetrics().get(name);
            return (foundMetric);
        }
        return (null);
    }

    @JsonIgnore
    public void addMetric(PetasosComponentMetric metric) {
        if (metric == null) {
            return;
        }
        if (getMetrics().containsKey(metric.getMetricName())) {
            getMetrics().remove(metric.getMetricName());
        }
        getMetrics().put(metric.getMetricName(), metric);
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "PetasosComponentMetricSet{" +
                "metrics=" + metrics +
                ", metricSourceComponentId=" + metricSourceComponentId +
                ", componentType=" + componentType +
                ", sourceWorkUnitProcessorParticipantName='" + sourceParticipantName + '\'' +
                '}';
    }

    public String businessToString() {
        return "PetasosComponentMetricSet{" +
                "metrics=" + metrics +
                ", metricSourceComponentId=" + metricSourceComponentId +
                ", componentType=" + componentType +
                ", sourceWorkUnitProcessorParticipantName='" + sourceParticipantName + '\'' +
                '}';
    }

    public boolean businessEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PetasosComponentMetricSet other = (PetasosComponentMetricSet) obj;
        if (!Objects.equals(this.sourceParticipantName, other.sourceParticipantName)) {
            return false;
        }
        if (!Objects.equals(this.metrics, other.metrics)) {
            return false;
        }
        if (!Objects.equals(this.metricSourceComponentId, other.metricSourceComponentId)) {
            return false;
        }
        return this.componentType == other.componentType;
    }
}
