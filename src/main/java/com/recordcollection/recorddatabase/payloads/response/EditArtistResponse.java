package com.recordcollection.recorddatabase.payloads.response;

import java.util.List;

public class EditArtistResponse {
    private Long id;
    private String artistName;
    private List<String> members;

    public EditArtistResponse(Long id, String artistName, List<String> members) {
        this.id = id;
        this.artistName = artistName;
        this.members = members;
    }

    public EditArtistResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
