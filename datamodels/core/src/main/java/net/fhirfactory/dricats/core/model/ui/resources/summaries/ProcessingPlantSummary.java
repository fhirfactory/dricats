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
package net.fhirfactory.dricats.core.model.ui.resources.summaries;

import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;

import java.util.HashMap;
import java.util.Map;

public class ProcessingPlantSummary extends SoftwareComponentSummary {
    private String platformID;
    private String securityZone;
    private String site;
    private Integer replicationCount;
    private Map<ComponentIdType, WorkshopSummary> workshops;
    private String actualHostIP;
    private String actualPodIP;

    //
    // Constructor(s)
    //

    public ProcessingPlantSummary() {
        super();
        this.platformID = null;
        this.actualHostIP = null;
        this.actualPodIP = null;
        this.replicationCount = null;
        this.site = null;
        this.workshops = new HashMap<>();
        this.securityZone = null;
    }

    //
    // To String
    //

    public Integer getReplicationCount() {
        return replicationCount;
    }

    public void setReplicationCount(Integer replicationCount) {
        this.replicationCount = replicationCount;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public Map<ComponentIdType, WorkshopSummary> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(Map<ComponentIdType, WorkshopSummary> workshops) {
        this.workshops = workshops;
    }

    public void addWorkshop(WorkshopSummary workshop) {
        removeWorkshop(workshop.getComponentID());
        workshops.put(workshop.getComponentID(), workshop);
    }

    public void removeWorkshop(ComponentIdType componentID) {
        if (workshops.containsKey(componentID)) {
            workshops.remove(componentID);
        }
    }

    public String getSecurityZone() {
        return securityZone;
    }

    public void setSecurityZone(String securityZone) {
        this.securityZone = securityZone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getActualHostIP() {
        return actualHostIP;
    }

    public void setActualHostIP(String actualHostIP) {
        this.actualHostIP = actualHostIP;
    }

    public String getActualPodIP() {
        return actualPodIP;
    }

    public void setActualPodIP(String actualPodIP) {
        this.actualPodIP = actualPodIP;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "ProcessingPlantSummary{" +
                "platformID='" + platformID + '\'' +
                ", securityZone='" + securityZone + '\'' +
                ", site='" + site + '\'' +
                ", workshops=" + workshops +
                ", actualHostIP='" + actualHostIP + '\'' +
                ", actualPodIP='" + actualPodIP + '\'' +
                ", participantName='" + getParticipantName() + '\'' +
                ", subsystemParticipantName='" + getSubsystemParticipantName() + '\'' +
                ", topologyNodeFDN=" + getTopologyNodeFDN() +
                ", componentID=" + getComponentID() +
                ", nodeVersion='" + getNodeVersion() + '\'' +
                ", nodeType=" + getNodeType() +
                ", concurrencyMode='" + getConcurrencyMode() + '\'' +
                ", resilienceMode='" + getResilienceMode() + '\'' +
                ", lastSynchronisationInstant=" + getLastSynchronisationInstant() +
                ", lastActivityInstant=" + getLastActivityInstant() +
                ", resourceId='" + getResourceId() + '\'' +
                ", replicationCount=" + replicationCount +
                '}';
    }
}
