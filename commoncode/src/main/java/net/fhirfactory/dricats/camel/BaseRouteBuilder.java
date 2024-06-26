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
package net.fhirfactory.dricats.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

/**
 * @author Jasen Schremmer
 */
public abstract class BaseRouteBuilder extends RouteBuilder {

    public BaseRouteBuilder() {
        super();
    }

    public BaseRouteBuilder(CamelContext context) {
        super(context);
    }

    /**
     * @param uri
     * @return the RouteBuilder.from(uri) with all exceptions logged but not handled
     */
    protected RouteDefinition fromWithStandardExceptionHandling(String uri) {
        RouteDefinition route = from(uri);
        route
                .onException(Exception.class)
                .log(LoggingLevel.ERROR, getClass().getName() + ":: Exception occurred ${exception.message}, Body=${body}")
                .handled(false)
                .end()
        ;
        return route;
    }
}
