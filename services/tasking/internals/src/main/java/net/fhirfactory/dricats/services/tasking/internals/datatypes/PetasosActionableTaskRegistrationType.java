package net.fhirfactory.dricats.services.tasking.internals.datatypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.datagrid.valuesets.DatagridPersistenceResourceStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.performer.datatypes.TaskPerformerTypeType;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PetasosActionableTaskRegistrationType implements Serializable {
    private TaskIdType actionableTaskId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant registrationInstant;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant checkInstant;
    private DatagridPersistenceResourceStatusEnum resourceStatus;
    private Set<ComponentIdType> fulfillmentProcessingPlants;
    private Set<String> fulfillmentServiceNames;
    private Set<TaskPerformerTypeType> performerType;

    //
    // Constructor(s)
    //

    public PetasosActionableTaskRegistrationType() {
        this.actionableTaskId = null;
        this.registrationInstant = null;
        this.checkInstant = null;
        this.fulfillmentProcessingPlants = new HashSet<>();
        this.performerType = new HashSet<>();
        this.fulfillmentServiceNames = new HashSet<>();
        this.resourceStatus = null;
    }

    //
    // Getters and Setters
    //


    public DatagridPersistenceResourceStatusEnum getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(DatagridPersistenceResourceStatusEnum resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public TaskIdType getActionableTaskId() {
        return actionableTaskId;
    }

    public void setActionableTaskId(TaskIdType actionableTask) {
        this.actionableTaskId = actionableTask;
    }

    public Instant getRegistrationInstant() {
        return registrationInstant;
    }

    public void setRegistrationInstant(Instant registrationInstant) {
        this.registrationInstant = registrationInstant;
    }

    public Instant getCheckInstant() {
        return checkInstant;
    }

    public void setCheckInstant(Instant checkInstant) {
        this.checkInstant = checkInstant;
    }

    public Set<ComponentIdType> getFulfillmentProcessingPlants() {
        return fulfillmentProcessingPlants;
    }

    public void setFulfillmentProcessingPlants(Set<ComponentIdType> fulfillmentProcessingPlants) {
        this.fulfillmentProcessingPlants.clear();
        this.fulfillmentProcessingPlants.addAll(fulfillmentProcessingPlants);
    }

    public Set<String> getFulfillmentServiceNames() {
        return fulfillmentServiceNames;
    }

    public void setFulfillmentServiceNames(Set<String> fulfillmentServiceNames) {
        this.fulfillmentServiceNames.clear();
        this.fulfillmentServiceNames.addAll(fulfillmentServiceNames);
    }

    public Set<TaskPerformerTypeType> getPerformerType() {
        return performerType;
    }

    public void setPerformerType(Set<TaskPerformerTypeType> performerType) {
        this.performerType.clear();
        this.performerType.addAll(performerType);
    }

    //
    // Business Methods
    //

    public void addPerformerType(TaskPerformerTypeType performerType) {
        if (performerType == null) {
            return;
        }
        if (!getPerformerType().contains(performerType)) {
            getPerformerType().add(performerType);
        }
    }

    public void addPerformerTypes(Collection<TaskPerformerTypeType> taskPerformers) {
        if (taskPerformers == null) {
            return;
        }
        if (taskPerformers.isEmpty()) {
            return;
        }
        for (TaskPerformerTypeType currentPerformerType : taskPerformers) {
            addPerformerType(currentPerformerType);
        }
    }

    public void addFulfillmentServiceName(String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            return;
        }
        if (!getFulfillmentServiceNames().contains(serviceName)) {
            getFulfillmentServiceNames().add(serviceName);
        }
    }

    public void addServiceFulfillmentNames(Collection<String> serviceNames) {
        if (serviceNames == null) {
            return;
        }
        if (serviceNames.isEmpty()) {
            return;
        }
        for (String currentServiceName : serviceNames) {
            addFulfillmentServiceName(currentServiceName);
        }
    }

    public void addFulfillmentProcessingPlant(ComponentIdType processingPlantId) {
        if (processingPlantId == null) {
            return;
        }
        if (!getFulfillmentProcessingPlants().contains(processingPlantId)) {
            getFulfillmentProcessingPlants().add(processingPlantId);
        }
    }

    public void addFulfillmentProcessingPlants(Collection<ComponentIdType> processingPlants) {
        if (processingPlants == null) {
            return;
        }
        if (processingPlants.isEmpty()) {
            return;
        }
        for (ComponentIdType currentProcessingPlant : processingPlants) {
            addFulfillmentProcessingPlant(currentProcessingPlant);
        }
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "PetasosActionableTaskRegistrationType{" +
                "actionableTaskId=" + actionableTaskId +
                ", registrationInstant=" + registrationInstant +
                ", checkInstant=" + checkInstant +
                ", fulfillmentProcessingPlants=" + fulfillmentProcessingPlants +
                ", fulfillmentServiceNames=" + fulfillmentServiceNames +
                ", performerType=" + performerType +
                ", resourceStatus=" + resourceStatus +
                '}';
    }
}
