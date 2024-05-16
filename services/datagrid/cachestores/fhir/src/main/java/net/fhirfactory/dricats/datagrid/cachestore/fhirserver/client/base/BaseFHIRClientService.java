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
package net.fhirfactory.dricats.datagrid.cachestore.fhirserver.client.base;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.configuration.FHIRServerCacheStoreConfiguration;
import net.fhirfactory.dricats.internals.fhir.r4.resources.bundle.BundleContentHelper;
import net.fhirfactory.dricats.util.FHIRContextUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class BaseFHIRClientService {

    public static final String API_KEY_HEADER_NAME = "x-api-key";
    public static final String DEFAULT_API_KEY_PROPERTY_NAME = "HAPI_API_KEY";
    private String hostName;
    private Integer portNumber;
    private String contextPath;
    private Boolean requiresEncryption;
    private Integer portTimeout;
    private IGenericClient client;
    private IParser fhirParser;
    private boolean initialised;
    private BundleContentHelper bundleContentHelper;

    private FHIRContextUtility fhirContextUtility;

    //
    // Constructor(s)
    //

    public BaseFHIRClientService(FHIRContextUtility fhirContextUtility, BundleContentHelper bundleContentHelper,
                                 FHIRServerCacheStoreConfiguration configuration) {
        super();
        this.fhirContextUtility = fhirContextUtility;
        this.bundleContentHelper = bundleContentHelper;
        this.initialised = false;
        this.hostName = configuration.host();
        this.portNumber = configuration.port();
        this.contextPath = configuration.contextPath();
        this.requiresEncryption = configuration.requiresEncryption();
        this.portTimeout = configuration.portTimeout();
    }

    //
    // Post Construct
    //

    @PostConstruct
    public void initialise() {
        getLogger().info(".initialise(): Entry");
        if (initialised) {
            getLogger().info(".initialise(): Nothing to do, already initialised!");
        } else {
            getLogger().info(".initialise(): Initialising....Start");
            getLogger().info(".initialise(): [Create a FHIR Parser] Start");
            this.fhirParser = getFHIRContextUtility().getJsonParser().setPrettyPrint(true);
            getLogger().info(".initialise(): [Create a FHIR Parser] Finish");
            getLogger().info(".initialise(): [Create HTTP(S) Client] Start");
            initialiseHTTPClient(getHostName(), getPortNumber(), getContextPath(), getRequiresEncryption());
            getLogger().info(".initialise(): [Create HTTP(S) Client] Finish");
            this.initialised = true;
            getLogger().info(".initialise(): Initialising....Finish");
        }
        getLogger().info(".initialise(): Exit");
    }

    //
    // Getters (and Setters)
    //

    public String getHostName() {
        return hostName;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Boolean getRequiresEncryption() {
        return requiresEncryption;
    }

    public Integer getPortTimeout() {
        return portTimeout;
    }

    abstract protected Logger getLogger();

    abstract protected ResourceType specifyResourceType();

    protected BundleContentHelper getBundleContentHelper() {
        return (bundleContentHelper);
    }

    public IGenericClient getClient() {
        return (this.client);
    }

    protected FHIRContextUtility getFHIRContextUtility() {
        return (fhirContextUtility);
    }

    protected String getApiKeyPropertyName() {
        return DEFAULT_API_KEY_PROPERTY_NAME;
    }

    protected int getSocketTimeoutInSecs() {
        int socketTimeout = getPortTimeout();
        return socketTimeout;
    }

    protected IParser getFHIRParser() {
        return (this.fhirParser);
    }

    //
    // HTTP(S) Client Management
    //

    protected IGenericClient initialiseHTTPClient(String host, Integer port, String contextPath,
                                                  boolean useEncryption) {
        getLogger().info(".initialiseHTTPClient(): Entry, host->{}, port->{}, contextPath->{}, useEncryption->{}", host,
                port, contextPath, useEncryption);
        //
        // Some Defensive Checking
        if (StringUtils.isEmpty(host)) {
            getLogger().error(".initialiseHTTPClient(): Exit, host is null, returning -null-");
            return (null);
        }
        if (port == null) {
            getLogger().error(".initialiseHTTPClient(): Exit, port is null, returning -null-");
            return (null);
        }
        if (port < 0 || port > 65535) {
            getLogger().error(".initialiseHTTPClient(): Exit, port is not within range!");
            return (null);
        }
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = "/";
        }
        //
        // Build ServerBaseAddress
        StringBuilder serverBaseAddressBuilder = new StringBuilder();
        if (useEncryption) {
            serverBaseAddressBuilder.append("https://");
        } else {
            serverBaseAddressBuilder.append("http://");
        }
        serverBaseAddressBuilder.append(host);
        serverBaseAddressBuilder.append(":");
        serverBaseAddressBuilder.append(Integer.toString(port));
        serverBaseAddressBuilder.append(contextPath);
        String theServerBase = serverBaseAddressBuilder.toString();
        //
        // Building Client
        getLogger().debug(".initialiseHTTPClient(): Get the FHIRContext!");
        FhirContext contextR4 = fhirContextUtility.getFhirContext();
        getLogger().info(".initialiseHTTPClient(): Set the ValidationMode to -NEVER-");
        contextR4.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        contextR4.getRestfulClientFactory().setSocketTimeout(getSocketTimeoutInSecs() * 1000);
        getLogger().info(".initialiseHTTPClient(): Get the Client");
        client = contextR4.newRestfulGenericClient(theServerBase);
        if (useEncryption) {
            getLogger().info(".initialiseHTTPClient(): Grab the API Key from the Properties");
            String apiKey = getApiKeyPropertyName();
            // From
            // https://hapifhir.io/hapi-fhir/docs/interceptors/built_in_client_interceptors.html#misc-add-headers-to-request
            getLogger().info(".initialiseHTTPClient(): Create a new Interceptor");
            AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor();
            getLogger().info(".initialiseHTTPClient(): Add the API Key to the Interceptor");
            interceptor.addHeaderValue(API_KEY_HEADER_NAME, apiKey);
            getLogger().info(".initialiseHTTPClient(): Register the Interceptor with the Client");
            client.registerInterceptor(interceptor);
        }
        getLogger().info(".initialiseHTTPClient(): Exit, client created!");
        return client;
    }

    //
    // Resource Method(s)
    //

    //
    // Create Resource
    public MethodOutcome createResource(Resource resourceToCreate) {
        getLogger().debug(".createResource(): Entry, resourceToCreate->{}", resourceToCreate);
        MethodOutcome outcome = null;
        try {
            outcome = getClient().create().resource(resourceToCreate).prettyPrint().encodedJson().execute();
        } catch (Exception ex) {
            getLogger().error(".createResource(): Error creating resource, stack->{}",
                    ExceptionUtils.getStackTrace(ex));
            outcome = new MethodOutcome();
            outcome.setCreated(false);
        }
        getLogger().debug(".createResource(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    //
    // Update Resource
    public MethodOutcome updateResource(Resource resourceToUpdate) {
        getLogger().debug(".updateResource(): Entry, resourceToUpdate->{}", resourceToUpdate);
        MethodOutcome outcome = null;
        try {
            outcome = getClient().update().resource(resourceToUpdate).prettyPrint().encodedJson().execute();
        } catch (Exception ex) {
            getLogger().error(".updateResource(): Error updating resource, stack->{}",
                    ExceptionUtils.getStackTrace(ex));
            outcome = new MethodOutcome();
            outcome.setCreated(false);
        }
        getLogger().debug(".updateResource(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    //
    // Find Resource(s)

    /**
     * @param resourceReference
     * @return
     */
    public Resource findResourceByReference(Reference resourceReference) {
        CodeableConcept identifierType = resourceReference.getIdentifier().getType();
        Coding identifierCode = identifierType.getCodingFirstRep();
        String identifierCodeValue = identifierCode.getCode();
//        String identifierSystem = identifierCode.getSystem();

        Resource response = findResourceByIdentifier(resourceReference.getType(),
                resourceReference.getIdentifier().getSystem(), identifierCodeValue,
                resourceReference.getIdentifier().getValue());
        return (response);
    }

    /**
     * @param resourceType
     * @param identifier
     * @return
     */
    public Resource findResourceByIdentifier(ResourceType resourceType, Identifier identifier) {
        getLogger().info(".findResourceByIdentifier(): Entry, resourceType->{}, identifier->{}", resourceType,
                identifier);
        CodeableConcept identifierType = identifier.getType();
        Coding identifierCode = identifierType.getCodingFirstRep();
        String identifierCodeValue = identifierCode.getCode();
        String identifierSystem = identifierCode.getSystem();
        String identifierValue = identifier.getValue();

        Resource response = findResourceByIdentifier(resourceType.toString(), identifierSystem, identifierCodeValue,
                identifierValue);
        getLogger().info(".findResourceByIdentifier(): Exit, response->{}", response);
        return (response);
    }

    /**
     * @param resourceType
     * @param identifier
     * @return
     */
    public Resource findResourceByIdentifier(String resourceType, Identifier identifier) {
        getLogger().info(".findResourceByIdentifier(): Entry, resourceType->{}, identifier->{}", resourceType,
                identifier);
        String identifierValue = identifier.getValue();
        String identifierSystem = identifier.getSystem();
        String identifierCode = null;
        if (identifier.hasType()) {
            CodeableConcept identifierType = identifier.getType();
            Coding identifierTypeCode = identifierType.getCodingFirstRep();
            identifierCode = identifierTypeCode.getCode();
        }
        Resource response = findResourceByIdentifier(resourceType, identifierSystem, identifierCode, identifierValue);
        getLogger().info(".findResourceByIdentifier(): Exit, response->{}", response);
        return (response);
    }

    /**
     * @param resourceType
     * @param identifierSystem
     * @param identifierCode
     * @param identifierValue
     * @return
     */
    public Resource findResourceByIdentifier(ResourceType resourceType, String identifierSystem, String identifierCode,
                                             String identifierValue) {
        Resource response = findResourceByIdentifier(resourceType.toString(), identifierSystem, identifierCode,
                identifierValue);
        return (response);
    }

    /**
     * @param resourceType
     * @param identifierSystem
     * @param identifierCode
     * @param identifierValue
     * @return
     */
    public Resource findResourceByIdentifier(String resourceType, String identifierSystem, String identifierCode,
                                             String identifierValue) {
        getLogger().info(
                ".findResourceByIdentifier(): Entry, resourceType->{}, identfierSystem->{}, identifierCode->{}, identifierValue->{}",
                resourceType, identifierSystem, identifierCode, identifierValue);
        String urlEncodedString = null;
        boolean noSystem = StringUtils.isEmpty(identifierSystem);
        boolean noCode = StringUtils.isEmpty(identifierCode);
        if (noSystem && noCode) {
            String rawSearchString = identifierValue;
            urlEncodedString = "identifier=" + URLEncoder.encode(rawSearchString, StandardCharsets.UTF_8);
        }
        if (noCode && !noSystem) {
            String rawSearchString = identifierSystem + "|" + identifierValue;
            urlEncodedString = "identifier=" + URLEncoder.encode(rawSearchString, StandardCharsets.UTF_8);
        }
        if (!noCode && !noSystem) {
            String rawSearchString = identifierSystem + "|" + identifierCode + "|" + identifierValue;
            urlEncodedString = "identifier:of_type=" + URLEncoder.encode(rawSearchString, StandardCharsets.UTF_8);
        }
        Bundle response = null;
        if (StringUtils.isNotEmpty(urlEncodedString)) {
            String searchURL = resourceType + "?" + urlEncodedString;
            getLogger().debug(".findResourceByIdentifier(): URL --> {}", searchURL);
            response = getClient().search().byUrl(searchURL).returnBundle(Bundle.class).execute();
            IParser r4Parser = getFHIRParser().setPrettyPrint(true);
            if (getLogger().isInfoEnabled()) {
                if (response != null) {
                    getLogger().info(".findResourceByIdentifier(): Retrieved Bundle --> {}",
                            r4Parser.encodeResourceToString(response));
                }
            }
        }
        Resource resource = null;
        if (response != null) {
            resource = bundleContentHelper.extractFirstRepOfType(response, resourceType);
        }
        getLogger().info(".findResourceByIdentifier(): Retrieved Resource --> {}", resource);
        return (resource);
    }
}
