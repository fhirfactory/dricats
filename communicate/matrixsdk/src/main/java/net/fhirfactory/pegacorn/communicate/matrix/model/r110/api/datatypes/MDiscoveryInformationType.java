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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MDiscoveryInformationType implements Serializable {
    private MHomeServerInformationType homeServer;
    private MIdentityServerInformationType identityServer;

    //
    // Gettes and Setters
    //

    @JsonProperty("m.homeserver")
    public MHomeServerInformationType getHomeServer() {
        return homeServer;
    }

    @JsonProperty("m.homeserver")
    public void setHomeServer(MHomeServerInformationType homeServer) {
        this.homeServer = homeServer;
    }

    @JsonProperty("m.identity_server")
    public MIdentityServerInformationType getIdentityServer() {
        return identityServer;
    }

    @JsonProperty("m.identity_server")
    public void setIdentityServer(MIdentityServerInformationType identityServer) {
        this.identityServer = identityServer;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "MDiscoverInformationType{" +
                "homeServer=" + homeServer +
                ", identityServer=" + identityServer +
                '}';
    }
}
