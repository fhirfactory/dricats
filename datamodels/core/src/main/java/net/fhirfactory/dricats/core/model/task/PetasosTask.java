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
package net.fhirfactory.dricats.core.model.task;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.datagrid.datatypes.DatagridElementSourceResourceIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskContextType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.performer.datatypes.TaskPerformerTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.datatypes.TaskReasonType;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskExecutionStatus;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.TaskOutcomeStatusType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityType;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemType;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a Petasos Task.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Jacksonized
public class PetasosTask implements Serializable {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant creationInstant;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant updateInstant;

    private DatagridElementSourceResourceIdType sourceResourceId;

    private TaskContextType taskContext;

    private TaskIdType taskId;

    private TaskTypeType taskType;

    private TaskWorkItemType taskWorkItem;

    private TaskTraceabilityType taskTraceability;

    private TaskOutcomeStatusType taskOutcomeStatus;

    private List<TaskPerformerTypeType> taskPerformerTypes = new ArrayList<>();

    private TaskReasonType taskReason;

    private ComponentIdType taskNodeAffinity;

    private Set<TaskIdType> aggregateTaskMembership = new HashSet<>();

    private TaskExecutionStatus taskExecutionDetail;

    private boolean registered;

    //
    // hasMethods
    //

    public boolean hasCreationInstant(){
        boolean hasValue = creationInstant != null;
        return(hasValue);
    }

    public boolean hasUpdateInstant(){
        boolean hasValue = updateInstant != null;
        return(hasValue);
    }

    public boolean hasSourceResourceId() {
        boolean hasValue = sourceResourceId != null;
        return (hasValue);
    }

    public boolean hasTaskContext() {
        boolean hasValue = taskContext != null;
        return(hasValue);
    }

    public boolean hasTaskId() {
        boolean hasValue = taskId != null;
        return(hasValue);
    }

    public boolean hasTaskType() {
        boolean hasValue = taskType != null;
        return(hasValue);
    }

    public boolean hasTaskWorkItem() {
        boolean hasValue = taskWorkItem != null;
        return(hasValue);
    }

    public boolean hasTaskTraceability() {
        boolean hasValue = taskTraceability != null;
        return (hasValue);
    }

    public boolean hasTaskOutcomeStatus() {
        boolean hasValue = taskOutcomeStatus != null;
        return(hasValue);
    }

    public boolean hasTaskPerformerTypes() {
        boolean hasValue = taskPerformerTypes != null;
        if(hasValue){
            if(taskPerformerTypes.isEmpty()){
                return(false);
            }
        }
        return(hasValue);
    }

    public boolean hasTaskReason() {
        boolean hasValue = taskReason != null;
        return(hasValue);
    }

    public boolean hasTaskNodeAffinity() {
        boolean hasValue = taskNodeAffinity != null;
        return(hasValue);
    }

    public boolean hasAggregateTaskMembership() {
        boolean hasValue = aggregateTaskMembership != null;
        if(hasValue){
            if(aggregateTaskMembership.isEmpty()){
                return(false);
            }
        }
        return(hasValue);
    }

    public boolean hasTaskExecutionDetail() {
        boolean hasValue = taskExecutionDetail != null;
        return(hasValue);
    }

    //
    // Update
    //

    public PetasosTask update(PetasosTask update) {
        PetasosTask petasosTask = updatePetasosTask(update);
        return (petasosTask);
    }

