package com.recordcollection.recorddatabase.payloads.response;

import java.util.LinkedList;

public class EditRecordResponse {
    private Long id;
    private String name;
    private String nameFormatted;
    private String releaseYear;
    private String numberOfTracks;
    private String imageLink;
    private LinkedList<String> tracks;

    public EditRecordResponse() {
    }

    public EditRecordResponse(Long id, String name, String nameFormatted, String releaseYear, String numberOfTracks, String imageLink, LinkedList<String> tracks) {
        this.id = id;
        this.name = name;
        this.nameFormatted = nameFormatted;
        this.releaseYear = releaseYear;
        this.numberOfTracks = numberOfTracks;
        this.imageLink = imageLink;
        this.tracks = tracks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
