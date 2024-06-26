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
package net.fhirfactory.dricats.core.model.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.component.SoftwareComponent;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointFunctionTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointStatusEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;

import java.util.Objects;

public abstract class PetasosEndpoint extends SoftwareComponent {
    private String endpointDescription;
    private PetasosEndpointStatusEnum endpointStatus;
    private PetasosEndpointFunctionTypeEnum interfaceFunction;
    private ComponentIdType enablingProcessingPlantId;
    private PetasosEndpointTopologyTypeEnum endpointType;

    //
    // Constructor(s)
    //

    public PetasosEndpoint() {
        super();
        this.endpointStatus = PetasosEndpointStatusEnum.PETASOS_ENDPOINT_STATUS_STARTED;
        this.interfaceFunction = null;
        this.endpointDescription = null;
        this.enablingProcessingPlantId = null;
        this.endpointType = null;
    }

    public PetasosEndpoint(PetasosEndpoint ori) {
        super(ori);
        if (ori.hasEndpointDescription()) {
            this.setEndpointDescription(ori.getEndpointDescription());
        }
        this.setEndpointStatus(ori.getEndpointStatus());
        this.setInterfaceFunction(ori.getInterfaceFunction());
        if (ori.hasEnablingProcessingPlant()) {
            setEnablingProcessingPlantId(ori.getEnablingProcessingPlantId());
        }
        if (ori.hasEndpointType()) {
            setEndpointType(ori.getEndpointType());
        }
    }

    //
    // Getters and Setters
    //

    @JsonIgnore
    public boolean hasEndpointType() {
        boolean hasValue = this.endpointType != null;
        return (hasValue);
    }

    public PetasosEndpointTopologyTypeEnum getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(PetasosEndpointTopologyTypeEnum endpointType) {
        this.endpointType = endpointType;
    }


    @JsonIgnore
    public boolean hasEnablingProcessingPlant() {
        boolean hasValue = this.enablingProcessingPlantId != null;
        return (hasValue);
    }

    public ComponentIdType getEnablingProcessingPlantId() {
        return enablingProcessingPlantId;
    }

    public void setEnablingProcessingPlantId(ComponentIdType enablingProcessingPlantId) {
        this.enablingProcessingPlantId = enablingProcessingPlantId;
    }

    public PetasosEndpointFunctionTypeEnum getInterfaceFunction() {
        return interfaceFunction;
    }

    public void setInterfaceFunction(PetasosEndpointFunctionTypeEnum interfaceFunction) {
        this.interfaceFunction = interfaceFunction;
    }


    public PetasosEndpointStatusEnum getEndpointStatus() {
        return endpointStatus;
    }

    public void setEndpointStatus(PetasosEndpointStatusEnum endpointStatus) {
        this.endpointStatus = endpointStatus;
    }

    @JsonIgnore
    public boolean hasEndpointDescription() {
        boolean hasValue = this.endpointDescription != null;
        return (hasValue);
    }

    public String getEndpointDescription() {
        return endpointDescription;
    }

    public void setEndpointDescription(String endpointDescription) {
        this.endpointDescription = endpointDescription;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "PetasosEndpoint{" +
                "processingPlantServiceName='" + getSubsystemParticipantName() + '\'' +
                ", componentFDN=" + getComponentFDN() +
                ", kubernetesDeployed=" + isKubernetesDeployed() +
                ", otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", concurrencyMode=" + getConcurrencyMode() +
                ", resilienceMode=" + getResilienceMode() +
                ", securityZone=" + getSecurityZone() +
                ", componentID=" + getComponentID() +
                ", nodeFunctionFDN=" + getNodeFunctionFDN() +
                ", componentType=" + getComponentType() +
                ", containingNodeFDN=" + getContainingNodeFDN() +
                ", componentRDN=" + getComponentRDN() +
                ", metrics=" + getMetrics() +
                ", componentSystemRole=" + getComponentSystemRole() +
                ", componentStatus=" + getComponentStatus() +
                ", endpointDescription='" + endpointDescription + '\'' +
                ", endpointStatus=" + endpointStatus +
                ", interfaceFunction=" + interfaceFunction +
                '}';
    }

    //
    // Equals and HashCode
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosEndpoint)) return false;
        PetasosEndpoint that = (PetasosEndpoint) o;
        return Objects.equals(getEndpointDescription(), that.getEndpointDescription()) && getEndpointStatus() == that.getEndpointStatus() && getInterfaceFunction() == that.getInterfaceFunction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEndpointDescription(), getEndpointStatus(), getInterfaceFunction());
    }

}
