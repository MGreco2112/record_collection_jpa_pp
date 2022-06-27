package com.recordcollection.recorddatabase.payloads.api.response;

public class DiscogsArtistSearchResponse {
    private String title;
    private String resource_url;
    private String cover_image;

    public DiscogsArtistSearchResponse(String title, String resource_url, String cover_image) {
        this.title = title;
        this.resource_url = resource_url;
        this.cover_image = cover_image;
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

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }
}
