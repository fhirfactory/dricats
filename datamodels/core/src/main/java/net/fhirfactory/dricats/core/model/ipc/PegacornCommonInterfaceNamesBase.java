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
package net.fhirfactory.dricats.core.model.ipc;

import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;

public abstract class PegacornCommonInterfaceNamesBase {

    public String getPetasosReplication() {
        return ("PetasosReplication");
    }

    public String getPetasosStatus() {
        return ("PetasosStatus");
    }

    public String getPetasosFinalisation() {
        return ("PetasosFinalisation");
    }

    public String getPetasosTopology() {
        return ("PetasosTopology");
    }

    //
    // Property Names
    //
    public String getPrometheusEndpointName() {
        return ("KubernetesSystemPrometheus");
    }

    public String getJolokiaEndpointName() {
        return ("KubernetesSystemJolokia");
    }

    public String getKubeReadinessEndpointName() {
        return ("KubernetesSystemReadiness");
    }

    public String getKubeLivelinessEndpointName() {
        return ("KubernetesSystemLiveliness");
    }

    public String getEdgeAnswerEndpointName() {
        return ("edge-answer");
    }

    public String getEdgeAskEndpointName() {
        return ("edge-ask");
    }

    //
    // Default ProcessorPlant Interface Binding
    //
    public String getDefaultInterfaceNameForBinding() {
        return ("0.0.0.0");
    }

    //
    // Endpoint Name Builder
    //
    public String getEndpointServerName(String endpointFunction) {
        String endpointServerName = "IPCEndpoint:Server(" + endpointFunction + ")";
        return (endpointServerName);
    }

    public String getEndpointClientName(String endpointFunction) {
        String endpointClientName = "IPCEndpoint:Client(" + endpointFunction + ")";
        return (endpointClientName);
    }

    public String getEndpointName(PetasosEndpointTopologyTypeEnum endpointType, String endpointFunction) {
        String endpointClientName = endpointType.getDisplayName() + "::" + endpointFunction;
        return (endpointClientName);
    }

    //
    // Connection Name Builder
    //

    public String getConnectionNameAtoB(String clientEndpoint, String serverEndpoint) {
        String newName = "ActiveIPCConnection:" + clientEndpoint + "-" + serverEndpoint;
        return (newName);
    }


}
