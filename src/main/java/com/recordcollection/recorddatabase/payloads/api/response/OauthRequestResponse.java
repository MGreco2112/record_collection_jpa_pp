package com.recordcollection.recorddatabase.payloads.api.response;


public class OauthRequestResponse {
    private String response;

    public OauthRequestResponse() {

    }

    public OauthRequestResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
