package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.recordcollection.recorddatabase.models.auth.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Collector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JsonIgnoreProperties({"collectors", "comments"})
    @JoinTable(
            name = "collector_record",
            joinColumns = @JoinColumn(name = "collector_id"),
            inverseJoinColumns = @JoinColumn(name = "record_id")
    )
    private Set<Record> records;

    @OneToMany(mappedBy = "collector", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("collector")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Offer> sentOffers;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<Offer> receivedOffers;

    @OneToOne
    @JoinColumn(
            name = "users_id",
            referencedColumnName = "id"
    )
    @JsonIgnore
    private User user;



    public Collector() {

    }

    public Collector(String name) {
        this.name = name;
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

    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Offer> getSentOffers() {
        return sentOffers;
    }

    public void setSentOffers(Set<Offer> sentOffers) {
        this.sentOffers = sentOffers;
    }

    public Set<Offer> getReceivedOffers() {
        return receivedOffers;
    }

    public void setReceivedOffers(Set<Offer> receivedOffers) {
        this.receivedOffers = receivedOffers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
