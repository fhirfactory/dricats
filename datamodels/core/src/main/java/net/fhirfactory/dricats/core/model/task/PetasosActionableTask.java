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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import net.fhirfactory.dricats.core.model.task.datatypes.completion.datatypes.TaskCompletionSummaryType;
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.datatypes.TaskFulfillmentType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Jacksonized
public class PetasosActionableTask extends PetasosTask {

    private TaskFulfillmentType taskFulfillment;
    private TaskCompletionSummaryType taskCompletionSummary;

    //
    // Constructor(s)
    //

    public PetasosActionableTask() {
        super();
        this.taskFulfillment = null;
        this.taskCompletionSummary = null;
        setTaskType(new TaskTypeType(TaskTypeTypeEnum.PETASOS_ACTIONABLE_TASK_TYPE));
    }

    //
    // has Methods
    //

    public boolean hasTaskFulfillment(){
        boolean hasValue = taskFulfillment != null;
        return(hasValue);
    }

    public boolean hasTaskCompletionSummary(){
        boolean hasValue = taskCompletionSummary != null;
        return(hasValue);
    }

    //
    // Update ActionableTask
    //

    @JsonIgnore
    public PetasosTask update(PetasosActionableTask update) {
        log.debug(".update(): Entry, update->{}", update);
        //
        // 1st, update the super-class attributes
        PetasosActionableTask updatedPetasosTask = (PetasosActionableTask) updatePetasosTask(update);
        log.trace(".update(): After PetasosTask update, updatedPetasosTask->{}", updatedPetasosTask);
        log.trace(".update(): After PetasosTask update, this->{}", this);
        //
        // 2nd, update the PetasosActionableTask specific attributes
        PetasosActionableTask updatedTask = updatePetasosActionableTask(update);
        log.debug(".update(): Exit, updatedTask->{}", updatedTask);
        return (updatedTask);
    }

    @JsonIgnore
    protected PetasosActionableTask updatePetasosActionableTask(PetasosActionableTask update) {
        if (update == null) {
            return (this);
        }
        if (update.getTaskCompletionSummary() != null) {
            if (!(this.getTaskCompletionSummary() != null)) {
                this.setTaskCompletionSummary(update.getTaskCompletionSummary());
            } else {
                this.getTaskCompletionSummary().setLastInChain(update.getTaskCompletionSummary().isLastInChain());
                this.getTaskCompletionSummary().setFinalised(update.getTaskCompletionSummary().isFinalised());
                this.getTaskCompletionSummary().setDownstreamTaskMap(update.getTaskCompletionSummary().getDownstreamTaskMap());
            }
        }
        log.trace(".updatePetasosActionableTask(): About to Update: update->getTaskFulfillment()->{}", update.getTaskFulfillment());
        log.trace(".updatePetasosActionableTask(): About to Update: this->getTaskFulfillment()->{}", this.getTaskFulfillment());
        if (update.getTaskFulfillment() != null) {
            if (!(this.getTaskFulfillment() != null)) {
                this.setTaskFulfillment(update.getTaskFulfillment());
            } else {
                if (update.getTaskFulfillment().hasFulfillerWorkUnitProcessor()) {
                    this.getTaskFulfillment().setFulfillerWorkUnitProcessor(update.getTaskFulfillment().getFulfillerWorkUnitProcessor());
                }
                if (update.getTaskFulfillment().hasFinalisationInstant()) {
                    this.getTaskFulfillment().setFinalisationInstant(update.getTaskFulfillment().getFinalisationInstant());
                }
                if (update.getTaskFulfillment().hasFinishInstant()) {
                    this.getTaskFulfillment().setFinishInstant(update.getTaskFulfillment().getFinishInstant());
                }
                if (update.getTaskFulfillment().hasLastCheckedInstant()) {
                    this.getTaskFulfillment().setLastCheckedInstant(update.getTaskFulfillment().getLastCheckedInstant());
                }
                if (update.getTaskFulfillment().hasReadyInstant()) {
                    this.getTaskFulfillment().setReadyInstant(update.getTaskFulfillment().getReadyInstant());
                }
                if (update.getTaskFulfillment().hasRegistrationInstant()) {
                    this.getTaskFulfillment().setRegistrationInstant(update.getTaskFulfillment().getRegistrationInstant());
                }
                if (update.getTaskFulfillment().hasStartInstant()) {
                    this.getTaskFulfillment().setStartInstant(update.getTaskFulfillment().getStartInstant());
                }
                if (update.getTaskFulfillment().hasStatus()) {
                    this.getTaskFulfillment().setStatus(update.getTaskFulfillment().getStatus());
                }
                if (update.getTaskFulfillment().hasTrackingID()) {
                    this.getTaskFulfillment().setTrackingID(update.getTaskFulfillment().getTrackingID());
                }
            }
        }
        log.debug(".updatePetasosActionableTask(): After Update: this->getTaskFulfillment()->{}", this.getTaskFulfillment());
        return (this);
    }
}
