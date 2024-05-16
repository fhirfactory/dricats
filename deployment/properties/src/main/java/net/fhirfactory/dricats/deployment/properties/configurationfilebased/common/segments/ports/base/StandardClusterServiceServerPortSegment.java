/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base;

public abstract class StandardClusterServiceServerPortSegment extends StandardServerPortSegment {
    private Integer servicePort;
    private String servicePortName;
    private String serviceDNS;
    private Integer clusterServicePortOffsetValue;

    //
    // Constructor(s)
    //

    public StandardClusterServiceServerPortSegment() {
        super();
        clusterServicePortOffsetValue = null;
        this.serviceDNS = null;
        this.servicePortName = null;
        this.servicePort = null;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public String getServicePortName() {
        return servicePortName;
    }

    public void setServicePortName(String servicePortName) {
        this.servicePortName = servicePortName;
    }

    public Integer getClusterServicePortIncludingOffset(Integer offset) {
        if (clusterServicePortOffsetValue == null) {
            getLogger().error(".getEdgeReceiveCommunicationClusterServicePort(): Not such port type defined");
            return (0);
        }
        Integer servicePortNumber = offset + getClusterServicePortOffsetValue();
        return (servicePortNumber);
    }

    public Integer getClusterServicePortOffsetValue() {
        return clusterServicePortOffsetValue;
    }

    public void setClusterServicePortOffsetValue(Integer clusterServicePortOffsetValue) {
        this.clusterServicePortOffsetValue = clusterServicePortOffsetValue;
    }

    public String getServiceDNS() {
        return serviceDNS;
    }

    public void setServiceDNS(String serviceDNS) {
        this.serviceDNS = serviceDNS;
    }

    @Override
    public String toString() {
        return "StandardClusterServicePortSegment{" +
                "portValue=" + getServerPort() +
                ", portType=" + getPortType() +
                ", isServer=" + isServer() +
                ", isEncrypted=" + isEncrypted() +
                ", hostDNSEntry=" + getServerHostname() +
                ", supportedInterfaceProfiles=" + getSupportedInterfaceProfiles() +
                ", servicePortValue=" + servicePort +
                ", servicePortName=" + servicePortName +
                ", serviceDNSEntry=" + serviceDNS +
                ", clusterServicePortOffsetValue=" + clusterServicePortOffsetValue +
                '}';
    }
}
