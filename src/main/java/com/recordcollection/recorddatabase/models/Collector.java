package com.recordcollection.recorddatabase.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Collector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
