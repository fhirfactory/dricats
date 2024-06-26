/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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

package net.fhirfactory.dricats.core.participants.wup.helper;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeFunctionFDNToken;
import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;
import net.fhirfactory.dricats.core.model.task.PetasosFulfillmentTask;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskContextType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.TaskTriggerSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.context.valuesets.TaskTriggerSummaryTypeTypeEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.FulfillmentTrackingIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.valuesets.FulfillmentExecutionStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.performer.datatypes.TaskPerformerTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.valuesets.TaskExecutionCommandEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemType;
import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskExecutionStatusEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkUnitProcessorSoftwareComponent;
import net.fhirfactory.dricats.core.model.task.datatypes.context.datatypes.MLLPMessageActivityParcel;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosActionableTaskSharedInstance;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosActionableTaskSharedInstanceAccessorFactory;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosFulfillmentTaskSharedInstance;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosFulfillmentTaskSharedInstanceAccessorFactory;
import net.fhirfactory.dricats.core.tasks.factories.PetasosActionableTaskFactory;
import net.fhirfactory.dricats.core.tasks.factories.PetasosFulfillmentTaskFactory;
import net.fhirfactory.dricats.core.tasks.management.local.LocalPetasosFulfilmentTaskActivityController;
import net.fhirfactory.dricats.core.tasks.management.local.status.TaskDataGridProxy;
import net.fhirfactory.dricats.core.oam.metrics.collectors.WorkUnitProcessorMetricsAgent;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;

/**
 * This class (bean) is to be injected into the flow of an Ingres Only WUP Implementation
 * (i.e. Ingres Messaging, RESTful.POST, RESTful.PUT, RESTful.DELETE). It provides the
 * Petasos Initialisation Sequence of the Transaction/Messaging flow - including logging
 * the initial Audit-Trail entry.
 * <p>
 * The method registerActivityStart must be invoked PRIOR to responding to the source (external)
 * system with a +ve/-ve response.
 */

@ApplicationScoped
public class IngresActivityBeginRegistration {
    private static final Logger LOG = LoggerFactory.getLogger(IngresActivityBeginRegistration.class);

    @Inject
    TaskDataGridProxy taskDataGridProxy;

    @Inject
    LocalPetasosFulfilmentTaskActivityController fulfilmentTaskActivityController;

    @Inject
    private PetasosFulfillmentTaskFactory fulfillmentTaskFactory;

    @Inject
    private PetasosActionableTaskFactory actionableTaskFactory;

    @Inject
    private PetasosActionableTaskSharedInstanceAccessorFactory actionableTaskSharedInstanceFactory;

    @Inject
    private PetasosFulfillmentTaskSharedInstanceAccessorFactory fulfillmentTaskSharedInstanceFactory;

    //
    // Business Methods
    //

