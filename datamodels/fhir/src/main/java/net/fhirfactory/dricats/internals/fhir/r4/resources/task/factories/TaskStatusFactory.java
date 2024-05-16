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

import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskExecutionStatus;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskExecutionStatusEnum;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskStatusFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskStatusFactory.class);

    @Inject
    private TaskRestrictionFactory taskRestrictionFactory;

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected TaskRestrictionFactory getTaskRestrictionFactory() {
        return (taskRestrictionFactory);
    }

    //
    // Business Methods
    //

    public Task.TaskStatus mapPetasosTaskStatusToFHIRTaskStatus(TaskExecutionStatus taskExecutionStatus) {
        getLogger().debug(".mapPetasosTaskStatusToFHIRTaskStatus(): Entry");

        if (taskExecutionStatus == null) {
            return (null);
        }

        if (taskExecutionStatus.getCurrentExecutionStatus() != null) {
            Task.TaskStatus outcome = null;
            TaskExecutionStatusEnum currentExecutionStatus = taskExecutionStatus.getCurrentExecutionStatus();
            switch (currentExecutionStatus) {
                case PETASOS_TASK_EXECUTION_STATUS_FAILED:
                    outcome = Task.TaskStatus.FAILED;
                    break;
                case PETASOS_TASK_EXECUTION_STATUS_QUEUED:
                    outcome = Task.TaskStatus.RECEIVED;
                    break;
                case PETASOS_TASK_EXECUTION_STATUS_ASSIGNED:
                    outcome = Task.TaskStatus.ACCEPTED;
                    break;
                case PETASOS_TASK_EXECUTION_STATUS_EXECUTING:
                    outcome = Task.TaskStatus.INPROGRESS;
                    break;
                case PETASOS_TASK_EXECUTION_STATUS_CANCELLED:
                    outcome = Task.TaskStatus.CANCELLED;
                    break;
                case PETASOS_TASK_EXECUTION_STATUS_FINISHED:
                case PETASOS_TASK_EXECUTION_STATUS_FINALISED:
                    outcome = Task.TaskStatus.COMPLETED;
                    break;
                default:
                    outcome = Task.TaskStatus.REJECTED;
                    break;
            }
            getLogger().debug(".mapPetasosTaskStatusToFHIRTaskStatus(): Exit, outcome->{}", outcome);
            return (outcome);
        }
        getLogger().debug(".mapPetasosTaskStatusToFHIRTaskStatus(): Exit, outcome->{}", Task.TaskStatus.DRAFT);
        return (Task.TaskStatus.DRAFT);
    }

    public TaskExecutionStatus extractTaskExecutionStatusFromFHIRTask(Task fhirTask) {
        getLogger().debug(".mapFHIRTaskStatusToTaskExecutionStatus(): Entry");

        if (fhirTask == null) {
            return (null);
        }

        if (fhirTask.getStatus() == null) {
            return (null);
        }

        TaskExecutionStatus taskExecutionStatus = new TaskExecutionStatus();

        switch (fhirTask.getStatus()) {
            case RECEIVED:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_QUEUED);
                break;
            case ACCEPTED:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_ASSIGNED);
                break;
            case FAILED:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_FAILED);
                break;
            case CANCELLED:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_CANCELLED);
                break;
            case INPROGRESS:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_EXECUTING);
                break;
            case COMPLETED:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_FINISHED);
            default:
                taskExecutionStatus.setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_CANCELLED);
                break;
        }

        getLogger().debug(".mapFHIRTaskStatusToTaskExecutionStatus(): Exit, outcome->{}", Task.TaskStatus.DRAFT);
        return (taskExecutionStatus);
    }
}
