package com.recordcollection.recorddatabase.payloads.api.response;

public class DiscogsRecordSearchResponse {
    private String title;
    private String resource_url;

    public DiscogsRecordSearchResponse(String title, String resource_url) {
        this.title = title;
        this.resource_url = resource_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }
}
