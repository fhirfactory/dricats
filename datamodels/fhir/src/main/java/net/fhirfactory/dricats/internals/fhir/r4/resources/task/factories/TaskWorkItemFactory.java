package net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemType;
import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWIdentifier;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import net.fhirfactory.dricats.core.model.uow.UoWProcessingOutcomeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskWorkItemFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskWorkItemFactory.class);
    private static final String PEGACORN_TASK_PARAMETER_COMPONENT_DATA_PARCEL_DESCRIPTOR = "/task-parameter-component-data-parcel-descriptor";
    private static final String DRICATS_TASK_PROCESSING_OUTCOME = "/extension/task-output/task-processing-outcome";
    private static final String DRICATS_TASK_PROCESSING_OUTCOME_DESCRIPTION = "/extension/task-output/task-processing-outcome";
    private static final String DRICATS_TASK_INPUT_TYPE = "/extension/task-input/task-input-type";
    private static final String DRICATS_TASK_INPUT_INSTANT_ID = "/extension/task-input/task-input-instant-id";
    private ObjectMapper jsonMapper;
    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    public TaskWorkItemFactory() {
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JavaTimeModule module = new JavaTimeModule();
        this.jsonMapper.registerModule(module);
        this.jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    //
    // Getters (and Setters)
    //

    public String getDricatsTaskProcessingOutcomeSystemURL() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_PROCESSING_OUTCOME;
        return (codeSystem);
    }

    public String getDricatsTaskProcessingOutcomeDescriptionSystemURL() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_PROCESSING_OUTCOME_DESCRIPTION;
        return (codeSystem);
    }

    public String getDricatsTaskInputTypeSystemURL() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_INPUT_TYPE;
        return (codeSystem);
    }

    public String getDricatsTaskInputInstantIdSystemURL() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_INPUT_INSTANT_ID;
        return (codeSystem);
    }

    public String getPegacornTaskParameterComponentDataParcelDescriptor() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + PEGACORN_TASK_PARAMETER_COMPONENT_DATA_PARCEL_DESCRIPTOR;
        return (codeSystem);
    }

    //
    // Business Methods (to FHIR)
    //

    public Task.ParameterComponent newInputWorkItemPayload(UoWPayload payload) {
        Task.ParameterComponent parameterComponent = newInputWorkItemPayload(payload, null);
        return (parameterComponent);
    }

    public Task.ParameterComponent newInputWorkItemPayload(UoWPayload payload, TaskWorkItemType petasosTaskWorkItem) {
        UoW uow = (UoW) petasosTaskWorkItem;
        Task.ParameterComponent parameterComponent = newInputWorkItemPayload(payload, uow);
        return (parameterComponent);
    }

    public Task.ParameterComponent newInputWorkItemPayload(UoWPayload payload, UoW petasosTaskWorkItem) {
        String parcelManifestAsString = mapDataParcelManifestToString(payload.getPayloadManifest());
        if (StringUtils.isEmpty(parcelManifestAsString)) {
            return (null);
        }
        CodeableConcept workItemType = new CodeableConcept();
        Coding workItemTypeCoding = new Coding();
        workItemTypeCoding.setSystem(getPegacornTaskParameterComponentDataParcelDescriptor());
        workItemTypeCoding.setCode(parcelManifestAsString);
        workItemType.addCoding(workItemTypeCoding);
        Task.ParameterComponent taskWorkItem = new Task.ParameterComponent();
        taskWorkItem.setType(workItemType);
        taskWorkItem.setValue(new StringType(payload.getPayload()));

        Extension taskInputTypeExtension = extractTaskTypeIdFromPetasosTaskWorkItem(petasosTaskWorkItem);
        if (taskInputTypeExtension != null) {
            taskWorkItem.addExtension(taskInputTypeExtension);
        }
        Extension taskUoWIdExtension = extractTaskUoWIdFromPetasosTaskWorkItem(petasosTaskWorkItem);
        if (taskUoWIdExtension != null) {
            taskWorkItem.addExtension(taskUoWIdExtension);
        }
        return (taskWorkItem);
    }

    protected Extension extractTaskTypeIdFromPetasosTaskWorkItem(TaskWorkItemType taskWorkItem) {
        if (taskWorkItem == null) {
            return (null);
        }
        UoW uow = (UoW) taskWorkItem;
        Extension extension = extractTaskTypeIdFromPetasosTaskWorkItem(uow);
        return (extension);
    }

    protected Extension extractTaskTypeIdFromPetasosTaskWorkItem(UoW taskWorkItem) {
        if (taskWorkItem == null) {
            return (null);
        }
        if (taskWorkItem.getTypeID() == null) {
            return (null);
        }
        String tokenValue = taskWorkItem.getTypeID().getContent();
        StringType tokenValueType = new StringType(tokenValue);
        Extension taskTypeExtension = new Extension();
        taskTypeExtension.setUrl(getDricatsTaskInputTypeSystemURL());
        taskTypeExtension.setValue(tokenValueType);
        return (taskTypeExtension);
    }

    protected Extension extractTaskUoWIdFromPetasosTaskWorkItem(TaskWorkItemType taskWorkItem) {
        if (taskWorkItem == null) {
            return (null);
        }
        UoW uow = (UoW) taskWorkItem;
        Extension extension = extractTaskUoWIdFromPetasosTaskWorkItem(uow);
        return (extension);
    }

    protected Extension extractTaskUoWIdFromPetasosTaskWorkItem(UoW taskWorkItem) {
        if (taskWorkItem == null) {
            return (null);
        }
        if (taskWorkItem.getInstanceID() == null) {
            return (null);
        }
        String uowIdValue = taskWorkItem.getInstanceID().getContent();
        StringType uowIdType = new StringType(uowIdValue);
        Extension taskUoWIdExtension = new Extension();
        taskUoWIdExtension.setUrl(getDricatsTaskInputInstantIdSystemURL());
        taskUoWIdExtension.setValue(uowIdType);
        return (taskUoWIdExtension);
    }

    public Task.TaskOutputComponent newOutputWorkItemPayload(UoWPayload payload) {
        Task.TaskOutputComponent taskOutputComponent = newOutputWorkItemPayload(payload, null);
        return (taskOutputComponent);
    }

    public Task.TaskOutputComponent newOutputWorkItemPayload(UoWPayload payload, TaskWorkItemType petasosTaskWorkItem) {
        String parcelManifestAsString = mapDataParcelManifestToString(payload.getPayloadManifest());
        if (StringUtils.isEmpty(parcelManifestAsString)) {
            return (null);
        }
        CodeableConcept workItemType = new CodeableConcept();
        Coding workItemTypeCoding = new Coding();
        workItemTypeCoding.setSystem(getPegacornTaskParameterComponentDataParcelDescriptor());
        workItemTypeCoding.setCode(parcelManifestAsString);
        workItemType.addCoding(workItemTypeCoding);
        Task.TaskOutputComponent taskWorkItem = new Task.TaskOutputComponent();
        taskWorkItem.setType(workItemType);
        taskWorkItem.setValue(new StringType(payload.getPayload()));

        Extension taskOutcomeExtension = extractUoWProcessingOutcomeExtensionFromPetasosTaskWorkItem(petasosTaskWorkItem);
        if (taskOutcomeExtension != null) {
            taskWorkItem.addExtension(taskOutcomeExtension);
        }
        Extension failureDescriptionExtension = extractUoWProcessingOutcomeDescriptionExtensionFromPetasosTaskWorkItem(petasosTaskWorkItem);
        if (failureDescriptionExtension != null) {
            taskWorkItem.addExtension(failureDescriptionExtension);
        }

        return (taskWorkItem);
    }

    protected String mapDataParcelManifestToString(DataParcelManifest parcelManifest) {
        try {
            String s = getJSONMapper().writeValueAsString(parcelManifest);
            return (s);
        } catch (JsonProcessingException e) {
            getLogger().error(".mapDataParcelManifestToString(): Error -->{}", ExceptionUtils.getStackTrace(e));
            return (null);
        }
    }

    protected Extension extractUoWProcessingOutcomeExtensionFromPetasosTaskWorkItem(TaskWorkItemType petasosTaskWorkItem) {
        if (petasosTaskWorkItem == null) {
            return (null);
        }
        if (petasosTaskWorkItem.getProcessingOutcome() == null) {
            return (null);
        }
        String processingOutcome = petasosTaskWorkItem.getProcessingOutcome().getToken();
        StringType processingOutcomeType = new StringType(processingOutcome);
        Extension processingOutcomeExtension = new Extension();
        processingOutcomeExtension.setUrl(getDricatsTaskProcessingOutcomeSystemURL());
        processingOutcomeExtension.setValue(processingOutcomeType);
        return (processingOutcomeExtension);
    }

    protected Extension extractUoWProcessingOutcomeDescriptionExtensionFromPetasosTaskWorkItem(TaskWorkItemType petasosTaskWorkItem) {
        if (petasosTaskWorkItem == null) {
            return (null);
        }
        if (StringUtils.isEmpty(petasosTaskWorkItem.getFailureDescription())) {
            return (null);
        }
        String failureDescription = petasosTaskWorkItem.getFailureDescription();
        StringType failureDescriptionType = new StringType(failureDescription);
        Extension failureDescriptionExtension = new Extension();
        failureDescriptionExtension.setUrl(getDricatsTaskProcessingOutcomeDescriptionSystemURL());
        failureDescriptionExtension.setValue(failureDescriptionType);
        return (failureDescriptionExtension);
    }

    //
    // Business Methods (from FHIR)
    //

    public TaskWorkItemType extractPetasosTaskWorkItemFromFHIRTask(Task fhirTask) {
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

        UoWIdentifier uoWIdentifier = extractUoWIdentifierFromFHIRTask(fhirTask);
        if (uoWIdentifier != null) {
            taskWorkItem.setInstanceID(uoWIdentifier);
        }

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

        UoWProcessingOutcomeEnum outcomeEnum = extractUoWOutcomeFromFHIRTask(fhirTask);
        if (outcomeEnum != null) {
            taskWorkItem.setProcessingOutcome(outcomeEnum);
        } else {
            taskWorkItem.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
        }
        String outcomeDescription = extractUoWOutcomeDescriptionFromFHIRTask(fhirTask);
        if (StringUtils.isNotEmpty(outcomeDescription)) {
            taskWorkItem.setFailureDescription(outcomeDescription);
        }

        return (taskWorkItem);
    }

    protected UoWIdentifier extractUoWIdentifierFromFHIRTask(Task fhirTask) {
        if (fhirTask == null) {
            return (null);
        }
        Task.ParameterComponent currentInputComponent = fhirTask.getInputFirstRep();
        if (currentInputComponent == null) {
            return (null);
        }
        if (currentInputComponent.hasExtension(getDricatsTaskInputInstantIdSystemURL())) {
            try {
                Extension instanceIdExtension = currentInputComponent.getExtensionByUrl(getDricatsTaskInputInstantIdSystemURL());
                StringType instanceIdStringType = (StringType) instanceIdExtension.getValue();
                String instanceIdValue = instanceIdStringType.getValue();
                UoWIdentifier instantId = new UoWIdentifier();
                instantId.setContent(instanceIdValue);
                return (instantId);
            } catch (Exception ex) {
                getLogger().warn(".extractUoWIdentifierFromFHIRTask(): Unable to extract UoWIdentifier, exception->", ex);
            }
        }
        return (null);
    }

    protected UoWProcessingOutcomeEnum extractUoWOutcomeFromFHIRTask(Task fhirTask) {
        if (fhirTask == null) {
            return (null);
        }
        Task.TaskOutputComponent output = fhirTask.getOutputFirstRep();
        if (output == null) {
            return (null);
        }
        if (output.hasExtension(getDricatsTaskProcessingOutcomeSystemURL())) {
            try {
                Extension outcomeExtension = output.getExtensionByUrl(getDricatsTaskProcessingOutcomeSystemURL());
                StringType outcomeStringType = (StringType) outcomeExtension.getValue();
                String outcomeTokenValue = outcomeStringType.getValue();
                UoWProcessingOutcomeEnum outcomeEnum = UoWProcessingOutcomeEnum.fromTokenValue(outcomeTokenValue);
                return (outcomeEnum);
            } catch (Exception ex) {
                getLogger().warn(".extractUoWOutcomeFromFHIRTask(): Unable to extract UoWProcessingOutcome, exception->", ex);
            }
        }
        return (null);
    }

    protected String extractUoWOutcomeDescriptionFromFHIRTask(Task fhirTask) {
        if (fhirTask == null) {
            return (null);
        }
        Task.TaskOutputComponent output = fhirTask.getOutputFirstRep();
        if (output == null) {
            return (null);
        }
        if (output.hasExtension(getDricatsTaskProcessingOutcomeDescriptionSystemURL())) {
            try {
                Extension outcomeDescriptionExtension = output.getExtensionByUrl(getDricatsTaskProcessingOutcomeDescriptionSystemURL());
                StringType outcomeDescriptionStringType = (StringType) outcomeDescriptionExtension.getValue();
                String outcomeDescription = outcomeDescriptionStringType.getValue();
                return (outcomeDescription);
            } catch (Exception ex) {
                getLogger().warn(".extractUoWOutcomeDescriptionFromFHIRTask(): Unable to extract UoWProcessingOutcome, exception->", ex);
            }
        }
        return (null);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected ObjectMapper getJSONMapper() {
        return (this.jsonMapper);
    }
}
