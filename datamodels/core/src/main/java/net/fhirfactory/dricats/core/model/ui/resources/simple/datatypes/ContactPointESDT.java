/*
 * Copyright (c) 2021 Mark A. Hunter
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
package net.fhirfactory.dricats.core.model.ui.resources.simple.datatypes;

import net.fhirfactory.dricats.core.model.ui.resources.simple.valuesets.ContactPointESDTTypeEnum;
import net.fhirfactory.dricats.core.model.ui.resources.simple.valuesets.ContactPointESDTUseEnum;

import java.util.Objects;

public class ContactPointESDT {
    private String name;
    private String value;
    private Integer rank;
    private ContactPointESDTTypeEnum type;
    private ContactPointESDTUseEnum use;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public ContactPointESDTTypeEnum getType() {
        return type;
    }

    public void setType(ContactPointESDTTypeEnum type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactPointESDTUseEnum getUse() {
        return use;
    }

    public void setUse(ContactPointESDTUseEnum use) {
        this.use = use;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactPointESDT)) return false;
        ContactPointESDT that = (ContactPointESDT) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(rank, that.rank) && type == that.type && use == that.use;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, rank, type, use);
    }

    @Override
    public String toString() {
        return "ContactPointESDT{" +
                "name=" + name +
                ", value=" + value +
                ", rank=" + rank +
                ", type=" + type +
                ", use=" + use +
                '}';
    }
}
