package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

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
}
