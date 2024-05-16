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
package net.fhirfactory.dricats.core.constants.subsystems;

public enum MLLPComponentConfigurationConstantsEnum {

    CAMEL_MLLP_RETRIES("CAMEL_MLLP_RETRIES", "", "1"),
    CAMEL_MLLP_RETRY_PERIOD("CAMEL_MLLP_RETRY_PERIOD", "", "60"),
    CAMEL_MLLP_CONNECTION_TIMEOUT("CAMEL_MLLP_CONNECTION_TIMEOUT", "connectTimeout", "30000"),
    CAMEL_MLLP_CONNECTION_IDLE_TIMEOUT("CAMEL_MLLP_CONNECTION_IDLE_TIMEOUT", "idleTimeout", "30000"),
    CAMEL_MLLP_CONNECTION_IDLE_TIMEOUT_STRATEGY("CAMEL_MLLP_CONNECTION_IDLE_TIMEOUT_STRATEGY", "idleTimeoutStrategy", "RESET"),
    CAMEL_MLLP_ACCEPT_TIMEOUT("CAMEL_MLLP_ACCEPT_TIMEOUT", "acceptTimeout", "30000"),
    CAMEL_MLLP_BIND_TIMEOUT("CAMEL_MLLP_BIND_TIMEOUT", "bindTimeout", "30000"),
    CAMEL_MLLP_MAX_CONCURRENT_SESSIONS("CAMEL_MLLP_MAX_CONCURRENT_SESSIONS", "maxConcurrentConsumers", "30"),
    CAMEL_MLLP_KEEPALIVE("CAMEL_MLLP_KEEPALIVE", "keepAlive", "true"),
    CAMEL_MLLP_PAYLOAD_VALIDATION("CAMEL_MLLP_PAYLOAD_VALIDATION", "validatePayload", "false"),
    CAMEL_MLLP_MINIMAL_CONFORMANCE_ENFORCEMENT("TRIGGER_EVENT_MINIMAL_CONFORMANCE_ENFORCEMENT", "validatePayload", "false"),
    CAMEL_MLLP_STRING_PAYLOAD("CAMEL_MLLP_STRING_PAYLOAD", "stringPayload", "true"),

    CAMEL_MLLP_ACKNOWLEDGEMENT_TIMEOUT("CAMEL_MLLP_ACKNOWLEDGEMENT_TIMEOUT", "not_supported", "30000");

    private String configurationFileAttributeName;
    private String camelConfigurationName;
    private String defaultValue;

    private MLLPComponentConfigurationConstantsEnum(String attributeName, String camelConfigurationName, String defaultValue) {
        this.configurationFileAttributeName = attributeName;
        this.camelConfigurationName = camelConfigurationName;
        this.defaultValue = defaultValue;
    }

    public String getConfigurationFileAttributeName() {
        return (this.configurationFileAttributeName);
    }

    public String getCamelConfigurationName() {
        return (this.camelConfigurationName);
    }

    public String getDefaultValue() {
        return (this.defaultValue);
    }
}
