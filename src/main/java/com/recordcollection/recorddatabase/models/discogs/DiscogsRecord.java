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
    private List<Track> tracklist;
    private Long year;
    private String uri;

    public DiscogsRecord(Long id, String title, List<DiscogsArtist> artists, List<Image> images, List<Track> tracklist, Long year, String uri) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.images = images;
        this.tracklist = tracklist;
        this.year = year;
        this.uri = uri;
    }

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

    public List<DiscogsArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<DiscogsArtist> artists) {
        this.artists = artists;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Track> getTracklist() {
        return tracklist;
    }

    public void setTracklist(List<Track> tracklist) {
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
