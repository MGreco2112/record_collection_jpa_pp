package com.recordcollection.recorddatabase.payloads.request;

import java.util.LinkedList;

public class UpdateRecordRequest {
    private String name;
    private String nameFormatted;
    private String releaseYear;
    private String numberOfTracks;
    private String imageLink;
    private LinkedList<String> tracks;

    public UpdateRecordRequest() {
    }

    public UpdateRecordRequest(String name, String nameFormatted, String releaseYear, String numberOfTracks, String imageLink, LinkedList<String> tracks) {
        this.name = name;
        this.nameFormatted = nameFormatted;
        this.releaseYear = releaseYear;
        this.numberOfTracks = numberOfTracks;
        this.imageLink = imageLink;
        this.tracks = tracks;
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

    public LinkedList<String> getTracks() {
        return tracks;
    }

    public void setTracks(LinkedList<String> tracks) {
        this.tracks = tracks;
    }
}
