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
package net.fhirfactory.dricats.core.model.task.queue;

import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskSequenceNumber;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.datatypes.TaskWindow;
import net.fhirfactory.dricats.core.model.task.datatypes.schedule.valuesets.TaskWindowBoundaryConditionEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.traceability.datatypes.TaskStorageType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class ParticipantTaskQueueEntry extends TaskStorageType implements Comparable<ParticipantTaskQueueEntry> {
    private TaskIdType taskId;
    private TaskWindow taskWindow;
    private ParticipantTaskQueueEntry nextNode;
    private ParticipantTaskQueueEntry previousNode;

    //
    // Constructor(s)
    //

    public ParticipantTaskQueueEntry() {
        taskId = null;
        taskWindow = null;
    }

    //
    // Getters and Setters
    //

    public TaskSequenceNumber getSequenceNumber() {
        return getTaskId().getTaskSequenceNumber();
    }

    public void setSequenceNumber(TaskSequenceNumber sequenceNumber) {
        if (getTaskId() == null) {
            this.setTaskId(new TaskIdType());
        }
        this.getTaskId().setTaskSequenceNumber(sequenceNumber);
    }

    public TaskIdType getTaskId() {
        return taskId;
    }

    public void setTaskId(TaskIdType taskId) {
        this.taskId = taskId;
    }

    public ParticipantTaskQueueEntry getNextNode() {
        return nextNode;
    }

    public void setNextNode(ParticipantTaskQueueEntry nextNode) {
        this.nextNode = nextNode;
    }

    public ParticipantTaskQueueEntry getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(ParticipantTaskQueueEntry previousNode) {
        this.previousNode = previousNode;
    }

    public TaskWindow getTaskWindow() {
        return taskWindow;
    }

    public void setTaskWindow(TaskWindow taskWindow) {
        this.taskWindow = taskWindow;
    }

    //
    // Comparison
    //

    @Override
    public int compareTo(@NotNull ParticipantTaskQueueEntry other) {
        if (other == null) {
            return (1);
        }
        boolean thisHasNoLowerBound = false;
        if (getTaskWindow() == null) {
            thisHasNoLowerBound = true;
        } else {
            thisHasNoLowerBound = getTaskWindow().getLowerBound().getWindowBoundaryCondition().equals(TaskWindowBoundaryConditionEnum.TASK_WINDOW_BOUNDARY_NONE);
        }
        boolean otherHasNoLowerBound = false;
        if (other.getTaskWindow() == null) {
            otherHasNoLowerBound = true;
        } else {
            otherHasNoLowerBound = other.getTaskWindow().getLowerBound().getWindowBoundaryCondition().equals(TaskWindowBoundaryConditionEnum.TASK_WINDOW_BOUNDARY_NONE);
        }
        boolean schedulingPlaysNoPart = thisHasNoLowerBound && otherHasNoLowerBound;
        boolean schedulingAppliesToBoth = !thisHasNoLowerBound && !otherHasNoLowerBound;

        if (schedulingAppliesToBoth) {
            boolean thisBeforeOtherScheduleWise = getTaskWindow().getLowerBound().getWindowsBoundaryInstant().isBefore(other.getTaskWindow().getLowerBound().getWindowsBoundaryInstant());
            if (thisBeforeOtherScheduleWise) {
                return (-1);
            } else {
                return (1);
            }
        }

        if (schedulingPlaysNoPart) {
            if (getSequenceNumber().getMajorSequenceNumber() > other.getSequenceNumber().getMajorSequenceNumber()) {
                return (1);
            }
            if (getSequenceNumber().getMajorSequenceNumber() < other.getSequenceNumber().getMinorSequenceNumber()) {
                return (-1);
            }
            // major sequence numbers are equal, so check minor sequence number
            if (getSequenceNumber().getMinorSequenceNumber() > other.getSequenceNumber().getMinorSequenceNumber()) {
                return (1);
            }
            if (getSequenceNumber().getMinorSequenceNumber() < other.getSequenceNumber().getMinorSequenceNumber()) {
                return (-1);
            }
            // minor sequence numbers are equal, so return zero
            return (0);
        }

        /*
        To get to hear, either this or other DOESN'T have a lowerBound in their taskWindow (or they don't have a
        taskWindow at all). So, now we have to ascertain that, for the one that does, are we IN that Task Window (i.e.
        now is after the lowerBound -> because then normal sequenceNumber based comparison applies! If we aren't than
        the one that DOESN'T have a lower bound should be queued first.
         */

        Instant now = Instant.now();

        boolean inThisScheduledWindow = false;
        boolean inOtherScheduledWindow = false;

        if (!thisHasNoLowerBound) {
            if (now.isAfter(getTaskWindow().getLowerBound().getWindowsBoundaryInstant())) {
                inThisScheduledWindow = true;
            }
        }

        if (!otherHasNoLowerBound) {
            if (now.isAfter(other.getTaskWindow().getLowerBound().getWindowsBoundaryInstant())) {
                inOtherScheduledWindow = true;
            }
        }

        if (inThisScheduledWindow || inOtherScheduledWindow) {
            // We are in the execution window, so queue is just based on sequence numbering
            if (getSequenceNumber().getMajorSequenceNumber() > other.getSequenceNumber().getMajorSequenceNumber()) {
                return (1);
            }
            if (getSequenceNumber().getMajorSequenceNumber() < other.getSequenceNumber().getMinorSequenceNumber()) {
                return (-1);
            }
            // major sequence numbers are equal, so check minor sequence number
            if (getSequenceNumber().getMinorSequenceNumber() > other.getSequenceNumber().getMinorSequenceNumber()) {
                return (1);
            }
            if (getSequenceNumber().getMinorSequenceNumber() < other.getSequenceNumber().getMinorSequenceNumber()) {
                return (-1);
            }
            // minor sequence numbers are equal, so return zero
            return (0);
        }

        /*
         1. we've ascertained that EITHER this or other HAS a taskWindow with a lowerBound.
         2. we've ascertained that, for which one has the taskWindow with a lowerBound, we are NOT in that window right now
         3. so, which one DOESN'T have a taskWindows with lowerBound is, therefore, the one we queue first.....
         */
        if (thisHasNoLowerBound) {
            return (-1);
        }
        return (1);
    }


    //
    // toString
    //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParticipantTaskQueueEntry{");
        sb.append(", taskId=").append(taskId);
        sb.append(", taskWindow=").append(taskWindow);
        if (getPreviousNode() != null) {
            sb.append(", previousEntry=").append(getPreviousNode().getSequenceNumber());
        } else {
            sb.append(", nextEntry=").append("null");
        }
        if (getNextNode() != null) {
            sb.append(", nextEntry=").append(getNextNode().getSequenceNumber());
        } else {
            sb.append(", nextEntry=").append("null");
        }
        sb.append(", localCacheStatus=").append(getLocalCacheStatus());
        sb.append(", localCacheLocation=").append(getLocalCacheLocation());
        sb.append(", localCacheInstant=").append(getLocalCacheInstant());
        sb.append(", centralCacheStatus=").append(getCentralCacheStatus());
        sb.append(", centralCacheLocation=").append(getCentralCacheLocation());
        sb.append(", centralCacheInstant=").append(getCentralCacheInstant());
        sb.append(", persistenceStatus=").append(getPersistenceStatus());
        sb.append(", persistenceInstant=").append(getPersistenceInstant());
        sb.append(", persistenceLocation=").append(getPersistenceLocation());
        sb.append('}');
        return sb.toString();
    }
}
