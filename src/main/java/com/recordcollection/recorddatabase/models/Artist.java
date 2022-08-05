package com.recordcollection.recorddatabase.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String artistName;
    private String artistNameFormatted;
    //refactor to create Members Entity
//    private String[] members;

    @OneToMany
    @JsonIgnoreProperties({"artist", "comments"})
    private Set<Record> records;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("artist")
    @OrderBy("name ASC")
    private Set<Member> members;

    public Artist() {

    }

    public Artist(String artistName, String artistNameFormatted) {
        this.artistName = artistName;
        this.artistNameFormatted = artistNameFormatted;
    }

    public Artist(String artistName, String artistNameFormatted, List<String> members) {
        this.artistName = artistName;
        this.artistNameFormatted = artistNameFormatted;
        this.members = formatMembers(members);
    }

    private Set<Member> formatMembers(List<String> members) {
        Set<Member> memberSet = new HashSet<>();

        for (String memberName : members) {
            memberSet.add(new Member(memberName, this));
        }

        return memberSet;
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

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public Set<Record> getRecords() {
        return records;
    }
}
