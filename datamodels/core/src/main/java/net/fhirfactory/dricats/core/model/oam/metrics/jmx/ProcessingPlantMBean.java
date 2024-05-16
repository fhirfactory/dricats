package net.fhirfactory.dricats.core.model.oam.metrics.jmx;

import java.time.Instant;

public interface ProcessingPlantMBean {
    public Integer getActionableTaskCacheSize();

    public Instant getActivityIndicator();

    public String getOperationalStatus();

    public String getExecutionStatus();
}