    /**
     * Updates a PetasosTask with the provided update.
     *
     * @param update The PetasosTask object containing the updated data.
     * @return The updated PetasosTask.
     */
    protected PetasosTask updatePetasosTask(PetasosTask update) {
        log.debug(".updatePetasosTask(): Entry, petasosTask->{}", update);
        if (update == null) {
            return (this);
        }
        // sourceResourceId
        if (update.getSourceResourceId() != null) {
            this.setSourceResourceId(update.getSourceResourceId());
        }
        // taskContext
        if (update.getTaskContext() != null) {
            log.trace(".updatePetasosTask(): Updating TaskContext");
            if (!(this.getTaskContext() != null)) {
                log.trace(".updatePetasosTask(): Updating TaskContext: Copying Whole TaskContext");
                this.setTaskContext(update.getTaskContext());
            } else {
                log.trace(".updatePetasosTask(): Updating TaskContext: Performing TaskContext per-attribute update ");
                if (update.getTaskContext().hasTaskEncounter()) {
                    log.trace(".updatePetasosTask(): Updating TaskContext: updating TaskEncounter attribute");
                    this.getTaskContext().setTaskEncounter(update.getTaskContext().getTaskEncounter());
                }
                if (update.getTaskContext().hasTaskBeneficiary()) {
                    log.trace(".updatePetasosTask(): Updating TaskContext: updating TaskBeneficiary attribute");
                    this.getTaskContext().setTaskBeneficiary(update.getTaskContext().getTaskBeneficiary());
                }
                if (update.getTaskContext().hasTaskTriggerSummary()) {
                    log.trace(".updatePetasosTask(): Updating TaskContext: updating TaskTriggerSummary attribute");
                    this.getTaskContext().setTaskTriggerSummary(update.getTaskContext().getTaskTriggerSummary());
                }
            }
        }
        // taskType
        if (update.getTaskType() != null) {
            log.trace(".updatePetasosTask(): Updating TaskType");
            if (!(this.getTaskType() != null)) {
                log.trace(".updatePetasosTask(): Updating TaskType: Copying Whole TaskType");
                this.setTaskType(update.getTaskType());
            } else {
                log.trace(".updatePetasosTask(): Updating TaskType: Performing TaskType per-attribute update ");
                if (update.getTaskType().hasTaskSubType()) {
                    log.trace(".updatePetasosTask(): Updating TaskType: updating TaskSubType attribute");
                    this.getTaskType().setTaskSubType(update.getTaskType().getTaskSubType());
                }
                if (update.getTaskType().hasTaskType()) {
                    log.trace(".updatePetasosTask(): Updating TaskType: updating TaskType attribute");
                    this.getTaskType().setTaskType(update.getTaskType().getTaskType());
                }
            }
        }
        // taskWorkItem
        if (update.getTaskWorkItem() != null) {
            log.trace(".updatePetasosTask(): Updating TaskWorkItem");
            if (!(this.getTaskWorkItem() != null)) {
                log.trace(".updatePetasosTask(): Updating TaskWorkItem: Copying Whole TaskWorkItem");
                this.setTaskWorkItem(update.getTaskWorkItem());
            } else {
                log.trace(".updatePetasosTask(): Updating TaskWorkItem: Performing TaskWorkItem per-attribute update");
                if (update.getTaskWorkItem().hasIngresContent()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating IngresContent attribute");
                    this.getTaskWorkItem().setIngresContent(update.getTaskWorkItem().getIngresContent());
                }
                if (update.getTaskWorkItem().hasEgressContent()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating EgressContent attribute");
                    this.getTaskWorkItem().setEgressContent(update.getTaskWorkItem().getEgressContent());
                }
                if (update.getTaskWorkItem().hasFailureDescription()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating FailureDescription attribute");
                    this.getTaskWorkItem().setFailureDescription(update.getTaskWorkItem().getFailureDescription());
                }
                if (update.getTaskWorkItem().hasInstanceID()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating InstanceId attribute");
                    this.getTaskWorkItem().setInstanceID(update.getTaskWorkItem().getInstanceID());
                }
                if (update.getTaskWorkItem().hasPayloadTopicID()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating TypeId attribute");
                    this.getTaskWorkItem().setUoWTypeID(update.getTaskWorkItem().getTypeID());
                }
                if (update.getTaskWorkItem().hasProcessingOutcome()) {
                    log.trace(".updatePetasosTask(): Updating TaskWorkItem: Updating ProcessingOutcome attribute");
                    this.getTaskWorkItem().setProcessingOutcome(update.getTaskWorkItem().getProcessingOutcome());
                }
            }
        }
        // taskTraceability
        if (update.getTaskTraceability() != null) {
            if (!(this.getTaskTraceability() != null)) {
                this.setTaskTraceability(update.getTaskTraceability());
            } else {
                if (update.getTaskTraceability().getTaskJourney() != null) {
                    this.getTaskTraceability().setTaskJourney(update.getTaskTraceability().getTaskJourney());
                }
            }
        }
        // taskOutcomeStatus
        if (update.getTaskOutcomeStatus() != null) {
            if (!(this.getTaskOutcomeStatus() != null)) {
                this.setTaskOutcomeStatus(update.getTaskOutcomeStatus());
            } else {
                if (update.getTaskOutcomeStatus().getOutcomeStatus() != null) {
                    this.getTaskOutcomeStatus().setOutcomeStatus(update.getTaskOutcomeStatus().getOutcomeStatus());
                }
                if (update.getTaskOutcomeStatus().getEntryInstant() != null) {
                    this.getTaskOutcomeStatus().setEntryInstant(update.getTaskOutcomeStatus().getEntryInstant());
                }
            }
        }
        // taskPerformerTypes
        if (update.getTaskPerformerTypes() != null) {
            if (!(this.getTaskPerformerTypes() != null)) {
                this.setTaskPerformerTypes(update.getTaskPerformerTypes());
            }
        }
        // taskReason
        if (update.getTaskReason() != null) {
            this.setTaskReason(update.getTaskReason());
        }
        // taskNodeAffinity
        if (update.getTaskNodeAffinity() != null) {
            if (!(this.getTaskNodeAffinity() != null)) {
                this.setTaskNodeAffinity(update.getTaskNodeAffinity());
            } else {
                if (update.getTaskNodeAffinity().hasIdValidityEndInstant()) {
                    this.getTaskNodeAffinity().setIdValidityEndInstant(update.getTaskNodeAffinity().getIdValidityEndInstant());
                }
                if (update.getTaskNodeAffinity().hasIdValidityStartInstant()) {
                    this.getTaskNodeAffinity().setIdValidityStartInstant(update.getTaskNodeAffinity().getIdValidityStartInstant());
                }
                if (update.getTaskNodeAffinity().hasId()) {
                    this.getTaskNodeAffinity().setId(update.getTaskNodeAffinity().getId());
                }
                if (update.getTaskNodeAffinity().hasDisplayName()) {
                    this.getTaskNodeAffinity().setDisplayName(this.getTaskNodeAffinity().getDisplayName());
                }
            }
        }
        // aggregateTaskMembership
        if (update.getAggregateTaskMembership() != null) {
            if (!(this.getAggregateTaskMembership() != null)) {
                this.setAggregateTaskMembership(update.getAggregateTaskMembership());
            } else {
                this.getAggregateTaskMembership().clear();
                this.getAggregateTaskMembership().addAll(update.getAggregateTaskMembership());
            }
        }
        // taskExecutionDetail
        if (update.getTaskExecutionDetail() != null) {
            if (!(this.getTaskExecutionDetail() != null)) {
                this.setTaskExecutionDetail(update.getTaskExecutionDetail());
            } else {
                this.getTaskExecutionDetail().setExecutionWindow(update.getTaskExecutionDetail().getExecutionWindow());
                this.getTaskExecutionDetail().setCurrentExecutionStatus(update.getTaskExecutionDetail().getCurrentExecutionStatus());
                this.getTaskExecutionDetail().setExecutionCommand(update.getTaskExecutionDetail().getExecutionCommand());
                this.getTaskExecutionDetail().setUpdateInstant(update.getTaskExecutionDetail().getUpdateInstant());
            }

        }
        // registered
        this.setRegistered(update.isRegistered());
        // done... except for updateInstant
        this.setUpdateInstant(update.getUpdateInstant());
        // now, definitely done!
        return (this);
    }
}
