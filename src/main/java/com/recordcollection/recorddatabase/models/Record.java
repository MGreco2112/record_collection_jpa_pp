package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String releaseYear;
    private String numberOfTracks;
    private String[] tracks;

//    @OneToMany
//    @JoinColumn(name = "collector_id", referencedColumnName = "id")
//    @JsonIncludeProperties({"name"})
//    private Collector collector;

    @ManyToMany
    @JsonIgnoreProperties("records")
    @JoinTable(
            name = "artists_records",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists;

    public Record() {

    }

    public Record(String name, String releaseYear, String numberOfTracks, String[] tracks) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.numberOfTracks = numberOfTracks;
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

    public String[] getTracks() {
        return tracks;
    }

    public void setTracks(String[] tracks) {
        this.tracks = tracks;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

//    public Collector getCollector() {
//        return collector;
//    }
//
//    public void setCollector(Collector collector) {
//        this.collector = collector;
//    }
}
