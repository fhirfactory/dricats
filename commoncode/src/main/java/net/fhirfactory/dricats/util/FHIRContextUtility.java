/*
 * Copyright (c) 2020 ACT Health
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
package net.fhirfactory.dricats.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class FHIRContextUtility {

    private FhirContext fhirContext;

    public FHIRContextUtility() {
        fhirContext = FhirContext.forR4();
    }

    /**
     * NOTE: the result is thread safe.
     *
     * @see {FhirContext}
     */
    public FhirContext getFhirContext() {
        return fhirContext;
    }

    /**
     * NOTE: the result is NOT thread safe.
     *
     * @see {IParser.newJsonParser()}
     */
    @Produces
    public IParser getJsonParser() {
        return getFhirContext().newJsonParser();
    }
}
