package com.recordcollection.recorddatabase.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userComment;

    @OneToOne
    @JsonIncludeProperties("name")
    private Record record;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIncludeProperties("name")
    private Collector collector;



}
