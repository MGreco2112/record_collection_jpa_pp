package com.recordcollection.recorddatabase.models;

import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean wasSeen = false;
    private String content;

    @OneToOne
    private Collector sender;

    @OneToOne
    private Collector receiver;

    //TODO add date and time


    public Message() {
    }

    public Message(String content, Collector sender, Collector receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isWasSeen() {
        return wasSeen;
    }

    public void setWasSeen(boolean wasSeen) {
        this.wasSeen = wasSeen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
