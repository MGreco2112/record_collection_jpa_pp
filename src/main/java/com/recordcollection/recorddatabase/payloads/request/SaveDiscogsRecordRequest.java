package com.recordcollection.recorddatabase.payloads.request;

import com.recordcollection.recorddatabase.models.Artist;
import com.recordcollection.recorddatabase.models.Track;

import java.util.List;

public class SaveDiscogsRecordRequest {

    private String name;
    private String nameFormatted;
    private String releaseYear;
    private String numberOfTracks;
    private String imageLink;

    private List<Track> tracks;
    private Artist artist;

    public SaveDiscogsRecordRequest() {
    }

    public SaveDiscogsRecordRequest(String name, String nameFormatted, String releaseYear, String numberOfTracks, String imageLink, List<Track> tracks, Artist artist) {
        this.name = name;
        this.nameFormatted = nameFormatted;
        this.releaseYear = releaseYear;
        this.numberOfTracks = numberOfTracks;
        this.imageLink = imageLink;
        this.tracks = tracks;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFormatted() {
        return nameFormatted;
    }

    public void setNameFormatted(String nameFormatted) {
        this.nameFormatted = nameFormatted;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(String numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
