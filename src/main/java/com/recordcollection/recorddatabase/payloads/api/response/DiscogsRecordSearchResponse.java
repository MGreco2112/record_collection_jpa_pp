package com.recordcollection.recorddatabase.payloads.api.response;

import java.util.ArrayList;
import java.util.List;

public class DiscogsRecordSearchResponse {
    private String title;
    private String artist;
    private String resource_url;

    public DiscogsRecordSearchResponse() {
    }

    public DiscogsRecordSearchResponse(String title, String resource_url) {
        String[] formattedTitle = title.split("-");

        this.artist = formattedTitle[0].trim();
        this.title = formattedTitle[1].trim();
        this.resource_url = resource_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }
}
