package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.fhirpersistence.dm;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;

public abstract class FHIRDataManagerPropertyFile extends BaseSubsystemPropertyFile {
    private ClusteredHTTPServerPortSegment fhirJPAServerPort;

    //
    // Constructor(s)
    //

    public FHIRDataManagerPropertyFile() {
        super();
        this.fhirJPAServerPort = null;
    }

    //
    // Getters and Setters
    //

    public ClusteredHTTPServerPortSegment getFHIRJPAServerPort() {
        return fhirJPAServerPort;
    }

    public void getFHIRJPAServerPort(ClusteredHTTPServerPortSegment edgeAnswer) {
        this.fhirJPAServerPort = edgeAnswer;
    }
}
