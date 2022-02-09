package com.recordcollection.recorddatabase.models;



import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;

@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne()
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIncludeProperties({"name"})
    private Collector sender;

    public Reply() {
    }

    public Reply(String content, Collector sender) {
        this.content = content;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
