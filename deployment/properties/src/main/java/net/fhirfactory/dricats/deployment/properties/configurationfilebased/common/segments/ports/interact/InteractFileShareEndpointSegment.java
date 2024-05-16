package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardClientPortSegment;

public class InteractFileShareEndpointSegment extends StandardClientPortSegment {

    private String fileShareName;
    private String fileShareProtocol;
    private String fileSharePath;
    private String fileSharePathAlias;
    private String fileShareServer;

    public String getFileShareName() {
        return fileShareName;
    }

    public void setFileShareName(String fileShareName) {
        this.fileShareName = fileShareName;
    }

    public String getFileShareProtocol() {
        return fileShareProtocol;
    }

    public void setFileShareProtocol(String fileShareProtocol) {
        this.fileShareProtocol = fileShareProtocol;
    }

    public String getFileSharePath() {
        return fileSharePath;
    }

    public void setFileSharePath(String fileSharePath) {
        this.fileSharePath = fileSharePath;
    }

    public String getFileSharePathAlias() {
        return fileSharePathAlias;
    }

    public void setFileSharePathAlias(String fileSharePathAlias) {
        this.fileSharePathAlias = fileSharePathAlias;
    }

    public String getFileShareServer() {
        return fileShareServer;
    }

    public void setFileShareServer(String fileShareServer) {
        this.fileShareServer = fileShareServer;
    }
}
