package net.fhirfactory.dricats.core.interfaces.tasks;

import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.datatypes.completion.datatypes.TaskCompletionSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskExecutionStatus;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.TaskOutcomeStatusType;
import net.fhirfactory.dricats.core.model.uow.UoWPayloadSet;

public interface PetasosTaskGridClientInterface {
    TaskIdType queueTask(PetasosActionableTask actionableTask);

    PetasosActionableTask registerExternallyTriggeredTask(String participantName, PetasosActionableTask actionableTask);

    PetasosActionableTask getNextPendingTask(String participantName);

    TaskExecutionStatus notifyTaskStart(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail);

    TaskExecutionStatus notifyTaskFinish(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String taskStatusReason);

    TaskExecutionStatus notifyTaskCancellation(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String taskStatusReason);

    TaskExecutionStatus notifyTaskFailure(String participantName, TaskIdType taskId, TaskFulfillmentType taskFulfillmentDetail, UoWPayloadSet egressPayload, TaskOutcomeStatusType taskOutcome, String taskStatusReason);

    TaskExecutionStatus notifyTaskFinalisation(String participantName, TaskIdType taskId, TaskCompletionSummaryType completionSummary);
}
