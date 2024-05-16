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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.datatypes.ParameterNameValuePairType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeploymentModeSegment {
    private boolean kubernetes;
    private Integer processingPlantReplicationCount;
    private boolean concurrent;
    private boolean usingInternalEncryption;
    private String deploymentConfig;
    private String petasosIPCStackConfigFile;
    private String petasosTopologyStackConfigFile;
    private String petasosMetricsStackConfigFile;
    private String petasosInterceptionStackConfigFile;
    private String petasosSubscriptionStackConfigFile;
    private String petasosTaskingStackConfigFile;
    private String petasosAuditStackConfigFile;
    private String multiuseInfinispanStackConfigFile;
    private List<ParameterNameValuePairType> otherDeploymentParameters;

    public DeploymentModeSegment() {
        kubernetes = false;
        processingPlantReplicationCount = 0;
        concurrent = false;
        usingInternalEncryption = false;
        deploymentConfig = null;
        this.petasosIPCStackConfigFile = null;
        this.petasosTopologyStackConfigFile = null;
        this.petasosMetricsStackConfigFile = null;
        this.petasosInterceptionStackConfigFile = null;
        this.petasosSubscriptionStackConfigFile = null;
        this.petasosTaskingStackConfigFile = null;
        this.multiuseInfinispanStackConfigFile = null;
        this.petasosAuditStackConfigFile = null;
        this.otherDeploymentParameters = new ArrayList<>();
    }

    public void mergeOverrides(DeploymentModeSegment overrides) {
        this.kubernetes = overrides.isKubernetes();
        this.concurrent = overrides.isConcurrent();
        this.usingInternalEncryption = overrides.isUsingInternalEncryption();
        if (overrides.getProcessingPlantReplicationCount() > 0) {
            this.processingPlantReplicationCount = overrides.getProcessingPlantReplicationCount();
        }
        if (!StringUtils.isEmpty(overrides.getDeploymentConfig())) {
            this.deploymentConfig = overrides.getDeploymentConfig();
        }
        if (!StringUtils.isEmpty(overrides.getPetasosIPCStackConfigFile())) {
            this.setPetasosIPCStackConfigFile(overrides.getPetasosIPCStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getMultiuseInfinispanStackConfigFile())) {
            this.setMultiuseInfinispanStackConfigFile(overrides.getMultiuseInfinispanStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosInterceptionStackConfigFile())) {
            this.setPetasosInterceptionStackConfigFile(overrides.getPetasosInterceptionStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosMetricsStackConfigFile())) {
            this.setPetasosMetricsStackConfigFile(overrides.getPetasosMetricsStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosSubscriptionStackConfigFile())) {
            this.setPetasosSubscriptionStackConfigFile(overrides.getPetasosSubscriptionStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosTaskingStackConfigFile())) {
            this.setPetasosTaskingStackConfigFile(overrides.getPetasosTaskingStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosTopologyStackConfigFile())) {
            this.setPetasosTopologyStackConfigFile(overrides.getPetasosTopologyStackConfigFile());
        }
        if (!StringUtils.isEmpty(overrides.getPetasosAuditStackConfigFile())) {
            this.setPetasosAuditStackConfigFile(overrides.getPetasosAuditStackConfigFile());
        }
    }

    //
    // Getters and Setters
    //

    public List<ParameterNameValuePairType> getOtherDeploymentParameters() {
        return otherDeploymentParameters;
    }

    public void setOtherDeploymentParameters(List<ParameterNameValuePairType> otherDeploymentParameters) {
        this.otherDeploymentParameters = otherDeploymentParameters;
    }


    public String getPetasosAuditStackConfigFile() {
        return petasosAuditStackConfigFile;
    }

    public void setPetasosAuditStackConfigFile(String petasosAuditStackConfigFile) {
        this.petasosAuditStackConfigFile = petasosAuditStackConfigFile;
    }

    public String getPetasosIPCStackConfigFile() {
        return petasosIPCStackConfigFile;
    }

    public void setPetasosIPCStackConfigFile(String petasosIPCStackConfigFile) {
        this.petasosIPCStackConfigFile = petasosIPCStackConfigFile;
    }

    public String getPetasosTopologyStackConfigFile() {
        return petasosTopologyStackConfigFile;
    }

    public void setPetasosTopologyStackConfigFile(String petasosTopologyStackConfigFile) {
        this.petasosTopologyStackConfigFile = petasosTopologyStackConfigFile;
    }

    public String getPetasosMetricsStackConfigFile() {
        return petasosMetricsStackConfigFile;
    }

    public void setPetasosMetricsStackConfigFile(String petasosMetricsStackConfigFile) {
        this.petasosMetricsStackConfigFile = petasosMetricsStackConfigFile;
    }

    public String getPetasosInterceptionStackConfigFile() {
        return petasosInterceptionStackConfigFile;
    }

    public void setPetasosInterceptionStackConfigFile(String petasosInterceptionStackConfigFile) {
        this.petasosInterceptionStackConfigFile = petasosInterceptionStackConfigFile;
    }

    public String getPetasosSubscriptionStackConfigFile() {
        return petasosSubscriptionStackConfigFile;
    }

    public void setPetasosSubscriptionStackConfigFile(String petasosSubscriptionStackConfigFile) {
        this.petasosSubscriptionStackConfigFile = petasosSubscriptionStackConfigFile;
    }

    public String getPetasosTaskingStackConfigFile() {
        return petasosTaskingStackConfigFile;
    }

    public void setPetasosTaskingStackConfigFile(String petasosTaskingStackConfigFile) {
        this.petasosTaskingStackConfigFile = petasosTaskingStackConfigFile;
    }

    public String getMultiuseInfinispanStackConfigFile() {
        return multiuseInfinispanStackConfigFile;
    }

    public void setMultiuseInfinispanStackConfigFile(String multiuseInfinispanStackConfigFile) {
        this.multiuseInfinispanStackConfigFile = multiuseInfinispanStackConfigFile;
    }

    public String getDeploymentConfig() {
        return deploymentConfig;
    }

    public void setDeploymentConfig(String deploymentConfig) {
        this.deploymentConfig = deploymentConfig;
    }

    public boolean isKubernetes() {
        return kubernetes;
    }

    public void setKubernetes(boolean kubernetes) {
        this.kubernetes = kubernetes;
    }

    public Integer getProcessingPlantReplicationCount() {
        return processingPlantReplicationCount;
    }

    public void setProcessingPlantReplicationCount(Integer processingPlantReplicationCount) {
        this.processingPlantReplicationCount = processingPlantReplicationCount;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public boolean isUsingInternalEncryption() {
        return usingInternalEncryption;
    }

    public void setUsingInternalEncryption(boolean usingInternalEncryption) {
        this.usingInternalEncryption = usingInternalEncryption;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "DeploymentModeSegment{" +
                "kubernetes=" + kubernetes +
                ", processingPlantReplicationCount=" + processingPlantReplicationCount +
                ", concurrent=" + concurrent +
                ", usingInternalEncryption=" + usingInternalEncryption +
                ", deploymentConfig=" + deploymentConfig +
                ", petasosIPCStackConfigFile=" + petasosIPCStackConfigFile +
                ", petasosTopologyStackConfigFile=" + petasosTopologyStackConfigFile +
                ", petasosMetricsStackConfigFile=" + petasosMetricsStackConfigFile +
                ", petasosInterceptionStackConfigFile=" + petasosInterceptionStackConfigFile +
                ", petasosSubscriptionStackConfigFile=" + petasosSubscriptionStackConfigFile +
                ", petasosTaskingStackConfigFile=" + petasosTaskingStackConfigFile +
                ", multiuseInfinispanStackConfigFile=" + multiuseInfinispanStackConfigFile +
                ", petasosAuditStackConfigFile=" + petasosAuditStackConfigFile +
                '}';
    }
}
