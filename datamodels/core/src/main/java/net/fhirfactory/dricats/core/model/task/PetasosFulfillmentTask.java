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
package net.fhirfactory.dricats.core.model.task;

import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;
import net.fhirfactory.dricats.core.model.wup.PetasosTaskJobCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class PetasosFulfillmentTask extends PetasosTask {
    //
    // Housekeeping
    //
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(PetasosFulfillmentTask.class);

    //
    // Member Variables
    //

    private TaskFulfillmentType taskFulfillment;

    private PetasosTaskJobCard taskJobCard;

    private TaskIdType actionableTaskId;

    private boolean aRetry;

    //
    // Constructor(s)
    //

    public PetasosFulfillmentTask() {
        super();
        this.actionableTaskId = null;
        this.taskFulfillment = new TaskFulfillmentType();
        this.taskJobCard = null;
        this.aRetry = false;
        setTaskType(new TaskTypeType(TaskTypeTypeEnum.PETASOS_FULFILLMENT_TASK_TYPE));
    }

    //
    // has Values
    //

    public boolean hasTaskFulfillment() {
        return taskFulfillment != null;
    }

    public boolean hasTaskJobCard() {
        return taskJobCard != null;
    }

    public boolean hasActionableTaskId() {
        return actionableTaskId != null;
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger(){
        return(LOG);
    }

    public TaskFulfillmentType getTaskFulfillment() {
        return taskFulfillment;
    }

    public void setTaskFulfillment(TaskFulfillmentType taskFulfillment) {
        this.taskFulfillment = taskFulfillment;
    }

    public PetasosTaskJobCard getTaskJobCard() {
        return taskJobCard;
    }

    public void setTaskJobCard(PetasosTaskJobCard taskJobCard) {
        this.taskJobCard = taskJobCard;
    }

    public TaskIdType getActionableTaskId() {
        return actionableTaskId;
    }

    public void setActionableTaskId(TaskIdType actionableTaskId) {
        this.actionableTaskId = actionableTaskId;
    }

    public boolean isaRetry() {
        return aRetry;
    }

    public void setaRetry(boolean aRetry) {
        this.aRetry = aRetry;
    }

    //
    // Utility Methods
    //


    @Override
    public String toString() {
        return "PetasosFulfillmentTask{" +
                "taskFulfillment=" + getTaskFulfillment() +
                ", taskJobCard=" + getTaskJobCard() +
                ", actionableTaskId=" + getActionableTaskId() +
                ", aRetry=" + isaRetry() +
                ", creationInstant=" + getCreationInstant() +
                ", updateInstant=" + getUpdateInstant() +
                ", sourceResourceId=" + getSourceResourceId() +
                ", taskContext=" + getTaskContext() +
                ", taskId=" + getTaskId() +
                ", taskType=" + getTaskType() +
                ", taskWorkItem=" + getTaskWorkItem() +
                ", taskTraceability=" + getTaskTraceability() +
                ", taskOutcomeStatus=" + getTaskOutcomeStatus() +
                ", taskPerformerTypes=" + getTaskPerformerTypes() +
                ", taskReason=" + getTaskReason() +
                ", taskNodeAffinity=" + getTaskNodeAffinity() +
                ", aggregateTaskMembership=" + getAggregateTaskMembership() +
                ", taskExecutionDetail=" + getTaskExecutionDetail() +
                ", registered=" + isRegistered() +
                '}';
    }
}
