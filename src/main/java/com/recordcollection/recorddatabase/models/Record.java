package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nameFormatted;
    private String releaseYear;
    private String numberOfTracks;
    private String[] tracks;
    private String imageLink;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @JsonIgnoreProperties("records")
    private Artist artist;

    @ManyToMany
    @JsonIncludeProperties({"id", "name"})
    @JoinTable(
            name = "collector_record",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "collector_id")
    )
    private Set<Collector> collectors;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"record"})
    private Set<Comment> comments;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Offer> offers;

    public Record() {

    }

    public Record(String name, String nameFormatted, String releaseYear, String numberOfTracks, String[] tracks, String imageLink) {
        this.name = name;
        this.nameFormatted = nameFormatted;
        this.releaseYear = releaseYear;
        this.numberOfTracks = numberOfTracks;
        this.tracks = tracks;
        this.imageLink = imageLink;
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

    public String[] getTracks() {
        return tracks;
    }

    public void setTracks(String[] tracks) {
        this.tracks = tracks;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Collector> getCollectors() {
        return collectors;
    }

    public void setCollectors(Set<Collector> collectors) {
        this.collectors = collectors;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
