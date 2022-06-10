package com.recordcollection.recorddatabase.models.discogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsArtist {
    private class Artist {
        private String name;

        private Artist(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }
    }
}
