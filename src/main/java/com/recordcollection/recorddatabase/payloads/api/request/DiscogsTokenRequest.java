package com.recordcollection.recorddatabase.payloads.api.request;

public class DiscogsTokenRequest {
    private String token;
    private String secret;

    public DiscogsTokenRequest(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
