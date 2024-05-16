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
import net.fhirfactory.dricats.core.model.task.PetasosTask;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.TaskCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskCodeFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskCodeFactory.class);

    private static final String DRICATS_TASK_CODE_TASK_TYPE = "/extension/task-code/task_type";
    private static final String DRICATS_TASK_CODE_TASK_SUBTYPE = "/extension/task-code/task_subtype";

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

    public String getTaskCodeTaskTypeSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_CODE_TASK_TYPE;
        return (system);
    }

    public String getTaskCodeTasSubtypeSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_CODE_TASK_SUBTYPE;
        return (system);
    }

    //
    // Business Methods (to FHIR)
    //

    public CodeableConcept extractTaskCodeFromPetasosTask(PetasosTask petasosTask) {
        getLogger().debug(".extractTaskCodeFromPetasosTask(): Entry");

        TaskCode taskCode = TaskCode.FULFILL;
        Coding coding = new Coding();
        coding.setCode(taskCode.toCode());
        coding.setSystem(taskCode.getSystem());
        coding.setDisplay(taskCode.getDisplay());
        CodeableConcept taskCodeCC = new CodeableConcept();
        taskCodeCC.addCoding(coding);
        taskCodeCC.setText(taskCode.getDisplay());

        getLogger().trace(".extractTaskCodeFromPetasosTask(): [Add TaskType] Start");

        if (petasosTask != null) {
            if (petasosTask.getTaskType() != null) {
                if (petasosTask.getTaskType().hasTaskType()) {
                    Extension taskTypeExtension = new Extension();
                    taskTypeExtension.setUrl(getTaskCodeTaskTypeSystemURL());
                    StringType taskType = new StringType(petasosTask.getTaskType().getTaskType().getTaskTypeToken());
                    taskTypeExtension.setValue(taskType);
                    taskCodeCC.addExtension(taskTypeExtension);
                }
                if (petasosTask.getTaskType().hasTaskSubType()) {
                    Extension taskSubtypeExtension = new Extension();
                    taskSubtypeExtension.setUrl(getTaskCodeTasSubtypeSystemURL());
                    StringType taskType = new StringType(petasosTask.getTaskType().getTaskSubType());
                    taskSubtypeExtension.setValue(taskType);
                    taskCodeCC.addExtension(taskSubtypeExtension);
                }
            }
        }

        getLogger().debug(".extractTaskCodeFromPetasosTask(): Exit");
        return taskCodeCC;
    }

    //
    // Business Methods (from FHIR)
    //

    public TaskTypeType extractTaskTypeFromFHIRTask(Task task) {
        getLogger().debug(".extractTaskTypeFromFHIRTask(): Entry");
        if (task == null) {
            getLogger().debug(".extractTaskTypeFromFHIRTask(): Exit, task is null/empty");
            return (null);
        }
        if (!task.hasCode()) {
            getLogger().debug(".extractTaskTypeFromFHIRTask(): Exit, task does not have a -code-");
            return (null);
        }
        CodeableConcept taskCode = task.getCode();
        if (!taskCode.hasExtension(getTaskCodeTaskTypeSystemURL())) {
            getLogger().debug(".extractTaskTypeFromFHIRTask(): Exit, taskCode does not have a TaskType extension");
            return (null);
        }

        TaskTypeType taskTypeEntity = new TaskTypeType();

        Extension taskTypeExtension = taskCode.getExtensionByUrl(getTaskCodeTaskTypeSystemURL());
        StringType taskType = (StringType) taskTypeExtension.getValue();
        String taskTypeTokenString = taskType.getValue();
        TaskTypeTypeEnum taskTypeEnum = TaskTypeTypeEnum.fromTokenValue(taskTypeTokenString);
        if (taskTypeEnum == null) {
            taskTypeEnum = TaskTypeTypeEnum.PETASOS_BASE_TASK_TYPE;
        }
        taskTypeEntity.setTaskType(taskTypeEnum);

        if (taskCode.hasExtension(getTaskCodeTasSubtypeSystemURL())) {
            Extension taskSubtypeExtension = taskCode.getExtensionByUrl(getTaskCodeTasSubtypeSystemURL());
            StringType taskSubtypeExtensionValue = (StringType) taskSubtypeExtension.getValue();
            String taskSubtypeValue = taskSubtypeExtensionValue.getValue();
            taskTypeEntity.setTaskSubType(taskSubtypeValue);
        }
        getLogger().debug(".extractTaskTypeFromFHIRTask(): Exit");
        return (taskTypeEntity);
    }
}
