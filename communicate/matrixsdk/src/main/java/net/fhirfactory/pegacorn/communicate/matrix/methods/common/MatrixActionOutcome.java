/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.communicate.matrix.methods.common;


import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.contenttypes.MEventTypeEnum;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.message.contenttypes.MRoomMessageTypeEnum;

import java.util.Objects;

public class MatrixActionOutcome {
    private MatrixActivityOutcomeStatusEnum outcomeStatus;
    private MEventTypeEnum activityFocus;
    private MRoomMessageTypeEnum activitySubFocus;
    private String actionID;

    public MatrixActivityOutcomeStatusEnum getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(MatrixActivityOutcomeStatusEnum outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    public MEventTypeEnum getActivityFocus() {
        return activityFocus;
    }

    public void setActivityFocus(MEventTypeEnum activityFocus) {
        this.activityFocus = activityFocus;
    }

    public MRoomMessageTypeEnum getActivitySubFocus() {
        return activitySubFocus;
    }

    public void setActivitySubFocus(MRoomMessageTypeEnum activitySubFocus) {
        this.activitySubFocus = activitySubFocus;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    @Override
    public String toString() {
        return "MatrixActionOutcome{" +
                "outcomeStatus=" + outcomeStatus +
                ", activityFocus=" + activityFocus +
                ", activitySubFocus=" + activitySubFocus +
                ", actionID='" + actionID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatrixActionOutcome)) return false;
        MatrixActionOutcome that = (MatrixActionOutcome) o;
        return getOutcomeStatus() == that.getOutcomeStatus() && getActivityFocus() == that.getActivityFocus() && getActivitySubFocus() == that.getActivitySubFocus() && Objects.equals(getActionID(), that.getActionID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOutcomeStatus(), getActivityFocus(), getActivitySubFocus(), getActionID());
    }
}
