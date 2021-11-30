package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userComment;

//    @ManyToOne
//    @JoinColumn(name = "record_id", referencedColumnName = "id")
//    @JsonIncludeProperties("name")
//    private Record record;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIncludeProperties("name")
    private Collector collector;

    public Comment() {
    }

    public Comment(String userComment, Record record, Collector collector) {
        this.userComment = userComment;
//        this.record = record;
        this.collector = collector;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

//    public Record getRecord() {
//        return record;
//    }
//
//    public void setRecord(Record record) {
//        this.record = record;
//    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }
}
