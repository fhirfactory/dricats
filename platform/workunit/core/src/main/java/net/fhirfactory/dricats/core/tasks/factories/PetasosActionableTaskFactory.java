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
package net.fhirfactory.dricats.core.tasks.factories;

import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskContextType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.valuesets.FulfillmentExecutionStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskSequenceNumber;
import net.fhirfactory.dricats.core.tasks.caches.TaskSequenceNumberGenerator;
import net.fhirfactory.dricats.core.tasks.caches.shared.ParticipantSharedActionableTaskCache;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.factories.TaskIdTypeFactory;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.datatypes.TaskReasonType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.valuesets.TaskReasonTypeEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityElementType;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskTraceabilityType;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.factories.TaskTraceabilityElementTypeFactory;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.factories.TaskTraceabilityTypeFactory;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
public class PetasosActionableTaskFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PetasosActionableTaskFactory.class);

    @Inject
    private TaskIdTypeFactory taskIdFactory;

    @Inject
    private ParticipantSharedActionableTaskCache actionableTaskIM;

    @Inject
    private TaskTraceabilityTypeFactory traceabilityTypeFactory;

    @Inject
    private TaskTraceabilityElementTypeFactory traceabilityElementTypeFactory;

    @Inject
    private ProcessingPlantInterface processingPlant;

    @Inject
    private TaskSequenceNumberGenerator sequenceNumberGenerator;

    //
    // Constructor(s)
    //


    //
    // Post Construct Methods
    //


    //
    // Business Methods
    //

    public PetasosActionableTask newMessageBasedActionableTask(PetasosActionableTask upstreamTask, TaskWorkItemType payload) {
        getLogger().debug(".newMessageBasedActionableTask(): Entry, upstreamTask->{}, fulfillmentTaskSummary->{}, payload->{}", upstreamTask, payload);

        //
        // Create an empty task
        getLogger().trace(".newMessageBasedActionableTask(): [Create Base Task] Start");
        PetasosActionableTask newTask = newMessageBasedActionableTask(payload);
        getLogger().trace(".newMessageBasedActionableTask(): [Create Base Task] Finish");

        getLogger().trace(".newMessageBasedActionableTask(): [Add TaskContext Detail] Start");
        if (upstreamTask.hasTaskContext()) {
            if (!newTask.hasTaskContext()) {
                newTask.setTaskContext(new TaskContextType());
            }
            if (upstreamTask.getTaskContext().hasTaskTriggerSummary()) {
                newTask.getTaskContext().setTaskTriggerSummary(upstreamTask.getTaskContext().getTaskTriggerSummary());
            }
        }
        getLogger().trace(".newMessageBasedActionableTask(): [Add TaskContext Detail] Finish");

        getLogger().trace(".newMessageBasedActionableTask(): [Add Traceability Information] Start");
        TaskTraceabilityType taskTraceabilityType = traceabilityTypeFactory.newTaskTraceabilityFromTask(upstreamTask);
        newTask.setTaskTraceability(taskTraceabilityType);
        getLogger().trace(".newMessageBasedActionableTask(): [Add Traceability Information] Finish");
        //
        // return the object
        getLogger().debug(".newMessageBasedActionableTask(): Exit, newTask->{}", newTask);
        return (newTask);
    }

    public PetasosActionableTask newMessageBasedActionableTask(PetasosActionableTask upstreamTask, TaskTraceabilityElementType fulfillmentTaskSummary, TaskWorkItemType payload) {
        getLogger().debug(".newMessageBasedActionableTask(): Entry, upstreamTask->{}, fulfillmentTaskSummary->{}, payload->{}", upstreamTask, fulfillmentTaskSummary, payload);

        //
        // Create an empty task
        PetasosActionableTask newTask = newMessageBasedActionableTask(payload);
        //
        // create task traceability information
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask Traceability Information] Start");
        TaskTraceabilityType taskTraceabilityType = traceabilityTypeFactory.newTaskTraceabilityFromTask(upstreamTask, fulfillmentTaskSummary);
        newTask.setTaskTraceability(taskTraceabilityType);
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask Traceability Information] Finish");
        //
        // return the object
        getLogger().debug(".newMessageBasedActionableTask(): Exit, petasosActionableTask->{}", newTask);
        return (newTask);
    }

    public PetasosActionableTask newMessageBasedActionableTask(PetasosActionableTask upstreamTask, TaskFulfillmentType fulfillment, TaskWorkItemType payload) {
        getLogger().debug(".newMessageBasedActionableTask(): Entry, upstreamTask->{}, fulfillment->{}, payload->{}", upstreamTask, fulfillment, payload);

        TaskTraceabilityElementType traceabilityElementType = traceabilityElementTypeFactory.newTaskTraceabilityElementFromTask(upstreamTask.getTaskId(), fulfillment);
        PetasosActionableTask petasosActionableTask = newMessageBasedActionableTask(upstreamTask, traceabilityElementType, payload);
        getLogger().debug(".newMessageBasedActionableTask(): Exit, petasosActionableTask->{}", petasosActionableTask);
        return (petasosActionableTask);
    }

    public PetasosActionableTask newMessageBasedActionableTask(TaskWorkItemType payload) {
        getLogger().debug(".newMessageBasedActionableTask(): Entry, payload->{}", payload);
        //
        // Create an empty task
        PetasosActionableTask newTask = new PetasosActionableTask();
        //
        // create a new id
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask ID] Start");
        TaskIdType taskId = taskIdFactory.newTaskId(TaskReasonTypeEnum.TASK_REASON_MESSAGE_PROCESSING, payload.getPayloadTopicID().getContentDescriptor());
        TaskSequenceNumber taskSequenceNumber = sequenceNumberGenerator.generateNewSequenceNumber();
        taskId.setTaskSequenceNumber(taskSequenceNumber);
        newTask.setTaskId(taskId);
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask ID] Finish");
        //
        // create task traceability information
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask Traceability Information] Start");
        TaskTraceabilityType taskTraceabilityType = new TaskTraceabilityType();
        newTask.setTaskTraceability(taskTraceabilityType);
        getLogger().trace(".newMessageBasedActionableTask(): [Create ActionableTask Traceability Information] Finish");
        //
        // add the task reason
        getLogger().trace(".newMessageBasedActionableTask(): [Assign ActionableTask Reason] Start");
        TaskReasonType taskReason = new TaskReasonType(TaskReasonTypeEnum.TASK_REASON_MESSAGE_PROCESSING);
        newTask.setTaskReason(taskReason);
        getLogger().trace(".newMessageBasedActionableTask(): [Assign ActionableTask Reason] Finish");
        //
        // add the task node affinity
        getLogger().trace(".newMessageBasedActionableTask(): [Assign Task Node Affinity] Start");
        newTask.setTaskNodeAffinity(processingPlant.getMeAsASoftwareComponent().getComponentID());
        getLogger().trace(".newMessageBasedActionableTask(): [Assign Task Node Affinity] Finish");
        //
        // add the task work item
        getLogger().trace(".newMessageBasedActionableTask(): [Assign Task Work Item] Start");
        newTask.setTaskWorkItem(payload);
        getLogger().trace(".newMessageBasedActionableTask(): [Assign Task Work Item] Finish");
        //
        // add an empty task fullfillment
        getLogger().trace(".newMessageBasedActionableTask(): [Add Task Fulfillment] Start");
        TaskFulfillmentType taskFulfillment = new TaskFulfillmentType();
        taskFulfillment.setStatus(FulfillmentExecutionStatusEnum.FULFILLMENT_EXECUTION_STATUS_UNREGISTERED);
        taskFulfillment.setLastCheckedInstant(Instant.now());
        newTask.setTaskFulfillment(taskFulfillment);
        //
        // return the object
        getLogger().debug(".newMessageBasedActionableTask(): Exit, petasosActionableTask->{}", newTask);
        return (newTask);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }
}
