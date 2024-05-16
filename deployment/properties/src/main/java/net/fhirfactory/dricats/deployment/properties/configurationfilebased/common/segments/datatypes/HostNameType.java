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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.datatypes;

import java.io.Serializable;
import java.util.Objects;

public class HostNameType implements Serializable {
    private String ipAddress;
    private String name;

    //
    // Constructor(s)
    //

    public HostNameType() {
        this.ipAddress = null;
        this.name = null;
    }

    //
    // Getters and Setters
    //

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //
    // ToString
    //

    @Override
    public String toString() {
        return "HostNameType{" +
                "ipAddress='" + ipAddress + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    //
    // equals and hash
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostNameType)) return false;
        HostNameType that = (HostNameType) o;
        return Objects.equals(getIpAddress(), that.getIpAddress()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIpAddress(), getName());
    }
}
