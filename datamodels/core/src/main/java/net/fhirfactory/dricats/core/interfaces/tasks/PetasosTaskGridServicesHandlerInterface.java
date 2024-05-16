/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.core.interfaces.tasks;

import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.datatypes.completion.datatypes.TaskCompletionSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskExecutionStatus;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.TaskOutcomeStatusType;
import net.fhirfactory.dricats.core.model.uow.UoWPayloadSet;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.JGroupsIntegrationPointSummary;

public interface PetasosTaskGridServicesHandlerInterface {
    public TaskIdType queueTask(PetasosActionableTask actionableTask, JGroupsIntegrationPointSummary requesterEndpointIdentifier);

    public PetasosActionableTask getNextPendingTask(String participantName, JGroupsIntegrationPointSummary requestorEndpointSummary);

    public TaskExecutionStatus notifyTaskStart(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, JGroupsIntegrationPointSummary requesterEndpointSummary);

    public TaskExecutionStatus notifyTaskFinish(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String statusReason, JGroupsIntegrationPointSummary requesterEndpointSummary);

    public TaskExecutionStatus notifyTaskCancellation(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String statusReason, JGroupsIntegrationPointSummary requesterEndpointSummary);

    public TaskExecutionStatus notifyTaskFailure(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String statusReason, JGroupsIntegrationPointSummary requesterEndpointSummary);

    public TaskExecutionStatus notifyTaskFinalisation(String participantName, TaskIdType taskId, TaskCompletionSummaryType completionSummary, JGroupsIntegrationPointSummary requesterEndpointSummary);
}
