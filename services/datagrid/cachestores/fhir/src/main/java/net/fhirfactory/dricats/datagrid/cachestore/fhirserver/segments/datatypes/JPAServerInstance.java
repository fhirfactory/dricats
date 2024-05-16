/*
 * Copyright (c) 2022 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.datagrid.cachestore.fhirserver.segments.datatypes;

import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class JPAServerInstance {
    private static final Logger LOG = LoggerFactory.getLogger(JPAServerInstance.class);

    private int segmentId;
    private Set<ResourceType> supportedResourceSet;
    private boolean concurrent;

    //
    // Constructor(s)
    //

    public JPAServerInstance() {
        segmentId = 0;
        supportedResourceSet = new HashSet<>();
        concurrent = false;
    }

    public JPAServerInstance(int segmentId, Set<ResourceType> supportedResourceSet, boolean concurrent) {
        this.segmentId = segmentId;
        this.supportedResourceSet = new HashSet<>();
        this.supportedResourceSet.addAll(supportedResourceSet);
        this.concurrent = concurrent;
    }

    //
    // Getters and Setters
    //

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public Set<ResourceType> getSupportedResourceSet() {
        return supportedResourceSet;
    }

    public void setSupportedResourceSet(Set<ResourceType> supportedResourceSet) {
        this.supportedResourceSet = supportedResourceSet;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    //
    // toString
    //

    @Override
    public String toString() {
        return "JPAServerInstance{" +
                "segmentId=" + segmentId +
                ", supportedResourceSet=" + supportedResourceSet +
                ", concurrent=" + concurrent +
                '}';
    }
}
