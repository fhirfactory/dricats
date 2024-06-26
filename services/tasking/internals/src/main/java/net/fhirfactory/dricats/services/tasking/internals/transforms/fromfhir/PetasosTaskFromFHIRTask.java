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
package net.fhirfactory.dricats.services.tasking.internals.transforms.fromfhir;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFunctionFDN;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFunctionFDNToken;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.task.PetasosTask;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskBeneficiaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskContextType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskEncounterType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskTriggerSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.performer.datatypes.TaskPerformerTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.datatypes.RetryTaskReasonType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.datatypes.TaskReasonType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.valuesets.TaskReasonTypeEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.TaskOutcomeStatusType;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusReasonEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityType;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemType;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeSystemFactory;
import net.fhirfactory.dricats.internals.fhir.r4.resources.identifier.PegacornIdentifierDataTypeHelpers;
import net.fhirfactory.dricats.internals.fhir.r4.resources.provenance.transformers.FHIRProvenanceToPetasosTaskJourneyTransformer;
import net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PetasosTaskFromFHIRTask {

    private ObjectMapper jsonMapper;

    @Inject
    private PegacornIdentifierDataTypeHelpers identifierHelpers;

    @Inject
    private TaskIdentifierFactory taskIdentifierFactory;

    @Inject
    private PegacornIdentifierCodeSystemFactory identifierCodeSystemFactory;

    @Inject
    private TaskPerformerTypeFactory taskPerformerTypeFactory;

    @Inject
    private TaskStatusReasonFactory taskStatusReasonFactory;

    @Inject
    private FHIRProvenanceToPetasosTaskJourneyTransformer provenanceToTaskJourneyTransformer;

    @Inject
    private TaskReasonFactory taskReasonFactory;

    @Inject
    private TaskPeriodFactory taskPeriodFactory;

    //
    // Constructor(s)
    //

    public PetasosTaskFromFHIRTask() {
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JavaTimeModule module = new JavaTimeModule();
        this.jsonMapper.registerModule(module);
        this.jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    //
    // Abstract Methods
    //

    abstract protected Logger getLogger();

    abstract protected PetasosTask createEmptyPetasosTaskSubclassObject();

    abstract protected TaskTypeTypeEnum getTaskType();

    //
    // Business Methods
    //

    public PetasosTask newPetasosTaskFromTask(Task fhirTask) {
        getLogger().debug(".newPetasosTaskFromTask(): Entry");

        if (fhirTask == null) {
            return (null);
        }

        PetasosTask petasosTask = createEmptyPetasosTaskSubclassObject();

        //
        // Create the TaskIdType
        TaskIdType taskId = new TaskIdType();
        Identifier taskIdentifier = getIdentifierHelpers().findIdentifierWithSystemAndCode(fhirTask.getIdentifier(), getIdentifierCodeSystemFactory().getPegacornIdentifierCodeSystem(), getTaskType().getTaskTypeToken());
        taskId.setLocalId(taskIdentifier.getValue());
        taskId.setPrimaryBusinessIdentifier(taskIdentifier);
        taskId.setResourceId(fhirTask.getIdElement());
        petasosTask.setTaskId(taskId);

        //
        // TaskType
        TaskTypeType taskType = new TaskTypeType();
        taskType.setTaskType(getTaskType());
        // TODO resolve TaskSubType

        getLogger().trace(".newPetasosTaskFromTask(): [Set .setTaskWorkItem()] Start");
        TaskWorkItemType taskWorkItem = extractTaskWorkItemFromFHIRTask(fhirTask);
        if (taskWorkItem != null) {
            petasosTask.setTaskWorkItem(taskWorkItem);
        }
        getLogger().trace(".newPetasosTaskFromTask(): [Set .setTaskWorkItem()] Finish");

        getLogger().trace(".newPetasosTaskFromTask(): [Set .setTaskReason()] Start");
        TaskReasonType taskReason = extractTaskReasonFromFHIRTask(fhirTask);
        if (taskReason != null) {
            petasosTask.setTaskReason(taskReason);
        }
        getLogger().trace(".newPetasosTaskFromTask(): [Set .setTaskReason()] Finish");

        getLogger().trace(".newPetasosTaskFromTask(): [Set .setPerformerTypes()] Start");
        List<TaskPerformerTypeType> performerTypes = extractPerformerTypesFromFHIRTask(fhirTask.getPerformerType());
        if (performerTypes != null) {
            if (!performerTypes.isEmpty()) {
                petasosTask.getTaskPerformerTypes().addAll(performerTypes);
            }
        }
        getLogger().trace(".newPetasosTaskFromTask(): [Set .setPerformerTypes()] Finish");

        // Now add the TaskContext
        TaskContextType taskContext = transformFHIRTaskToTaskContext(fhirTask);
        petasosTask.setTaskContext(taskContext);

        // Now add the AggregateTask membership
        Set<TaskIdType> aggregateTaskMembership = transformFHIRTaskToAggregateTaskMembership(fhirTask);
        if (aggregateTaskMembership != null) {
            if (!aggregateTaskMembership.isEmpty()) {
                petasosTask.getAggregateTaskMembership().addAll(aggregateTaskMembership);
            }
        }

        // Now add the TaskOutcomeStatus
        TaskOutcomeStatusType outcomeStatus = extractOutcomeStatusFromFHIRTask(fhirTask);
        if (outcomeStatus != null) {
            petasosTask.setTaskOutcomeStatus(outcomeStatus);
        }

        return (petasosTask);
    }

    protected TaskReasonType extractTaskReasonFromFHIRTask(Task fhirTask) {
        getLogger().debug(".extractTaskReasonFromFHIRTask(): Entry");
        TaskReasonTypeEnum taskReasonTypeEnum = null;
        RetryTaskReasonType taskRetryReason = null;
        if (fhirTask.hasReasonCode()) {
            taskReasonTypeEnum = TaskReasonTypeEnum.fromTaskReasonCode(fhirTask.getReasonCode().getCodingFirstRep().getCode());
        }
        if (fhirTask.hasReasonReference()) {
            taskRetryReason = taskReasonFactory.extractRetryTaskReasonFromReference(fhirTask.getReasonReference());
        }
        if (taskRetryReason != null || taskReasonTypeEnum != null) {
            TaskReasonType taskReason = new TaskReasonType();
            taskReason.setReasonType(taskReasonTypeEnum);
            if (taskRetryReason != null) {
                taskReason.setRetryTask(true);
                taskReason.setRetryTaskDetail(taskRetryReason);
            }
            return (taskReason);
        }
        getLogger().debug(".extractTaskReasonFromFHIRTask(): Exit");
        return (null);
    }

    protected List<TaskPerformerTypeType> extractPerformerTypesFromFHIRTask(List<CodeableConcept> performers) {
        getLogger().debug(".transformPerformerCodeableConceptIntoTaskPerformerType(): Entry");
        if (performers == null) {
            getLogger().debug(".transformPerformerCodeableConceptIntoTaskPerformerType(): Exit, performers is null");
            return (new ArrayList<>());
        }
        if (performers.isEmpty()) {
            getLogger().debug(".transformPerformerCodeableConceptIntoTaskPerformerType(): Exit, performers is empty");
            return (new ArrayList<>());
        }
        List<TaskPerformerTypeType> taskPerformers = new ArrayList<>();
        for (CodeableConcept currentPerformer : performers) {
            String taskPerformerTypeName = taskPerformerTypeFactory.extractCodeFromCodeableConceptForTaskPerformerType(currentPerformer);
            TaskPerformerTypeType taskPerformerType = new TaskPerformerTypeType();
            TopologyNodeFunctionFDNToken functionToken = new TopologyNodeFunctionFDNToken();
            functionToken.setToken(taskPerformerTypeName);
            TopologyNodeFunctionFDN functionFDN = new TopologyNodeFunctionFDN(functionToken);
            taskPerformerType.setRequiredPerformerType(functionFDN);
            taskPerformerType.setRequiredPerformerTypeDescription(currentPerformer.getText());
            taskPerformers.add(taskPerformerType);
        }
        getLogger().debug(".transformPerformerCodeableConceptIntoTaskPerformerType(): Exit, taskPerformers->{}", taskPerformers);
        return (taskPerformers);
    }

    protected TaskWorkItemType extractTaskWorkItemFromFHIRTask(Task fhirTask) {
        TaskWorkItemType taskWorkItem = new TaskWorkItemType();
        UoWPayload inputPayload = new UoWPayload();
        Task.ParameterComponent currentInputComponent = fhirTask.getInputFirstRep();
        String parcelManifestAsString = currentInputComponent.getType().getCodingFirstRep().getCode();
        try {
            DataParcelManifest dataParcelManifest = getJSONMapper().readValue(parcelManifestAsString, DataParcelManifest.class);
            inputPayload.setPayloadManifest(dataParcelManifest);
        } catch (JsonProcessingException e) {
            getLogger().warn(".transformFHIRTaskToTaskWorkItem(): Could not resolve Data Parcel Manifest for Task Work Item Input Payload! StackTrace->{}", ExceptionUtils.getStackTrace(e));
        }
        StringType inputPayloadValue = (StringType) currentInputComponent.getValue();
        String inputPayloadString = inputPayloadValue.getValue();
        inputPayload.setPayload(inputPayloadString);
        taskWorkItem.setIngresContent(inputPayload);
        // now the Task Work Item egress payload
        for (Task.TaskOutputComponent currentOutput : fhirTask.getOutput()) {
            UoWPayload currentOutputPayload = new UoWPayload();
            String currentOutputParcelManifestString = currentOutput.getType().getCodingFirstRep().getCode();
            try {
                DataParcelManifest dataParcelManifest = getJSONMapper().readValue(currentOutputParcelManifestString, DataParcelManifest.class);
                currentOutputPayload.setPayloadManifest(dataParcelManifest);
            } catch (JsonProcessingException e) {
                getLogger().warn(".transformFHIRTaskToTaskWorkItem(): Could not resolve Data Parcel Manifest for Task Work Item Output Payload! StackTrace->{}", ExceptionUtils.getStackTrace(e));
            }
            StringType currentOutputPayloadStringType = (StringType) currentInputComponent.getValue();
            String currentOutputPayloadString = currentOutputPayloadStringType.getValue();
            taskWorkItem.getEgressContent().addPayloadElement(currentOutputPayload);
        }
        return (taskWorkItem);
    }


    protected TaskContextType transformFHIRTaskToTaskContext(Task fhirTask) {
        getLogger().debug(".transformFHIRTaskToTaskContext(): Entry");
        if (fhirTask == null) {
            getLogger().debug(".transformFHIRTaskToTaskContext(): Exit, fhirTask is null");
            return (null);
        }
        TaskContextType taskContext = new TaskContextType();
        TaskTriggerSummaryType taskTriggerSummary = new TaskTriggerSummaryType();
        //
        // Check to see if fhirTask has .basedOn and add it as the TriggerSummary
        if (fhirTask.hasBasedOn()) {
            TaskIdType taskId = new TaskIdType();
            String taskIdString = null;
            Identifier identifier = null;
            boolean found = false;
            for (Reference currentReference : fhirTask.getBasedOn()) {
                for (Coding currentCoding : currentReference.getIdentifier().getType().getCoding()) {
                    if (currentCoding.getCode().contentEquals(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_ACTIONABLE_TASK.getToken())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    identifier = currentReference.getIdentifier();
                    taskIdString = identifier.getValue();
                }
            }
            taskId.setLocalId(taskIdString);
            taskId.setPrimaryBusinessIdentifier(identifier);
            taskTriggerSummary.setTriggerTaskId(taskId);
            taskContext.setTaskTriggerSummary(taskTriggerSummary);
        }
        // Check to see if the fhirTask has a .hasFor (Beneficiary) and add that to the TaskContext
        if (fhirTask.hasFor()) {
            TaskBeneficiaryType taskBeneficiary = new TaskBeneficiaryType(fhirTask.getFor());
            taskContext.setTaskBeneficiary(taskBeneficiary);
        }
        // Check to see if the fhirTask has a .encounter and add that to the TaskContext
        if (fhirTask.hasEncounter()) {
            TaskEncounterType taskEncounter = new TaskEncounterType(fhirTask.getEncounter());
            taskContext.setTaskEncounter(taskEncounter);
        }
        getLogger().debug(".transformFHIRTaskToTaskContext(): Exit, taskContext->{}", taskContext);
        return (taskContext);
    }

    protected Set<TaskIdType> transformFHIRTaskToAggregateTaskMembership(Task fhirTask) {
        getLogger().debug(".transformFHIRTaskToAggregateTaskMembership(): Entry");
        if (fhirTask == null) {
            getLogger().debug(".transformFHIRTaskToAggregateTaskMembership(): Exit, fhirTask is null");
            return (new HashSet<>());
        }
        Set<TaskIdType> membershipSet = new HashSet<>();
        if (fhirTask.hasPartOf()) {
            for (Reference currentReference : fhirTask.getPartOf()) {
                TaskIdType currentTaskId = new TaskIdType();
                currentTaskId.setPrimaryBusinessIdentifier(currentReference.getIdentifier());
                currentTaskId.setLocalId(currentReference.getIdentifier().getValue());
                membershipSet.add(currentTaskId);
            }
        }
        getLogger().debug(".transformFHIRTaskToAggregateTaskMembership(): Exit, membershipSet->{}", membershipSet);
        return (membershipSet);
    }

    protected TaskOutcomeStatusType extractOutcomeStatusFromFHIRTask(Task fhirTask) {
        getLogger().debug(".transformFHIRTaskToTaskOutcomeStatus(): Entry");
        if (fhirTask == null) {
            getLogger().debug(".transformFHIRTaskToTaskOutcomeStatus(): Exit, fhirTask is null");
            return (null);
        }
        TaskOutcomeStatusType outcomeStatus = new TaskOutcomeStatusType();
        if (fhirTask.hasStatus()) {
            switch (fhirTask.getStatus()) {
                case CANCELLED:
                case DRAFT:
                case REQUESTED:
                case RECEIVED:
                case REJECTED:
                case ENTEREDINERROR:
                case ACCEPTED:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_CANCELLED);
                    break;
                case READY:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_WAITING);
                    break;
                case INPROGRESS:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_ACTIVE);
                    break;
                case FAILED:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_FAILED);
                    break;
                case COMPLETED:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_FINISHED);
                    break;
                case ONHOLD:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_WAITING);
                case NULL:
                default:
                    outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_UNKNOWN);
                    break;
            }

            //
            // Check for finalisation
            if (fhirTask.hasStatusReason()) {
                CodeableConcept statusReason = fhirTask.getStatusReason();
                boolean hasFinalisationInfo = false;
                for (Coding currentCoding : statusReason.getCoding()) {
                    if (currentCoding.getSystem().contentEquals(taskStatusReasonFactory.getTaskStatusReasonSystem())) {
                        if (currentCoding.getCode().contentEquals(taskStatusReasonFactory.getTaskIsFinalisedCode())) {
                            outcomeStatus.setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_FINALISED);
                            break;
                        }
                    }
                }
            }

            getLogger().debug(".transformFHIRTaskToTaskOutcomeStatus(): Exit, outcomeStatus->{}", outcomeStatus);
            return (outcomeStatus);
        }
        getLogger().debug(".transformFHIRTaskToTaskOutcomeStatus(): Exit, FHIR::Task has no .status element, returning null");
        return (null);
    }

    public PetasosTask enrichPetasosActionableTaskWithTraceabilityDetails(PetasosTask petasosTask, Provenance fhirProvenance) {
        getLogger().debug(".enrichPetasosActionableTaskWithTraceabilityDetails(): Entry");
        if (petasosTask == null) {
            getLogger().debug(".enrichPetasosActionableTaskWithTraceabilityDetails(): Exit, petasosTask is null");
            return (null);
        }
        if (fhirProvenance == null) {
            getLogger().debug(".enrichPetasosActionableTaskWithTraceabilityDetails(): Exit, fhirProvenance is null");
        }
        TaskTraceabilityType taskTraceability = provenanceToTaskJourneyTransformer.newTaskTraceabilityFromFHIRProvenance(petasosTask.getTaskId(), fhirProvenance);
        petasosTask.setTaskTraceability(taskTraceability);
        getLogger().debug(".enrichPetasosActionableTaskWithTraceabilityDetails(): Exit");
        return (petasosTask);
    }


    //
    // Getters (and Setters)
    //

    protected PegacornIdentifierDataTypeHelpers getIdentifierHelpers() {
        return (this.identifierHelpers);
    }

    protected TaskIdentifierFactory getTaskIdentifierFactory() {
        return (this.taskIdentifierFactory);
    }

    protected PegacornIdentifierCodeSystemFactory getIdentifierCodeSystemFactory() {
        return (this.identifierCodeSystemFactory);
    }

    protected ObjectMapper getJSONMapper() {
        return (this.jsonMapper);
    }

}
