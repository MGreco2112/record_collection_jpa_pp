package com.recordcollection.recorddatabase.payloads.request;

import com.recordcollection.recorddatabase.models.Record;

import java.util.List;
import java.util.Set;

public class UpdateArtistRequest {
    private String artistName;
    private String artistNameFormatted;
    private List<String> members;
    private Set<Record> records;

    public UpdateArtistRequest() {
    }

    public UpdateArtistRequest(String artistName, String artistNameFormatted, List<String> members) {
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

    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }
}
