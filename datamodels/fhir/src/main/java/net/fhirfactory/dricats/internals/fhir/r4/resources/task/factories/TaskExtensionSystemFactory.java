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
package net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.internals.fhir.r4.resources.task.valuesets.TaskExtensionSystemEnum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskExtensionSystemFactory {

    private static final String DRICATS_TASK_EXTENSION_SYSTEM = "/extension/task";

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    //
    // Business Logic
    //

    public String getDRICaTSTaskExtensionSystem() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_EXTENSION_SYSTEM;
        return (codeSystem);
    }

    public String getDricatsTaskExtensionSystemURL(TaskExtensionSystemEnum taskExtensionSystem) {
        if (taskExtensionSystem == null) {
            return (null);
        }
        String system = getDRICaTSTaskExtensionSystem() + taskExtensionSystem.getToken();
        return (system);
    }


}

