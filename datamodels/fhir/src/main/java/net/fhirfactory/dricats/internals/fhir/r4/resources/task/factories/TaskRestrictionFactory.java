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
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskWindow;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.valuesets.TaskWindowBoundaryConditionEnum;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

@ApplicationScoped
public class TaskRestrictionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskRestrictionFactory.class);

    private static final String TASK_RESTRICTION_PERIOD_START_QUALIFIER_EXTENSION_SYSTEM = "/extension/task-restriction/task-start-qualifier";
    private static final String TASK_RESTRICTION_PERIOD_END_QUALIFIER_EXTENSION_SYSTEM = "/extension/task-restriction/task-end-qualifier";

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    public String getTaskRestrictionPeriodStartQualifierExtensionSystem() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + TASK_RESTRICTION_PERIOD_START_QUALIFIER_EXTENSION_SYSTEM;
        return (system);
    }

    public String getTaskRestrictionPeriodEndQualifierExtensionSystem() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + TASK_RESTRICTION_PERIOD_END_QUALIFIER_EXTENSION_SYSTEM;
        return (system);
    }

    //
    // Business Methods
    //

    public Task.TaskRestrictionComponent mapPetasosExecutionWindowToFHIRTaskRestriction(PetasosTask petasosTask) {
        if (petasosTask == null) {
            return (null);
        }
        if (petasosTask.getTaskExecutionDetail() == null) {
            return (null);
        }
        if (petasosTask.getTaskExecutionDetail().getExecutionWindow() == null) {
            return (null);
        }

        TaskWindow executionWindow = petasosTask.getTaskExecutionDetail().getExecutionWindow();

        Task.TaskRestrictionComponent taskRestriction = new Task.TaskRestrictionComponent();

        Period requiredExecutionPeriod = new Period();

        getLogger().trace(".mapPetasosExecutionWindowToFHIRTaskRestriction(): [Map Task Start Instant to Period.start()] Start");
        if (executionWindow.getLowerBound() != null) {
            if (executionWindow.getLowerBound().getWindowBoundaryCondition().equals(TaskWindowBoundaryConditionEnum.TASK_WINDOW_BOUNDARY_AFTER)) {
                if (executionWindow.getLowerBound().getWindowsBoundaryInstant() != null) {
                    requiredExecutionPeriod.setStart(Date.from(executionWindow.getLowerBound().getWindowsBoundaryInstant()));
                    Extension taskStartQualifierExtension = new Extension();
                    taskStartQualifierExtension.setUrl(getTaskRestrictionPeriodStartQualifierExtensionSystem());
                    StringType taskStartQualifierStringType = new StringType(executionWindow.getLowerBound().getWindowBoundaryCondition().getCode());
                    taskStartQualifierExtension.setValue(taskStartQualifierStringType);
                    requiredExecutionPeriod.addExtension(taskStartQualifierExtension);
                }
            }
        }
        getLogger().trace(".mapPetasosExecutionWindowToFHIRTaskRestriction(): [Map Task Start Instant to Period.start()] Start");

        getLogger().trace(".mapPetasosExecutionWindowToFHIRTaskRestriction(): [Map Task Finish Instant to Period.end()] Start");
        if (executionWindow.getUpperBound() != null) {
            if (executionWindow.getUpperBound().getWindowBoundaryCondition().equals(TaskWindowBoundaryConditionEnum.TASK_WINDOW_BOUNDARY_BEFORE)) {
                if (executionWindow.getUpperBound().getWindowsBoundaryInstant() != null) {
                    requiredExecutionPeriod.setEnd(Date.from(executionWindow.getUpperBound().getWindowsBoundaryInstant()));
                    Extension taskEndQualifierExtension = new Extension();
                    taskEndQualifierExtension.setUrl(getTaskRestrictionPeriodEndQualifierExtensionSystem());
                    StringType taskEndQualifierStringType = new StringType(executionWindow.getUpperBound().getWindowBoundaryCondition().getCode());
                    taskEndQualifierExtension.setValue(taskEndQualifierStringType);
                    requiredExecutionPeriod.addExtension(taskEndQualifierExtension);
                }
            }
        }
        getLogger().trace(".mapPetasosExecutionWindowToFHIRTaskRestriction(): [Map Task Finish Instant to Period.end()] Start");

        if (requiredExecutionPeriod.hasStart() || requiredExecutionPeriod.hasEnd()) {
            taskRestriction.setPeriod(requiredExecutionPeriod);
        }

        return (taskRestriction);
    }

    public TaskWindow extractTaskWindowFromFHIRTask(Task fhirTask) {
        if (fhirTask == null) {
            return (null);
        }
        if (!fhirTask.hasRestriction()) {
            return (null);
        }

        Task.TaskRestrictionComponent restrictionComponent = fhirTask.getRestriction();
        if (restrictionComponent.getPeriod() == null) {
            return (null);
        }
        Period taskRestrictionPeriod = restrictionComponent.getPeriod();

        TaskWindow taskWindow = new TaskWindow();

        if (taskRestrictionPeriod.hasStart()) {
            taskWindow.getLowerBound().setWindowsBoundaryInstant(taskRestrictionPeriod.getStart().toInstant());
            if (taskRestrictionPeriod.hasExtension(getTaskRestrictionPeriodStartQualifierExtensionSystem())) {
                Extension taskStartConditionExtension = taskRestrictionPeriod.getExtensionByUrl(getTaskRestrictionPeriodStartQualifierExtensionSystem());
                if (taskStartConditionExtension != null) {
                    StringType qualifierCodeStringType = (StringType) taskStartConditionExtension.getValue();
                    String qualifierCode = qualifierCodeStringType.getValue();
                    if (StringUtils.isNotEmpty(qualifierCode)) {
                        TaskWindowBoundaryConditionEnum conditionEnum = TaskWindowBoundaryConditionEnum.fromCode(qualifierCode);
                        if (conditionEnum != null) {
                            taskWindow.getLowerBound().setWindowBoundaryCondition(conditionEnum);
                        }
                    }
                }
            }
        }

        if (taskRestrictionPeriod.hasEnd()) {
            taskWindow.getUpperBound().setWindowsBoundaryInstant(taskRestrictionPeriod.getEnd().toInstant());
            if (taskRestrictionPeriod.hasExtension(getTaskRestrictionPeriodEndQualifierExtensionSystem())) {
                Extension taskEndConditionExtension = taskRestrictionPeriod.getExtensionByUrl(getTaskRestrictionPeriodEndQualifierExtensionSystem());
                if (taskEndConditionExtension != null) {
                    StringType qualifierCodeStringType = (StringType) taskEndConditionExtension.getValue();
                    String qualifierCode = qualifierCodeStringType.getValue();
                    if (StringUtils.isNotEmpty(qualifierCode)) {
                        TaskWindowBoundaryConditionEnum conditionEnum = TaskWindowBoundaryConditionEnum.fromCode(qualifierCode);
                        if (conditionEnum != null) {
                            taskWindow.getUpperBound().setWindowBoundaryCondition(conditionEnum);
                        }
                    }
                }
            }
        }

        return (taskWindow);
    }
}
