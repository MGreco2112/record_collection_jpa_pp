package com.recordcollection.recorddatabase.payloads.request;

import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Offer;

import java.util.Set;

public class UpdateCollectorRequest {
    private Long id;
    private String name;
    private Set<String> records;
    private Set<Comment> comments;
    private Set<Offer> sentOffers;
    private Set<Offer> receivedOffers;

    public UpdateCollectorRequest(Long id, String name, Set<String> records, Set<Comment> comments, Set<Offer> sentOffers, Set<Offer> receivedOffers) {
        this.id = id;
        this.name = name;
        this.records = records;
        this.comments = comments;
        this.sentOffers = sentOffers;
        this.receivedOffers = receivedOffers;
    }

    public UpdateCollectorRequest() {
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

    public Set<String> getRecords() {
        return records;
    }

    public void setRecords(Set<String> records) {
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
}
