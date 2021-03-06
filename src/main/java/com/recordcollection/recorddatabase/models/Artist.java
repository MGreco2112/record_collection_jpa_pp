package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String artistName;
    private String artistNameFormatted;
    private String[] members;

    @OneToMany
    @JsonIgnoreProperties({"artist", "comments"})
    private Set<Record> records;

    public Artist() {

    }

    public Artist(String artistName, String artistNameFormatted, String[] members) {
        this.artistName = artistName;
        this.artistNameFormatted = artistNameFormatted;
        this.members = members;
    }

    public Artist(String artistName, String artistNameFormatted, List<String> members) {
        this.artistName = artistName;
        this.artistNameFormatted = artistNameFormatted;
        this.members = formatMembers(members);
    }

    private String[] formatMembers(List<String> members) {
        String[] formattedMembers = new String[members.size()];

        int index = 0;

        for (String member : members) {
            formattedMembers[index] = member;

            index++;
        }

        return formattedMembers;
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

    public String getArtistNameFormatted() {
        return artistNameFormatted;
    }

    public void setArtistNameFormatted(String artistNameFormatted) {
        this.artistNameFormatted = artistNameFormatted;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public Set<Record> getRecords() {
        return records;
    }
}