    public UoW registerActivityStart(UoW theUoW, Exchange camelExchange) {
        getLogger().debug(".registerActivityStart(): Entry, payload --> {}", theUoW);

        getLogger().trace(".registerActivityStart(): Retrieve NodeElement");
        WorkUnitProcessorSoftwareComponent wup = camelExchange.getProperty(PetasosPropertyConstants.WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME, WorkUnitProcessorSoftwareComponent.class);

        getLogger().trace(".registerActivityStart(): Node Element retrieved --> {}", wup);
        TopologyNodeFunctionFDNToken wupFunctionToken = wup.getNodeFunctionFDN().getFunctionToken();
        getLogger().trace(".registerActivityStart(): wupFunctionToken (NodeElementFunctionToken) for this activity --> {}", wupFunctionToken);

        //
        // add to WUP Metrics
        WorkUnitProcessorMetricsAgent metricsAgent = camelExchange.getProperty(PetasosPropertyConstants.WUP_METRICS_AGENT_EXCHANGE_PROPERTY, WorkUnitProcessorMetricsAgent.class);
        if (metricsAgent == null) {
            getLogger().warn(".registerActivityStart(): Could not get metricsAgent");
        }
        metricsAgent.touchLastActivityStartInstant();
        metricsAgent.touchLastActivityInstant();

        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] Start");
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Specify TaskWorkItem] Start");
        TaskWorkItemType workItem = new TaskWorkItemType(theUoW.getIngresContent());
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Specify TaskWorkItem] Finish");
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create Base PetasosActionableTask] Start");
        PetasosActionableTask petasosActionableTask = getActionableTaskFactory().newMessageBasedActionableTask(workItem);
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create Base PetasosActionableTask] Finish");
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create & Assign TaskContext] Start");
        TaskContextType taskContext = new TaskContextType();
        TaskTriggerSummaryType taskTriggerSummary = new TaskTriggerSummaryType();
        taskTriggerSummary.setTriggerTaskId(petasosActionableTask.getTaskId());
        taskTriggerSummary.setTriggerLocation(wup.getComponentFDN().getToken().getTokenValue());
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create & Assign TaskContext] [Retrieve HL7 Message Context if Available] Start");
        MLLPMessageActivityParcel messageHeader = camelExchange.getProperty(PetasosPropertyConstants.DRICATS_HL7_MESSAGE_DETAIL_PARAMETER_NAME, MLLPMessageActivityParcel.class);
        if (messageHeader != null) {
            taskTriggerSummary.setTriggerSummaryType(TaskTriggerSummaryTypeTypeEnum.TASK_TRIGGER_HL7_V2X_MESSAGE);
            taskTriggerSummary.setReceivedTriggerEvent(messageHeader);
        }
        taskContext.setTaskTriggerSummary(taskTriggerSummary);
        petasosActionableTask.setTaskContext(taskContext);
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create & Assign TaskContext] Finish");
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create & Assign TaskPerformer] Start");
        TaskPerformerTypeType taskPerformer = new TaskPerformerTypeType();
        taskPerformer.setRequiredPerformerType(wup.getNodeFunctionFDN());
        taskPerformer.setKnownFulfillerInstance(wup.getComponentID());
        taskPerformer.setRequiredParticipantName(wup.getParticipantName());
        taskPerformer.setRequiredPerformerTypeDescription(wup.getParticipantDisplayName());
        if (petasosActionableTask.getTaskPerformerTypes() == null) {
            petasosActionableTask.setTaskPerformerTypes(new ArrayList<>());
        }
        petasosActionableTask.getTaskPerformerTypes().add(taskPerformer);
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] [Create & Assign TaskPerformer] Finish");
        getLogger().trace(".registerActivityStart(): [Create PetasosActionableTask] Finish");

        getLogger().trace(".registerActivityStart(): Register PetasosActionableTask for the incoming message (processing activity): Start");
        PetasosActionableTaskSharedInstance actionableTaskSharedInstance = getTaskDataGridProxy().registerExternallyTriggeredTask(wup.getParticipantName(), petasosActionableTask);
        // Add some more metrics
        metricsAgent.incrementRegisteredTasks();
        getLogger().trace(".registerActivityStart(): Register PetasosActionableTask for the incoming message (processing activity): Finish");

        getLogger().trace(".registerActivityStart(): Create a PetasosFulfillmentTask for the (local) processing implementation activity: Start");
        PetasosFulfillmentTask fulfillmentTask = getFulfillmentTaskFactory().newFulfillmentTask(petasosActionableTask, wup);
        fulfillmentTask.getTaskExecutionDetail().setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_ASSIGNED);
        fulfillmentTask.setUpdateInstant(Instant.now());
        fulfillmentTask.getTaskFulfillment().setStartInstant(Instant.now());
        fulfillmentTask.getTaskFulfillment().setStatus(FulfillmentExecutionStatusEnum.FULFILLMENT_EXECUTION_STATUS_REGISTERED);
        getLogger().trace(".registerActivityStart(): Create a PetasosFulfillmentTask for the (local) processing implementation activity: Finish");

        getLogger().trace(".registerActivityStart(): Register PetasosFulfillmentTask for the (local) processing implementation activity: Start");
        PetasosFulfillmentTaskSharedInstance petasosFulfillmentTaskSharedInstance = getFulfilmentTaskActivityController().registerFulfillmentTask(fulfillmentTask, true);// by default, use synchronous audit writing
        getLogger().trace(".registerActivityStart(): Register PetasosFulfillmentTask for the (local) processing implementation activity: Finish");

        getLogger().trace(".registerActivityStart(): Set processing to the grantedExecutionStatus: Start");
        if (actionableTaskSharedInstance.getTaskExecutionDetail().getExecutionCommand().equals(TaskExecutionCommandEnum.TASK_COMMAND_EXECUTE)) {
            petasosFulfillmentTaskSharedInstance.getTaskExecutionDetail().setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_EXECUTING);
            petasosFulfillmentTaskSharedInstance.getTaskFulfillment().setStartInstant(Instant.now());
            petasosFulfillmentTaskSharedInstance.getTaskFulfillment().setStatus(FulfillmentExecutionStatusEnum.FULFILLMENT_EXECUTION_STATUS_ACTIVE);
            petasosFulfillmentTaskSharedInstance.update();

            getTaskDataGridProxy().notifyTaskStart(petasosFulfillmentTaskSharedInstance.getActionableTaskId(), petasosFulfillmentTaskSharedInstance.getInstance());

            actionableTaskSharedInstance.getTaskFulfillment().setFulfillerWorkUnitProcessor(petasosFulfillmentTaskSharedInstance.getTaskFulfillment().getFulfillerWorkUnitProcessor());
            actionableTaskSharedInstance.getTaskFulfillment().setTrackingID(new FulfillmentTrackingIdType(petasosFulfillmentTaskSharedInstance.getTaskId()));
            getLogger().trace(".registerActivityStart(): Before Update: actionableTaskSharedInstance.getTaskFulfillment()->{}", actionableTaskSharedInstance.getTaskFulfillment());
            actionableTaskSharedInstance.update();
            getLogger().trace(".registerActivityStart(): After Update: actionableTaskSharedInstance.getTaskFulfillment()->{}", actionableTaskSharedInstance.getTaskFulfillment());

            // Add some more metrics
            metricsAgent.incrementStartedTasks();
        } else {
            petasosFulfillmentTaskSharedInstance.getTaskExecutionDetail().setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_CANCELLED);
            petasosFulfillmentTaskSharedInstance.getTaskFulfillment().setStartInstant(Instant.now());
            petasosFulfillmentTaskSharedInstance.getTaskFulfillment().setFinishInstant(Instant.now());
            petasosFulfillmentTaskSharedInstance.getTaskFulfillment().setStatus(FulfillmentExecutionStatusEnum.FULFILLMENT_EXECUTION_STATUS_CANCELLED);
            petasosFulfillmentTaskSharedInstance.update();

            actionableTaskSharedInstance.getTaskFulfillment().setFulfillerWorkUnitProcessor(petasosFulfillmentTaskSharedInstance.getTaskFulfillment().getFulfillerWorkUnitProcessor());
            actionableTaskSharedInstance.getTaskFulfillment().setTrackingID(new FulfillmentTrackingIdType(fulfillmentTask.getTaskId()));
            actionableTaskSharedInstance.getTaskExecutionDetail().setCurrentExecutionStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_CANCELLED);
            actionableTaskSharedInstance.getTaskExecutionDetail().setUpdateInstant(Instant.now());
            actionableTaskSharedInstance.getTaskExecutionDetail().setTaskExecutionStatusReason("Told to Cancel");
            actionableTaskSharedInstance.update();
            getTaskDataGridProxy().notifyTaskCancellation(actionableTaskSharedInstance.getTaskId(), petasosFulfillmentTaskSharedInstance.getInstance());
            theUoW.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_CANCELLED);
        }
        getLogger().trace(".registerActivityStart(): Update status to reflect local processing is proceeding: Finish");
        //
        // Now we have to Inject some details into the Exchange so that the WUPEgressConduit can extract them as per standard practice
        getLogger().trace(".registerActivityStart(): Injecting Job Card and Status Element into Exchange for extraction by the WUP Egress Conduit");
        camelExchange.setProperty(PetasosPropertyConstants.WUP_PETASOS_FULFILLMENT_TASK_EXCHANGE_PROPERTY, petasosFulfillmentTaskSharedInstance);
        //
        // And now we are done!
        getLogger().debug(".registerActivityStart(): exit, my work is done!");
        return (theUoW);
    }

    //
    // Getters (and Setters)
    //

    protected TaskDataGridProxy getTaskDataGridProxy() {
        return taskDataGridProxy;
    }

    protected LocalPetasosFulfilmentTaskActivityController getFulfilmentTaskActivityController() {
        return fulfilmentTaskActivityController;
    }

    protected PetasosFulfillmentTaskFactory getFulfillmentTaskFactory() {
        return fulfillmentTaskFactory;
    }

    protected PetasosActionableTaskFactory getActionableTaskFactory() {
        return actionableTaskFactory;
    }

    protected Logger getLogger() {
        return (LOG);
    }
}
