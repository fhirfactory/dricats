package net.fhirfactory.dricats.deployment.properties.configurationfilebased.fhirbreak.ldapscanner;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractClusteredServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.fhirbreak.common.FHIRBreakSubsystemPropertyFile;

public class FHIRBreakLDAPScannerPropertyFile extends FHIRBreakSubsystemPropertyFile {

    private InteractClusteredServerPortSegment ldapProxy;
    private InteractClusteredServerPortSegment ldapServer;

    public InteractClusteredServerPortSegment getLdapProxy() {
        return ldapProxy;
    }

    public void setLdapProxy(InteractClusteredServerPortSegment ldapProxy) {
        this.ldapProxy = ldapProxy;
    }

    public InteractClusteredServerPortSegment getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(InteractClusteredServerPortSegment ldapServer) {
        this.ldapServer = ldapServer;
    }

}
