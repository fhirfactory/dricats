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
package net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;

import java.io.Serializable;
import java.time.Instant;

public class TaskFulfillmenStatusType implements Serializable {

    private boolean beingFulfilled;
    private Integer retryCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant entryInstant;
    private boolean clusterWideFulfillmentTask;
    private boolean systemWideFulfillmentTask;
    private boolean retryRequired;

    //
    // Constructor(s)
    //

    public TaskFulfillmenStatusType() {
        this.entryInstant = Instant.now();
        this.clusterWideFulfillmentTask = false;
        this.systemWideFulfillmentTask = false;
        this.retryRequired = false;
        this.retryCount = 0;
        this.beingFulfilled = false;
    }

    //
    // Getters and Setters (Bean Methods)
    //

    public boolean isClusterWideFulfillmentTask() {
        return clusterWideFulfillmentTask;
    }

    public void setClusterWideFulfillmentTask(boolean clusterWideFulfillmentTask) {
        this.clusterWideFulfillmentTask = clusterWideFulfillmentTask;
    }

    public boolean isSystemWideFulfillmentTask() {
        return systemWideFulfillmentTask;
    }

    public void setSystemWideFulfillmentTask(boolean systemWideFulfillmentTask) {
        this.systemWideFulfillmentTask = systemWideFulfillmentTask;
    }

    public boolean isRetryRequired() {
        return retryRequired;
    }

    public void setRetryRequired(boolean retryRequired) {
        this.retryRequired = retryRequired;
    }

    public boolean isBeingFulfilled() {
        return beingFulfilled;
    }

    public void setBeingFulfilled(boolean beingFulfilled) {
        this.beingFulfilled = beingFulfilled;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public boolean hasEntryInstant() {
        boolean hasValue = this.entryInstant != null;
        return (hasValue);
    }

    public Instant getEntryInstant() {
        return entryInstant;
    }

    public void setEntryInstant(Instant entryInstant) {
        this.entryInstant = entryInstant;
    }

    //
    // To String
    //


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskFulfillmenStatusType{");
        sb.append("beingFulfilled=").append(beingFulfilled);
        sb.append(", retryCount=").append(retryCount);
        sb.append(", entryInstant=").append(entryInstant);
        sb.append(", clusterWideFulfillmentTask=").append(clusterWideFulfillmentTask);
        sb.append(", systemWideFulfillmentTask=").append(systemWideFulfillmentTask);
        sb.append(", retryRequired=").append(retryRequired);
        sb.append('}');
        return sb.toString();
    }
}
