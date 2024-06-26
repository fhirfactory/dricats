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
package net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.datatypes;


import net.fhirfactory.dricats.core.model.task.PetasosActionableTask;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BatchTaskRetrievalSet implements Serializable {
    private Set<PetasosActionableTask> taskSet;
    private String setStartToken;
    private String nextSetsStartToken;

    //
    // Constructor(s)
    //

    public BatchTaskRetrievalSet() {
        this.taskSet = new HashSet<>();
        this.setStartToken = null;
        this.nextSetsStartToken = null;
    }

    //
    // Getters and Setters
    //

    public Set<PetasosActionableTask> getTaskSet() {
        return taskSet;
    }

    public void setTaskSet(Set<PetasosActionableTask> taskSet) {
        this.taskSet = taskSet;
    }

    public String getSetStartToken() {
        return setStartToken;
    }

    public void setSetStartToken(String setStartToken) {
        this.setStartToken = setStartToken;
    }

    public String getNextSetsStartToken() {
        return nextSetsStartToken;
    }

    public void setNextSetsStartToken(String nextSetsStartToken) {
        this.nextSetsStartToken = nextSetsStartToken;
    }

    //
    // toString()
    //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskRetrievalSet{");
        sb.append("taskSet=").append(taskSet);
        sb.append(", setStartToken='").append(setStartToken).append('\'');
        sb.append(", nextSetsStartToken='").append(nextSetsStartToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
