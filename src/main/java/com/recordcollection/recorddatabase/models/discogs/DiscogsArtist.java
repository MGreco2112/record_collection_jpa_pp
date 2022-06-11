package com.recordcollection.recorddatabase.models.discogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsArtist {

    private String name;

    public DiscogsArtist(String name) {
            this.name = name;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
        }

}
