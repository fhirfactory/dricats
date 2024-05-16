package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardClientPortSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPClientPortSegment extends StandardClientPortSegment {
    private static Logger LOG = LoggerFactory.getLogger(HTTPClientPortSegment.class);

    private int defaultRetryCount;
    private int defaultRetryWait;
    private int defaultTimeout;
    private String contextPath;

    //
    // Constructor(s)
    //

    public HTTPClientPortSegment() {
        super();
        this.defaultRetryCount = 3;
        this.defaultRetryWait = 30;
        this.defaultTimeout = 60;
        this.contextPath = null;
    }

    //
    // Getters and Setters
    //

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getDefaultRetryCount() {
        return defaultRetryCount;
    }

    public void setDefaultRetryCount(int defaultRetryCount) {
        this.defaultRetryCount = defaultRetryCount;
    }

    public int getDefaultRetryWait() {
        return defaultRetryWait;
    }

    public void setDefaultRetryWait(int defaultRetryWait) {
        this.defaultRetryWait = defaultRetryWait;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    protected Logger specifyLogger() {
        return (LOG);
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "HTTPClientPortSegment{" +
                "otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", connectedSystem=" + getConnectedSystem() +
                ", portType='" + getPortType() + '\'' +
                ", server=" + isServer() +
                ", encrypted=" + isEncrypted() +
                ", startupDelay=" + getStartupDelay() +
                ", name='" + getName() + '\'' +
                ", defaultRetryCount=" + defaultRetryCount +
                ", defaultRetryWait=" + defaultRetryWait +
                ", defaultTimeout=" + defaultTimeout +
                ", contextPath='" + contextPath + '\'' +
                '}';
    }
}
