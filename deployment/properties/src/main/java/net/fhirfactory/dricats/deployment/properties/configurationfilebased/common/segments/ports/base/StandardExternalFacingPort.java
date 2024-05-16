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
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.connectedsystems.ConnectedSystemProperties;
import org.slf4j.Logger;

public abstract class StandardExternalFacingPort extends ConfigurableNodeSegment {

    private String portType;
    private boolean server;
    private boolean encrypted;
    private Integer startupDelay;
    private String name;
    private ConnectedSystemProperties connectedSystem;

    //
    // Constructor(s)
    //

    public StandardExternalFacingPort() {
        super();
        this.startupDelay = null;
        this.server = false;
        this.encrypted = false;
        this.startupDelay = 0;
        this.name = null;
        this.connectedSystem = new ConnectedSystemProperties();
    }

    //
    // Getters and Setters
    //

    protected abstract Logger specifyLogger();

    @JsonIgnore
    protected Logger getLogger() {
        return (specifyLogger());
    }

    public ConnectedSystemProperties getConnectedSystem() {
        return connectedSystem;
    }

    public void setConnectedSystem(ConnectedSystemProperties connectedSystem) {
        this.connectedSystem = connectedSystem;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Integer getStartupDelay() {
        return startupDelay;
    }

    public void setStartupDelay(Integer startupDelay) {
        this.startupDelay = startupDelay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "StandardExternalFacingPort{" +
                "otherConfigurationParameters=" + otherConfigurationParameters +
                ", otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", portType='" + portType + '\'' +
                ", server=" + server +
                ", encrypted=" + encrypted +
                ", startupDelay=" + startupDelay +
                ", name='" + name + '\'' +
                ", connectedSystem=" + connectedSystem +
                '}';
    }
}
