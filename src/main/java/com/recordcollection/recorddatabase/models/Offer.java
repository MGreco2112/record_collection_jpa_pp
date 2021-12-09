package com.recordcollection.recorddatabase.models;

import javax.persistence.*;

@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "record_id",
            referencedColumnName = "id"
    )
    private Record record;

    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id"
    )
    private Collector sender;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            referencedColumnName = "id"
    )
    private Collector receiver;

    private Integer offerPrice;

    public Offer() {
    }

    public Offer(Record record, Collector sender, Collector receiver, Integer offerPrice) {
        this.record = record;
        this.sender = sender;
        this.receiver = receiver;
        this.offerPrice = offerPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Collector getSender() {
        return sender;
    }

    public void setSender(Collector sender) {
        this.sender = sender;
    }

    public Collector getReceiver() {
        return receiver;
    }

    public void setReceiver(Collector receiver) {
        this.receiver = receiver;
    }

    public Integer getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Integer offerPrice) {
        this.offerPrice = offerPrice;
    }
}
