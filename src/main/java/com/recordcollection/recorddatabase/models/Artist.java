package com.recordcollection.recorddatabase.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artistName;
    private List<String> members;
    @ManyToMany
    @JoinTable(
            name = "artist_record",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "record_id")
    )
    private Set<Artist> artists;

    public Artist() {

    }

    public Artist(String artistName, List<String> members) {
        this.artistName = artistName;
        this.members = members;
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

    public Set<Artist> getArtists() {
        return artists;
    }
}
