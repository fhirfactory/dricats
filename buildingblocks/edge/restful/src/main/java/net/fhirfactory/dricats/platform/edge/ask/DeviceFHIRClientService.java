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
package net.fhirfactory.dricats.platform.edge.ask;

import ca.uhn.fhir.rest.api.MethodOutcome;
import net.fhirfactory.dricats.platform.edge.ask.base.ResourceFHIRClientService;
import org.hl7.fhir.r4.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceFHIRClientService extends ResourceFHIRClientService {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceFHIRClientService.class);

    //
    // Constructor(s)
    //

    public DeviceFHIRClientService() {
        super();
    }

    //
    // Getters (and Setters)
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    //
    // Business Methods
    //

    public MethodOutcome createDevice(String deviceJSONString) {
        getLogger().debug(".createDevice(): Entry, deviceJSONString->{}", deviceJSONString);
        Device device = getFHIRParser().parseResource(Device.class, deviceJSONString);
        MethodOutcome outcome = createResource(device);
        getLogger().debug(".createDevice(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    public MethodOutcome createDevice(Device device) {
        getLogger().debug(".createDevice(): Entry, device->{}", device);
        MethodOutcome outcome = createResource(device);
        getLogger().debug(".createDevice(): Exit, outcome->{}", outcome);
        return (outcome);
    }
}
