package com.recordcollection.recorddatabase.models.discogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private List<DiscogsArtist> artists;
    private List<Image> images;
    private String released;
    private String uri;


    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Image {
        private String uri;

        private Image(String uri) {
            this.uri = uri;
        }

        private String getUri() {
            return uri;
        }

        private void setUri(String uri) {
            this.uri = uri;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Track {
        private String title;

        private Track(String title) {
            this.title = title;
        }

        private String getTitle() {
            return title;
        }

        private void setTitle(String title) {
            this.title = title;
        }
    }
}
