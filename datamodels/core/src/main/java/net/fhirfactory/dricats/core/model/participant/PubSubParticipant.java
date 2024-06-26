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
package net.fhirfactory.dricats.core.model.participant;

import java.io.Serializable;
import java.util.Objects;

@Deprecated
public class PubSubParticipant implements Serializable {
    private IntraSubsystemPubSubParticipant intraSubsystemParticipant;
    private PetasosParticipant interSubsystemParticipant;

    public PubSubParticipant() {
        super();
        this.intraSubsystemParticipant = null;
        this.interSubsystemParticipant = null;
    }

    public PubSubParticipant(IntraSubsystemPubSubParticipant intraSubsystemParticipant) {
        this.intraSubsystemParticipant = intraSubsystemParticipant;
        this.interSubsystemParticipant = null;
    }

    public IntraSubsystemPubSubParticipant getIntraSubsystemParticipant() {
        return intraSubsystemParticipant;
    }

    public void setIntraSubsystemParticipant(IntraSubsystemPubSubParticipant intraSubsystemParticipant) {
        this.intraSubsystemParticipant = intraSubsystemParticipant;
    }

    public PetasosParticipant getInterSubsystemParticipant() {
        return interSubsystemParticipant;
    }

    public void setInterSubsystemParticipant(PetasosParticipant interSubsystemParticipant) {
        this.interSubsystemParticipant = interSubsystemParticipant;
    }

    @Override
    public String toString() {
        return "PubSubParticipant{" +
                "intraSubsystemParticipant=" + intraSubsystemParticipant +
                ", interSubsystemParticipant=" + interSubsystemParticipant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PubSubParticipant)) return false;
        PubSubParticipant that = (PubSubParticipant) o;
        return Objects.equals(getIntraSubsystemParticipant(), that.getIntraSubsystemParticipant()) && Objects.equals(getInterSubsystemParticipant(), that.getInterSubsystemParticipant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIntraSubsystemParticipant(), getInterSubsystemParticipant());
    }
}
