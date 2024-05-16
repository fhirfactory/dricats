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
package net.fhirfactory.dricats.datagrid.cachestore.fhirserver.common.valuesets;

public enum ConfigurationElementEnum {
    // must be first
    UNKNOWN(null),

    FHIR_SERVER_CACHE_STORE("fhir-server-cache-store"),
    FHIR_SERVER_CLIENT("fhir-server-client");

    private String elementName;

    private ConfigurationElementEnum(String name) {
        this.elementName = name;
    }

    public static ConfigurationElementEnum fromElementName(String name) {
        for (ConfigurationElementEnum currentElement : values()) {
            if (currentElement.getElementName().contentEquals(name)) {
                return (currentElement);
            }
        }
        return (UNKNOWN);
    }

    public String getElementName() {
        return (elementName);
    }
}
