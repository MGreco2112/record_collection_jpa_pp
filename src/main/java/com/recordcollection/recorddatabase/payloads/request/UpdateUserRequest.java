package com.recordcollection.recorddatabase.payloads.request;

import java.util.Set;

public class UpdateUserRequest {
    private String username;
    private String password;
    private String discogsToken;
    private String discogsSecret;
    private Set<String> roles;

    public UpdateUserRequest(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UpdateUserRequest(String username, String password, String discogsToken, String discogsSecret, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.discogsToken = discogsToken;
        this.discogsSecret = discogsSecret;
        this.roles = roles;
    }

    public UpdateUserRequest() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDiscogsToken() {
        return discogsToken;
    }

    public void setDiscogsToken(String discogsToken) {
        this.discogsToken = discogsToken;
    }

    public String getDiscogsSecret() {
        return discogsSecret;
    }

    public void setDiscogsSecret(String discogsSecret) {
        this.discogsSecret = discogsSecret;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
