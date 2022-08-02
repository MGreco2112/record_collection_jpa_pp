package com.recordcollection.recorddatabase.payloads.request;

public class NewRecordRequest {
    private String name;
    private String releaseUear;
    private String numberOfTracks;
    private String[] tracks;
    private String imageLink;

    public NewRecordRequest() {
    }

    public NewRecordRequest(String name, String releaseUear, String numberOfTracks, String[] tracks, String imageLink) {
        this.name = name;
        this.releaseUear = releaseUear;
        this.numberOfTracks = numberOfTracks;
        this.tracks = tracks;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseUear() {
        return releaseUear;
    }

    public void setReleaseUear(String releaseUear) {
        this.releaseUear = releaseUear;
    }

    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(String numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String[] getTracks() {
        return tracks;
    }

    public void setTracks(String[] tracks) {
        this.tracks = tracks;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
