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
package net.fhirfactory.dricats.internals.fhir.r4.resources.device.valuesets;

public enum DevicePropertyTypeEnum {
    DEVICE_PROPERTY_KUBERNETES_POD_ID("Pod Id", "pegacorn.fhir.device.software_component.kubernetes_pod_id", "Software Component Kubernetes Execution Pod ID"),
    DEVICE_PROPERTY_KUBERNETES_POD_IP("Pod IP Address", "pegacorn.fhir.device.software_component.kubernetes_pod_ip_address", "Software Component Kubernetes Execution Pod IP Address"),
    DEVICE_PROPERTY_PLATFORM_IP("Host IP Address", "pegacorn.fhir.device.software_component.host_ip_address", "Software Component Host IP Address (or Kubernetes Worker Node IP Address)"),
    DEVICE_PROPERTY_RESILIENCE_MODE("Resilience Mode", "pegacorn.fhir.device.software_component.resilience_mode", "Software Component Resilience Mode"),
    DEVICE_PROPERTY_CONCURRENCY_MODE("Concurency Mode", "pegacorn.fhir.device.software_component.concurrency_mode", "Software Component Concurrency Mode"),
    DEVICE_PROPERTY_SECURITY_ZONE("Security Zone", "pegacorn.fhir.device.software_component.security_zone", "Software Component Security Zone");

    private String displayName;
    private String token;
    private String displayText;

    private DevicePropertyTypeEnum(String name, String code, String text) {
        this.displayText = text;
        this.token = code;
        this.displayName = name;
    }

    public String getDisplayName() {
        return (this.displayName);
    }

    public String getToken() {
        return (this.token);
    }

    public String getDisplayText() {
        return (this.displayText);
    }

}
