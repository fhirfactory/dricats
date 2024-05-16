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
package net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes;

import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskWorkItemType extends UoW {
    private static final Logger LOG = LoggerFactory.getLogger(UoW.class);

    public TaskWorkItemType() {
        super();
    }

    public TaskWorkItemType(UoWPayload inputPayload) {
        super(inputPayload);
    }

    public TaskWorkItemType(UoW originalUoW) {
        super(originalUoW);
    }

    @Override
    protected Logger getLogger() {
        return (LOG);
    }
}