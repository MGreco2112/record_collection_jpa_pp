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
    private DiscogsArtist[] artists;
    private Image[] images;
    private Track[] tracklist;
    private Long year;
    private String uri;

    public DiscogsRecord(Long id, String title, DiscogsArtist[] artists, Image[] images, Track[] tracklist, Long year, String uri) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.images = images;
        this.tracklist = tracklist;
        this.year = year;
        this.uri = uri;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Image {
        private String uri;

        public Image(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Track {
        private String title;

        public Track(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DiscogsArtist[] getArtists() {
        return artists;
    }

    public void setArtists(DiscogsArtist[] artists) {
        this.artists = artists;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public Track[] getTracklist() {
        return tracklist;
    }

    public void setTracklist(Track[] tracklist) {
        this.tracklist = tracklist;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
