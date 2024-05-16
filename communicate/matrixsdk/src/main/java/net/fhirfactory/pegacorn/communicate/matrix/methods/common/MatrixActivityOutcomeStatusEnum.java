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

public enum MatrixActivityOutcomeStatusEnum {
    MATRIX_ACTIVITY_OUTCOME_NO_ACTION("matrix.activity-outcome.no-action"),
    MATRIX_ACTIVITY_OUTCOME_NO_RESULT("matrix.activity-outcome.no-result"),
    MATRIX_ACTIVITY_OUTCOME_FAILED("matrix.activity-outcome.failed"),
    MATRIX_ACTIVITY_OUTCOME_SUCCESS("matrix.activity-outcome.success");

    private String activityOutcome;

    private MatrixActivityOutcomeStatusEnum(String outcome) {
        activityOutcome = outcome;
    }

    public String getActivityOutcome() {
        return activityOutcome;
    }
}
