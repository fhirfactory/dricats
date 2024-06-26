package net.fhirfactory.dricats.core.model.restfulapi;

import java.util.HashMap;

public class RESTfulActionResponse {
    private String response;
    private HashMap<String, String> headerParameters;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public HashMap<String, String> getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(HashMap<String, String> headerParameters) {
        this.headerParameters = headerParameters;
    }
}
