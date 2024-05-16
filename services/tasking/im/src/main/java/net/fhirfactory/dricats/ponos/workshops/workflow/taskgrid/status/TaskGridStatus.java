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
package net.fhirfactory.dricats.ponos.workshops.workflow.taskgrid.status;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class TaskGridStatus {
    private boolean startupComplete;
    private Instant startupStartInstant;
    private Instant startupCompletionInstant;

    //
    // Constructor(s)
    //

    public TaskGridStatus() {
        this.startupComplete = false;
        this.startupStartInstant = null;
        this.startupCompletionInstant = null;
    }

    //
    // Business Methods
    //

    public boolean isStartupComplete() {
        return startupComplete;
    }

    public void setStartupComplete(boolean startupComplete) {
        this.startupComplete = startupComplete;
    }

    public Instant getStartupStartInstant() {
        return startupStartInstant;
    }

    public void setStartupStartInstant(Instant startupStartInstant) {
        this.startupStartInstant = startupStartInstant;
    }

    public Instant getStartupCompletionInstant() {
        return startupCompletionInstant;
    }

    public void setStartupCompletionInstant(Instant startupCompletionInstant) {
        this.startupCompletionInstant = startupCompletionInstant;
    }
}
