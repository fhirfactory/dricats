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
package net.fhirfactory.dricats.core.model.oam.topology.valuesets;

import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;

public enum PetasosMonitoredComponentTypeEnum {
    PETASOS_MONITORED_COMPONENT_SUBSYSTEM("petasos.oam.monitored_node_type.subsystem"),
    PETASOS_MONITORED_COMPONENT_SERVICE("petasos.oam.monitored_node_type.service"),
    PETASOS_MONITORED_COMPONENT_PROCESSING_PLANT("petasos.oam.monitored_node_type.processingplant"),
    PETASOS_MONITORED_COMPONENT_WORKSHOP("petasos.aom.monitored_node_type.workshop"),
    PETASOS_MONITORED_COMPONENT_WORK_UNIT_PROCESSOR("petasos.oam.monitored_node_type.wup"),
    PETASOS_MONITORED_COMPONENT_WORK_UNIT_PROCESSOR_COMPONENT("petasos.oam.monitored_node_type.wup_component"),
    PETASOS_MONITORED_COMPONENT_ENDPOINT("petasos.oam.monitored_node_type.endpoint");

    private String nodeType;

    private PetasosMonitoredComponentTypeEnum(String nodeType) {
        this.nodeType = nodeType;
    }

    public static PetasosMonitoredComponentTypeEnum nodeTypeFromTopologyNodeType(PegacornSystemComponentTypeTypeEnum nodeType) {
        switch (nodeType) {
            case WORKSHOP:
            case OAM_WORKSHOP: {
                return (PETASOS_MONITORED_COMPONENT_WORKSHOP);
            }
            case OAM_WORK_UNIT_PROCESSOR:
            case WUP: {
                return (PETASOS_MONITORED_COMPONENT_WORK_UNIT_PROCESSOR);
            }
            case ENDPOINT: {
                return (PETASOS_MONITORED_COMPONENT_ENDPOINT);
            }
            case CLUSTER_SERVICE: {
                return (PETASOS_MONITORED_COMPONENT_SERVICE);
            }
            case SUBSYSTEM: {
                return (PETASOS_MONITORED_COMPONENT_SUBSYSTEM);
            }
            case PROCESSING_PLANT: {
                return (PETASOS_MONITORED_COMPONENT_PROCESSING_PLANT);
            }
            case WUP_CORE:
            case WUP_INTERCHANGE_ROUTER:
            case WUP_CONTAINER_EGRESS_CONDUIT:
            case WUP_CONTAINER_INGRES_CONDUIT:
            case WUP_CONTAINER_EGRESS_PROCESSOR:
            case WUP_CONTAINER_INGRES_PROCESSOR:
            case WUP_CONTAINER_EGRESS_GATEKEEPER:
            case WUP_CONTAINER_INGRES_GATEKEEPER:
            case WUP_INTERCHANGE_PAYLOAD_TRANSFORMER: {
                return (PETASOS_MONITORED_COMPONENT_WORK_UNIT_PROCESSOR_COMPONENT);
            }
            default: {
                return (null);
            }
        }
    }

    public String getNodeType() {
        return nodeType;
    }
}
