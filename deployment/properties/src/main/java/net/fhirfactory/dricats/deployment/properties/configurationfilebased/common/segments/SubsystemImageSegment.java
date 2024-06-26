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

import org.apache.commons.lang3.StringUtils;

public class SubsystemImageSegment {
    private String repository;
    private String imageName;
    private String imageVersion;
    private String pullPolicy;

    public SubsystemImageSegment() {
        setRepository(null);
        setImageName(null);
        setImageVersion(null);
        setPullPolicy(null);
    }

    public void mergeOverrides(SubsystemImageSegment overrides) {
        if (overrides.hasImageName()) {
            setImageName(overrides.getImageName());
        }
        if (overrides.hasRepository()) {
            setRepository(overrides.getRepository());
        }
        if (overrides.hasImageVersion()) {
            setImageVersion(overrides.imageVersion);
        }
        if (overrides.hasPullPolicy()) {
            setPullPolicy(overrides.getPullPolicy());
        }
    }

    public boolean hasRepository() {
        boolean has = !(StringUtils.isEmpty(this.repository));
        return (has);
    }

    public boolean hasImageName() {
        boolean has = !(StringUtils.isEmpty(this.imageName));
        return (has);
    }

    public boolean hasImageVersion() {
        boolean has = !(StringUtils.isEmpty(this.imageVersion));
        return (has);
    }

    public boolean hasPullPolicy() {
        boolean has = !(StringUtils.isEmpty(this.pullPolicy));
        return (has);
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public String getPullPolicy() {
        return pullPolicy;
    }

    public void setPullPolicy(String pullPolicy) {
        this.pullPolicy = pullPolicy;
    }

    @Override
    public String toString() {
        return "SubsystemImageSegment{" +
                "repository=" + repository +
                ", imageName=" + imageName +
                ", imageVersion=" + imageVersion +
                ", pullPolicy=" + pullPolicy +
                '}';
    }
}
