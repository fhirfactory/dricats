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
package net.fhirfactory.dricats.core.model.datagrid.datatypes;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DatagridPersistenceServiceRegistrationType implements Serializable {
    private DataParcelTypeDescriptor resourceType;
    private Set<DatagridPersistenceServiceType> persistenceServices;

    //
    // Constructor(s)
    //

    public DatagridPersistenceServiceRegistrationType() {
        this.resourceType = null;
        persistenceServices = new HashSet<>();
    }

    //
    // Getters and Setters
    //

    public DataParcelTypeDescriptor getResourceType() {
        return resourceType;
    }

    public void setResourceType(DataParcelTypeDescriptor resourceType) {
        this.resourceType = resourceType;
    }

    public Set<DatagridPersistenceServiceType> getPersistenceServices() {
        return persistenceServices;
    }

    public void setPersistenceServices(Set<DatagridPersistenceServiceType> persistenceServices) {
        this.persistenceServices = persistenceServices;
    }

    //
    // Hashcode and Equals
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatagridPersistenceServiceRegistrationType that = (DatagridPersistenceServiceRegistrationType) o;
        return Objects.equals(getResourceType(), that.getResourceType()) && Objects.equals(getPersistenceServices(), that.getPersistenceServices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResourceType(), getPersistenceServices());
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "DatagridPersistenceServiceRegistrationType{" +
                "resourceType=" + resourceType +
                ", persistenceServices=" + persistenceServices +
                '}';
    }
}
