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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;

import java.util.ArrayList;

public abstract class StandardServerPortSegment extends StandardExternalFacingPort {

    private Integer serverPort;
    private String serverHostname;
    private ArrayList<InterfaceDefinitionSegment> supportedInterfaceProfiles;


    //
    // Abstract Methods
    //

    public StandardServerPortSegment() {
        super();
        this.supportedInterfaceProfiles = new ArrayList<>();
        this.serverPort = null;

    }

    //
    // Constructor(s)
    //

    protected abstract Logger specifyLogger();

    //
    // Getters and Setters
    //

    @JsonIgnore
    protected Logger getLogger() {
        return (specifyLogger());
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public ArrayList<InterfaceDefinitionSegment> getSupportedInterfaceProfiles() {
        return supportedInterfaceProfiles;
    }

    public void setSupportedInterfaceProfiles(ArrayList<InterfaceDefinitionSegment> supportedInterfaceProfiles) {
        this.supportedInterfaceProfiles = supportedInterfaceProfiles;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "StandardServerPortSegment{" +
                "otherConfigurationParameters=" + otherConfigurationParameters +
                ", otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", connectedSystem=" + getConnectedSystem() +
                ", portType='" + getPortType() + '\'' +
                ", server=" + isServer() +
                ", encrypted=" + isEncrypted() +
                ", startupDelay=" + getStartupDelay() +
                ", name='" + getName() + '\'' +
                ", portValue=" + serverPort +
                ", hostDNSEntry='" + serverHostname + '\'' +
                ", supportedInterfaceProfiles=" + supportedInterfaceProfiles +
                '}';
    }
}
