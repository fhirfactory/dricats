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
package net.fhirfactory.dricats.core.model.task.datatypes.traceability.factories;

import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityElementType;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityType;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.PetasosFulfillmentTask;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskTraceabilityTypeFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskTraceabilityTypeFactory.class);

    @Inject
    private TaskTraceabilityElementTypeFactory traceabilityElementTypeFactory;

    //
    // Constructor(s)
    //

    /* none required */

    //
    // Business Methods
    //

    public TaskTraceabilityType newTaskTraceabilityFromTask(PetasosActionableTask task) {
        getLogger().debug(".newTaskTraceabilityFromTask(): Entry, task->{}", task);
        if (task == null) {
            getLogger().debug(".newTaskTraceabilityFromTask(): Exit, task is null, returing null");
            return (null);
        }
        TaskTraceabilityType traceability = null;
        if (task.getTaskTraceability() != null) {
            traceability = SerializationUtils.clone(task.getTaskTraceability());
        } else {
            traceability = new TaskTraceabilityType();
        }
        if (task.getTaskFulfillment() != null) {
            TaskIdType taskId = task.getTaskId();
            getLogger().trace(".newTaskTraceabilityFromTask(): taskId->{}", taskId);
            TaskFulfillmentType taskFulfillment = task.getTaskFulfillment();
            getLogger().trace(".newTaskTraceabilityFromTask(): taskFulfillment->{}", taskFulfillment);
            TaskTraceabilityElementType traceabilityElementType = traceabilityElementTypeFactory.newTaskTraceabilityElementFromTask(taskId, taskFulfillment);
            if (traceabilityElementType != null) {
                traceability.addToTaskJourney(traceabilityElementType);
            }
        }
        getLogger().debug(".newTaskTraceabilityFromTask(): Exit, traceability->{}", traceability);
        return (traceability);
    }

    public TaskTraceabilityType newTaskTraceabilityFromTask(PetasosActionableTask task, TaskTraceabilityElementType lastTraceabilityElement) {
        getLogger().debug(".newTaskTraceabilityFromTask(): Entry, task->{}", task);
        if (task == null) {
            getLogger().debug(".newTaskTraceabilityFromTask(): Exit, task is null, returing null");
            return (null);
        }
        TaskTraceabilityType traceability = null;
        if (task.getTaskTraceability() != null) {
            traceability = SerializationUtils.clone(task.getTaskTraceability());
            traceability.setResourceId(null);
        } else {
            traceability = new TaskTraceabilityType();
        }
        if (lastTraceabilityElement != null) {
            traceability.addToTaskJourney(lastTraceabilityElement);
        }
        getLogger().debug(".newTaskTraceabilityFromTask(): Exit, traceability->{}", traceability);
        return (traceability);
    }

    public TaskTraceabilityType newTaskTraceabilityFromTask(PetasosFulfillmentTask task) {
        getLogger().debug(".newTaskTraceabilityFromTask(): Entry, task->{}", task);
        if (task == null) {
            return (null);
        }
        TaskTraceabilityType traceability = null;
        if (task.getTaskTraceability() != null) {
            traceability = SerializationUtils.clone(task.getTaskTraceability());
        } else {
            traceability = new TaskTraceabilityType();
        }
        if (task.getTaskTraceability() != null) {
            TaskTraceabilityElementType traceabilityElementType = traceabilityElementTypeFactory.newTaskTraceabilityElementFromTask(task.getActionableTaskId(), task.getTaskFulfillment());
            if (traceabilityElementType != null) {
                traceability.addToTaskJourney(traceabilityElementType);
            }
        }
        getLogger().debug(".newTaskTraceabilityFromTask(): Exit, traceability->{}", traceability);
        return (traceability);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }
}
