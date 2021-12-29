package com.recordcollection.recorddatabase.payloads.request;

import java.util.Set;

public class SignupRequest {
    private String username;
    private String password;
    private Set<String> roles;

    public SignupRequest(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
