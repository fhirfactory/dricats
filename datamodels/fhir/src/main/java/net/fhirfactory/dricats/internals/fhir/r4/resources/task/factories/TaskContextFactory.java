/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskTriggerSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskContextFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskContextFactory.class);

    private static final String DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_NAME = "/extension/task-based-on/trigger-name";
    private static final String DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_DESCRIPTION = "/extension/task-based-on/trigger-description";
    private static final String DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_LOCATION = "/extension/task-based-on/trigger-location";

    @Inject
    private TaskIdentifierFactory taskIdentifierFactory;

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected TaskIdentifierFactory getTaskIdentifierFactory() {
        return (taskIdentifierFactory);
    }

    public String getTaskBasedOnTriggerNameSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_NAME;
        return (system);
    }

    public String getTaskBasedOnTriggerDescriptionSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_DESCRIPTION;
        return (system);
    }

    public String getTaskBasedOnTriggerLocationSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_BASED_ON_REFERENCE_TRIGGER_LOCATION;
        return (system);
    }

    //
    // Business Logic (to FHIR)
    //

    public Reference extractBasedOnFromTaskContext(PetasosActionableTask actionableTask) {
        getLogger().debug(".extractBasedOnFromTaskContext(): Entry");

        TaskIdType triggerTaskId = null;
        if (actionableTask.getTaskContext() != null) {
            if (actionableTask.getTaskContext().hasTaskTriggerSummary()) {
                if (actionableTask.getTaskContext().getTaskTriggerSummary().hasTriggerTaskId()) {
                    triggerTaskId = actionableTask.getTaskContext().getTaskTriggerSummary().getTriggerTaskId();
                }
            }
        }
        if (triggerTaskId == null) {
            getLogger().debug(".specifyBasedOn(): Exit, returning null (no trigger task id found");
            return (null);
        }
        Identifier triggerTaskIdentifier = getTaskIdentifierFactory().newTaskIdentifier(TaskTypeTypeEnum.PETASOS_ACTIONABLE_TASK_TYPE, triggerTaskId);
        Reference triggerTaskReference = new Reference();
        triggerTaskReference.setIdentifier(triggerTaskIdentifier);
        triggerTaskReference.setDisplay(triggerTaskId.getLocalId());
        triggerTaskReference.setType(ResourceType.Task.toString());

        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Name Extension] Start");
        if (actionableTask.getTaskContext().getTaskTriggerSummary().hasTriggerName()) {
            Extension triggerNameExtension = extractTriggerNameExtensionFromTaskTrigger(actionableTask.getTaskContext().getTaskTriggerSummary());
            if (triggerNameExtension != null) {
                triggerTaskReference.addExtension(triggerNameExtension);
            }
        }
        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Name Extension] Finish");

        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Description Extension] Start");
        if (actionableTask.getTaskContext().getTaskTriggerSummary().hasTriggerDescription()) {
            Extension triggerDescriptionExtension = extractTriggerDescriptionExtensionFromTaskTrigger(actionableTask.getTaskContext().getTaskTriggerSummary());
            if (triggerDescriptionExtension != null) {
                triggerTaskReference.addExtension(triggerDescriptionExtension);
            }
        }
        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Description Extension] Finish");

        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Location Extension] Start");
        if (actionableTask.getTaskContext().getTaskTriggerSummary().hasTriggerLocation()) {
            Extension triggerLocationExtension = extractTriggerLocationExtensionFromTaskTrigger(actionableTask.getTaskContext().getTaskTriggerSummary());
            if (triggerLocationExtension != null) {
                triggerTaskReference.addExtension(triggerLocationExtension);
            }
        }
        getLogger().trace(".extractBasedOnFromTaskContext(): [Add Trigger Location Extension] Finish");

        getLogger().debug(".extractBasedOnFromTaskContext(): Exit");
        return (triggerTaskReference);
    }

    public Extension extractTriggerNameExtensionFromTaskTrigger(TaskTriggerSummaryType taskTrigger) {
        getLogger().debug(".extractTriggerNameExtensionFromTaskTrigger(): Entry");
        if (taskTrigger == null) {
            getLogger().debug(".extractTriggerNameExtensionFromTaskTrigger(): Exit, taskTrigger is null/empty");
            return (null);
        }
        if (StringUtils.isEmpty(taskTrigger.getTriggerName())) {
            getLogger().debug(".extractTriggerNameExtensionFromTaskTrigger(): Exit, taskTrigger.getTriggerName() is null/empty");
            return (null);
        }
        Extension triggerNameExtension = new Extension();
        triggerNameExtension.setUrl(getTaskBasedOnTriggerNameSystemURL());
        StringType triggerName = new StringType(taskTrigger.getTriggerName());
        triggerNameExtension.setValue(triggerName);
        getLogger().debug(".extractTriggerNameExtensionFromTaskTrigger(): Exit");
        return (triggerNameExtension);
    }

    public Extension extractTriggerDescriptionExtensionFromTaskTrigger(TaskTriggerSummaryType taskTrigger) {
        getLogger().debug(".extractTriggerDescriptionExtensionFromTaskTrigger(): Entry");
        if (taskTrigger == null) {
            getLogger().debug(".extractTriggerDescriptionExtensionFromTaskTrigger(): Exit, taskTrigger is null/empty");
            return (null);
        }
        if (StringUtils.isEmpty(taskTrigger.getTriggerDescription())) {
            getLogger().debug(".extractTriggerDescriptionExtensionFromTaskTrigger(): Exit, taskTrigger.getTriggerDescription() is null/empty");
            return (null);
        }
        Extension triggerDescriptionExtension = new Extension();
        triggerDescriptionExtension.setUrl(getTaskBasedOnTriggerDescriptionSystemURL());
        StringType triggerDescription = new StringType(taskTrigger.getTriggerDescription());
        triggerDescriptionExtension.setValue(triggerDescription);
        getLogger().debug(".extractTriggerDescriptionExtensionFromTaskTrigger(): Exit");
        return (triggerDescriptionExtension);
    }

    public Extension extractTriggerLocationExtensionFromTaskTrigger(TaskTriggerSummaryType taskTrigger) {
        getLogger().debug(".extractTriggerLocationExtensionFromTaskTrigger(): Entry");
        if (taskTrigger == null) {
            getLogger().debug(".extractTriggerLocationExtensionFromTaskTrigger(): Exit, taskTrigger is null/empty");
            return (null);
        }
        if (StringUtils.isEmpty(taskTrigger.getTriggerLocation())) {
            getLogger().debug(".extractTriggerLocationExtensionFromTaskTrigger(): Exit, taskTrigger.getTriggerLocation() is null/empty");
            return (null);
        }
        Extension triggerLocationExtension = new Extension();
        triggerLocationExtension.setUrl(getTaskBasedOnTriggerLocationSystemURL());
        StringType triggerLocation = new StringType(taskTrigger.getTriggerLocation());
        triggerLocationExtension.setValue(triggerLocation);
        getLogger().debug(".extractTriggerLocationExtensionFromTaskTrigger(): Exit");
        return (triggerLocationExtension);
    }

    //
    // Business Logic (from FHIR)
    //

    public TaskTriggerSummaryType extractTriggerSummaryFromFHIRTask(Task fhirTask) {
        getLogger().debug(".extractTriggerSummaryFromFHIRTask(): Entry");
        if (fhirTask == null) {
            getLogger().debug(".extractTriggerSummaryFromFHIRTask(): Exit, fhirTask is null/empty");
            return (null);
        }
        if (!fhirTask.hasBasedOn() || fhirTask.getBasedOn().isEmpty()) {
            getLogger().debug(".extractTriggerSummaryFromFHIRTask(): Exit, fhirTask.getBasedOn() is empty/null");
            return (null);
        }

        Reference triggerReference = null;
        for (Reference currentReference : fhirTask.getBasedOn()) {
            for (Coding currentCoding : currentReference.getIdentifier().getType().getCoding()) {
                if (currentCoding.getCode().contentEquals(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_ACTIONABLE_TASK.getToken())) {
                    triggerReference = currentReference;
                    break;
                }
            }
            if (triggerReference != null) {
                break;
            }
        }

        if (triggerReference == null) {
            getLogger().debug(".extractTriggerSummaryFromFHIRTask(): Exit, Cannot resolve fhirTask.getBasedOnFirstRep()");
            return (null);
        }

        TaskTriggerSummaryType taskTrigger = null;
        try {

            taskTrigger = new TaskTriggerSummaryType();

            TaskIdType triggerTaskId = new TaskIdType();
            triggerTaskId.setId(triggerReference.getIdentifier().getValue());
            triggerTaskId.setPrimaryBusinessIdentifier(triggerReference.getIdentifier());

            String triggerLocation = extractTriggerLocationFromBasedOnReference(triggerReference);
            if (StringUtils.isNotEmpty(triggerLocation)) {
                taskTrigger.setTriggerLocation(triggerLocation);
            }

            String triggerDescription = extractTriggerDescriptionFromBasedOnReference(triggerReference);
            if (StringUtils.isNotEmpty(triggerDescription)) {
                taskTrigger.setTriggerDescription(triggerDescription);
            }

            String triggerName = extractTriggerNameFromBasedOnReference(triggerReference);
            if (StringUtils.isNotEmpty(triggerName)) {
                taskTrigger.setTriggerName(triggerName);
            }

        } catch (Exception ex) {
            getLogger().warn(".extractTriggerSummaryFromFHIRTask(): Could not extract Trigger from FHIR::Task, error->", ex);
        }
        getLogger().debug(".extractTriggerSummaryFromFHIRTask(): Exit");
        return (taskTrigger);
    }

    public String extractTriggerLocationFromBasedOnReference(Reference basedOnReference) {
        getLogger().debug(".extractTriggerLocationFromBasedOnReference(): Entry");
        if (basedOnReference == null) {
            getLogger().debug(".extractTriggerLocationFromBasedOnReference(): Exit, basedOnReference is null/empty");
            return (null);
        }
        if (!basedOnReference.hasExtension(getTaskBasedOnTriggerLocationSystemURL())) {
            getLogger().debug(".extractTriggerLocationFromBasedOnReference(): Exit, basedOnReference does not have a TriggerLocation extension");
            return (null);
        }
        Extension triggerLocationExtension = basedOnReference.getExtensionByUrl(getTaskBasedOnTriggerLocationSystemURL());
        StringType triggerLocation = (StringType) triggerLocationExtension.getValue();
        String location = triggerLocation.getValue();
        getLogger().debug(".extractTriggerLocationFromBasedOnReference(): Exit");
        return (location);
    }

    public String extractTriggerDescriptionFromBasedOnReference(Reference basedOnReference) {
        getLogger().debug(".extractTriggerDescriptionFromBasedOnReference(): Entry");
        if (basedOnReference == null) {
            getLogger().debug(".extractTriggerDescriptionFromBasedOnReference(): Exit, basedOnReference is null/empty");
            return (null);
        }
        if (!basedOnReference.hasExtension(getTaskBasedOnTriggerDescriptionSystemURL())) {
            getLogger().debug(".extractTriggerDescriptionFromBasedOnReference(): Exit,basedOnReference does not have a TriggerDescription extension");
            return (null);
        }
        Extension triggerDescriptionExtension = basedOnReference.getExtensionByUrl(getTaskBasedOnTriggerDescriptionSystemURL());
        StringType triggerDescription = (StringType) triggerDescriptionExtension.getValue();
        String description = triggerDescription.getValue();
        getLogger().debug(".extractTriggerDescriptionFromBasedOnReference(): Exit");
        return (description);
    }

    public String extractTriggerNameFromBasedOnReference(Reference basedOnReference) {
        getLogger().debug(".extractTriggerNameFromBasedOnReference(): Entry");
        if (basedOnReference == null) {
            getLogger().debug(".extractTriggerNameFromBasedOnReference(): Exit, basedOnReference is null/empty");
            return (null);
        }
        if (!basedOnReference.hasExtension(getTaskBasedOnTriggerNameSystemURL())) {
            getLogger().debug(".extractTriggerNameFromBasedOnReference(): Exit,basedOnReference does not have a TriggerName extension");
            return (null);
        }
        Extension triggerNameExtension = basedOnReference.getExtensionByUrl(getTaskBasedOnTriggerNameSystemURL());
        StringType triggerName = (StringType) triggerNameExtension.getValue();
        String name = triggerName.getValue();
        getLogger().debug(".extractTriggerNameFromBasedOnReference(): Exit");
        return (name);
    }
}
