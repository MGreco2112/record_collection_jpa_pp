package com.recordcollection.recorddatabase.payloads.request;

import java.util.List;

public class PostArtistRequest {
    private String artistName;
    private String artistNameFormatted;
    private List<String> members;

    public PostArtistRequest() {
    }

    public PostArtistRequest(String artistName, String artistNameFormatted, List<String> members) {
        this.artistName = artistName;
        this.artistNameFormatted = artistNameFormatted;
        this.members = members;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistNameFormatted() {
        return artistNameFormatted;
    }

    public void setArtistNameFormatted(String artistNameFormatted) {
        this.artistNameFormatted = artistNameFormatted;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
