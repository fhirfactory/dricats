/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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

package net.fhirfactory.dricats.core.constants.systemwide;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Mark A. Hunter
 */
@ApplicationScoped
public class PegacornReferenceProperties {

    private static final String PEGACORN_INTERNAL_FHIR_RESOURCE_R4_PATH = "/pegacorn/internal/fhir/r4";
    private static final String PEGACORN_EXPOSED_FHIR_RESOURCE_R4_PATH = "/fhir/r4";
    private static final String PEGACORN_PETASOS_STATUS_PATH = "/pegacorn/internal/petasos/status";
    private static final String PEGACORN_PETASOS_REPLICATION_PATH = "/pegacorn/internal/petasos/replication";
    private static final String PEGACORN_PETASOS_IPC_PATH = "/pegacorn/internal/petasos/ipc";
    private static final String PEGACORN_RESOURCE_DIRECTORY_R1_PATH = "/pegacorn/operations/directory/r1";
    private static final String PEGACORN_COMMUNICATE_DIRECTORY_R1_PATH = "/pegacorn/communicate/directory/r1";
    @Inject
    private DeploymentSystemIdentificationInterface systemIdentification;

    public static String getPegacornPetasosStatusPath() {
        return PEGACORN_PETASOS_STATUS_PATH;
    }

    public static String getPegacornResourceDirectoryR1Path() {
        return PEGACORN_RESOURCE_DIRECTORY_R1_PATH;
    }

    public static String getPegacornCommunicateDirectoryR1Path() {
        return PEGACORN_COMMUNICATE_DIRECTORY_R1_PATH;
    }

    public static String getPegacornPetasosReplicationPath() {
        return PEGACORN_PETASOS_REPLICATION_PATH;
    }

    public static String getPegacornPetasosIPCPath() {
        return PEGACORN_PETASOS_IPC_PATH;
    }

    public String getSystemDeploymentName() {
        return (systemIdentification.getSystemName());
    }

    public String getPegacornInternalFhirResourceR4Path() {
        return PEGACORN_INTERNAL_FHIR_RESOURCE_R4_PATH;
    }

    public String getPegacornExposedFhirResourceR4Path() {
        return PEGACORN_EXPOSED_FHIR_RESOURCE_R4_PATH;
    }

    public String getPegacornCodeSystemSite() {
        return ("http://ontology.fhirfactory.net/fhir/code-systems");
    }

    public String getContainmentSeparator() {
        return ("+");
    }

    public String getPegacornDefaultFHIRVersion() {
        return ("4.0.1");
    }

    public String getLocalCodeSystemPath() {
        return ("/local/fhir/code-systems");
    }

    public String getITOpsContextPath() {
        return ("/pegacorn/internal/itops/r1");
    }

    public String getAuditDMContextPath() {
        return ("/pegacorn/hestia/audit-event");
    }


    public String getTaskDMContextPath() {
        return ("/pegacorn/hestia/task");
    }

    //
    // Audit Event Details
    //

}
