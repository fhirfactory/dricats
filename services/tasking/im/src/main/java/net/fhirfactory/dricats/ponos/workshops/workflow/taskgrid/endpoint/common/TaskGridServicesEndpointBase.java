/*
 * Copyright (c) 2022 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.endpoint.common;

import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.datatypes.completion.datatypes.TaskCompletionSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.performer.datatypes.TaskPerformerTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskExecutionStatus;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.TaskOutcomeStatusType;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.JGroupsIntegrationPointSummary;
import net.fhirfactory.dricats.core.model.uow.UoWPayloadSet;
import net.fhirfactory.dricats.internals.fhir.r4.resources.endpoint.valuesets.EndpointPayloadTypeEnum;
import net.fhirfactory.dricats.petasos.endpoints.technologies.jgroups.JGroupsIntegrationPointBase;
import net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.clientservices.TaskGridClientServicesManagerAlpha;
import net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.clientservices.TaskGridClientServicesManagerBeta;
import net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.clientservices.common.TaskGridClientServicesManagerBase;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.time.Instant;

public abstract class TaskGridServicesEndpointBase extends JGroupsIntegrationPointBase {

    private static final Long MAXIMUM_ACTIVITY_DURATION = 3000L;

    @Inject
    private TaskGridClientServicesManagerBeta taskServicesManagerBeta;

    @Inject
    private TaskGridClientServicesManagerAlpha taskServicesManagerAlpha;

    //
    // Instance Methods
    //

    protected TaskGridClientServicesManagerBase getTaskGridClientServicesManager() {
        if (taskServicesManagerAlpha.isBusy()) {
            if (taskServicesManagerBeta.isBusy()) {
                Long alphaActivityDuration = Instant.now().getEpochSecond() - taskServicesManagerAlpha.getBusyStartTime().getEpochSecond();
                if (alphaActivityDuration > MAXIMUM_ACTIVITY_DURATION) {
                    return (taskServicesManagerAlpha);
                } else {
                    return (taskServicesManagerBeta);
                }
            } else {
                return (taskServicesManagerBeta);
            }
        }
        return (taskServicesManagerAlpha);
    }

    //
    // Business Methods
    //

    public PetasosActionableTask registerExternallyTriggeredTask(String participantName, PetasosActionableTask actionableTask, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            TaskIdType actionableTaskId = null;
            if (actionableTask != null) {
                if (actionableTask.hasTaskId()) {
                    actionableTaskId = actionableTask.getTaskId();
                }
            }
            getLogger().info(".registerExternallyTriggeredTask(): Entry, participantName->{}, actionableTaskId->{}", participantName, actionableTaskId);
        }
        getLogger().debug(".registerExternallyTriggeredTask(): Entry, participantName->{}, actionableTask->{}, integrationPoint->{}", participantName, actionableTask, requesterEndpointSummary);
        PetasosActionableTask task = getTaskGridClientServicesManager().registerExternallyTriggeredTask(participantName, actionableTask);
        getLogger().debug(".registerExternallyTriggeredTask(): Exit, task->{}", task);
        getLogger().info(".registerExternallyTriggeredTask(): Exit");
        return (task);
    }

    public TaskIdType queueTask(PetasosActionableTask actionableTask, JGroupsIntegrationPointSummary integrationPoint) {
        if (getLogger().isInfoEnabled()) {
            TaskIdType actionableTaskId = null;
            String taskPerformer = null;
            if (actionableTask != null) {
                if (actionableTask.hasTaskId()) {
                    actionableTaskId = actionableTask.getTaskId();
                }
                if (actionableTask.hasTaskPerformerTypes()) {
                    StringBuilder performerTypes = new StringBuilder();
                    for (TaskPerformerTypeType currentPerformerType : actionableTask.getTaskPerformerTypes()) {
                        performerTypes.append(currentPerformerType.getRequiredParticipantName() + " ");
                    }
                    taskPerformer = performerTypes.toString();
                }
            }
            getLogger().info(".queueTask(): Entry, actionableTaskId->{}, participantName->{}", actionableTaskId, taskPerformer);
        }
        getLogger().debug(".queueTask(): Entry, actionableTask->{}, integrationPoint->{}", actionableTask, integrationPoint);
        TaskIdType taskId = getTaskGridClientServicesManager().queueTask(actionableTask);
        getLogger().debug(".queueTask(): Exit, taskId->{}", taskId);
        getLogger().info(".queueTask(): Exit");
        return (taskId);
    }

    public PetasosActionableTask getNextPendingTask(String participantName, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        getLogger().debug(".getNextPendingTask(): Entry, participantName->{}, requesterEndpointSummary->{}", participantName, requesterEndpointSummary);
        if (StringUtils.isEmpty(participantName)) {
            getLogger().debug(".getNextPendingTask(): Exit, participantName is empty");
            return (null);
        }
        PetasosActionableTask nextPendingTask = getTaskGridClientServicesManager().getNextPendingTask(participantName);
        getLogger().debug(".getNextPendingTask(): Exit, task->{}", nextPendingTask);
        return (nextPendingTask);
    }

    public TaskExecutionStatus notifyTaskStart(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(".notifyTaskStart(): Entry, participantName->{}, taskId->{}", participantName, taskId);
        }
        getLogger().debug(".notifyTaskStart(): Entry, participantName->{}, taskId->{}, taskFulfillmentDetail->{}", participantName, taskId, taskFulfillmentDetail);
        TaskExecutionStatus executionControl = getTaskGridClientServicesManager().updateTaskStatusToStart(participantName, taskId, taskFulfillmentDetail);
        getLogger().debug(".notifyTaskStart(): Exit, executionControl->{}", executionControl);
        getLogger().info(".notifyTaskStart(): Exit");
        return (executionControl);
    }

    public TaskExecutionStatus notifyTaskFinish(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String statusReason, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(".notifyTaskFinish(): Entry, participantName->{}, taskId->{}", participantName, taskId);
        }
        getLogger().debug(".notifyTaskFinish(): Entry, participantName->{}, taskId->{}, taskFulfillmentDetail->{}, egressPayload->{}, taskOutcome->{}", participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome);
        TaskExecutionStatus executionControl = getTaskGridClientServicesManager().updateTaskStatusToFinish(participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome, statusReason);
        getLogger().debug(".notifyTaskFinish(): Exit, executionControl->{}", executionControl);
        getLogger().info(".notifyTaskFinish(): Exit");
        return (executionControl);
    }

    public TaskExecutionStatus notifyTaskCancellation(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String statusReason, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(".notifyTaskCancellation(): Entry, participantName->{}, taskId->{}", participantName, taskId);
        }
        getLogger().debug(".notifyTaskCancellation(): Entry, participantName->{}, taskId->{}, taskFulfillmentDetail->{}, egressPayload->{}, taskOutcome->{}", participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome);
        TaskExecutionStatus executionControl = getTaskGridClientServicesManager().updateTaskStatusToCancelled(participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome, statusReason);
        getLogger().debug(".notifyTaskCancellation(): Exit, executionControl->{}", executionControl);
        getLogger().info(".notifyTaskCancellation(): Exit");
        return (executionControl);
    }

    public TaskExecutionStatus notifyTaskFailure(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String failureDescription, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(".notifyTaskFailure(): Entry, participantName->{}, taskId->{}", participantName, taskId);
        }
        getLogger().debug(".notifyTaskFailure(): Entry, participantName->{}, taskId->{}, taskFulfillmentDetail->{}, egressPayload->{}, taskOutcome->{}, failureDescription->{}", participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome, failureDescription);
        TaskExecutionStatus executionControl = getTaskGridClientServicesManager().updateTaskStatusToFailed(participantName, taskId, taskFulfillmentDetail, egressPayload, taskOutcome, failureDescription);
        getLogger().debug(".notifyTaskFailure(): Exit, executionControl->{}", executionControl);
        getLogger().info(".notifyTaskFailure(): Exit");
        return (executionControl);
    }

    public TaskExecutionStatus notifyTaskFinalisation(String participantName, TaskIdType taskId, TaskCompletionSummaryType completionSummary, JGroupsIntegrationPointSummary requesterEndpointSummary) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(".notifyTaskFinalisation(): Entry, participantName->{}, taskId->{}", participantName, taskId);
        }
        getLogger().debug(".notifyTaskFinalisation(): Entry, participantName->{}, taskId->{}, completionSummary->{}", participantName, taskId, completionSummary);
        TaskExecutionStatus executionControl = getTaskGridClientServicesManager().updateTaskStatusToFinalised(participantName, taskId, completionSummary);
        getLogger().debug(".notifyTaskFinalisation(): Exit, executionControl->{}", executionControl);
        getLogger().info(".notifyTaskFinalisation(): Exit");
        return (executionControl);
    }

    //
    // JGroups Endpoint Housekeeping
    //

    @Override
    protected String specifySubsystemParticipantName() {
        return (getProcessingPlant().getSubsystemParticipantName());
    }

    @Override
    protected String specifyJGroupsClusterName() {
        return (getComponentNameUtilities().getPetasosTaskServicesGroupName());
    }

    @Override
    protected String specifyJGroupsStackFileName() {
        return (getProcessingPlant().getMeAsASoftwareComponent().getPetasosTaskingStackConfigFile());
    }

    @Override
    protected PetasosEndpointTopologyTypeEnum specifyIPCType() {
        return (PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT);
    }

    @Override
    protected EndpointPayloadTypeEnum specifyPetasosEndpointPayloadType() {
        return (EndpointPayloadTypeEnum.ENDPOINT_PAYLOAD_INTERNAL_TASKS);
    }

    @Override
    protected void addIntegrationPointToJGroupsIntegrationPointSet() {
        getJgroupsIPSet().setPetasosTaskServicesEndpoint(getJGroupsIntegrationPoint());
    }

    @Override
    protected void doIntegrationPointBusinessFunctionCheck(JGroupsIntegrationPointSummary integrationPointSummary, boolean isRemoved, boolean isAdded) {

    }

    @Override
    protected void executePostConstructActivities() {

    }

    //
    // Task Grid Client Interface Stubs
    //


}
