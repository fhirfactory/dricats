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
package net.fhirfactory.dricats.internals.communicate.exceptions;

/**
 * @author mhunter
 */
public class MajorTransformationException extends Exception {

    /**
     * Creates a new instance of <code>TransformError</code> without detail
     * message.
     */
    public MajorTransformationException() {
    }

    /**
     * Constructs an instance of <code>TransformError</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public MajorTransformationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>TransformError</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public MajorTransformationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
