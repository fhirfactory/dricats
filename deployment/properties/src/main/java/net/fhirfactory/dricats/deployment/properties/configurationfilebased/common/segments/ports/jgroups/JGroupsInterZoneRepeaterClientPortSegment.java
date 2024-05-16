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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.InterfaceDefinitionSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardClientPortSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JGroupsInterZoneRepeaterClientPortSegment extends StandardClientPortSegment {
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsInterZoneRepeaterClientPortSegment.class);

    private static String INTERFACE_DEFINITION_NAME = "JGroups-IZR-Client";
    private static String INTERFACE_DEFINITION_VERSION = "1.0.0";

    private Integer targetPortValue;
    private String targetHostName;
    private boolean encrypted;
    private List<InterfaceDefinitionSegment> supportedInterfaceProfiles;

    //
    // Constructor(s)
    //

    public JGroupsInterZoneRepeaterClientPortSegment() {
        this.targetHostName = null;
        this.targetPortValue = null;
        this.encrypted = false;
        this.supportedInterfaceProfiles = new ArrayList<>();
    }

    //
    // Getter and Setters
    //

    public List<InterfaceDefinitionSegment> getSupportedInterfaceProfiles() {
        return supportedInterfaceProfiles;
    }

    public void setSupportedInterfaceProfiles(List<InterfaceDefinitionSegment> supportedInterfaceProfiles) {
        this.supportedInterfaceProfiles = supportedInterfaceProfiles;
    }

    public String getInterfaceDefinitionName() {
        return INTERFACE_DEFINITION_NAME;
    }

    public String getInterfaceDefinitionVersion() {
        return INTERFACE_DEFINITION_VERSION;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Integer getTargetPortValue() {
        return targetPortValue;
    }

    public void setTargetPortValue(Integer targetPortValue) {
        this.targetPortValue = targetPortValue;
    }

    public String getTargetHostName() {
        return targetHostName;
    }

    public void setTargetHostName(String targetHostName) {
        this.targetHostName = targetHostName;
    }

    @Override
    protected Logger specifyLogger() {
        return (LOG);
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "JGroupsInterZoneRepeaterClientPortSegment{" +
                "otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", defaultRetryCount=" + getDefaultRetryCount() +
                ", defaultRetryWait=" + getDefaultRetryWait() +
                ", defaultTimeout=" + getDefaultTimeout() +
                ", connectedSystem=" + getConnectedSystem() +
                ", portType='" + getPortType() + '\'' +
                ", server=" + isServer() +
                ", startupDelay=" + getStartupDelay() +
                ", name='" + getName() + '\'' +
                ", targetPortValue=" + targetPortValue +
                ", targetHostName='" + targetHostName + '\'' +
                ", encrypted=" + encrypted +
                ", supportedInterfaceProfiles=" + supportedInterfaceProfiles +
                ", interfaceDefinitionName='" + getInterfaceDefinitionName() + '\'' +
                ", interfaceDefinitionVersion='" + getInterfaceDefinitionVersion() + '\'' +
                '}';
    }
}
