package com.recordcollection.recorddatabase.payloads.request;

import com.recordcollection.recorddatabase.models.Comment;

import java.util.HashSet;
import java.util.Set;

public class EditCollectorRequest {
    private String name;
    private Set<String> records = new HashSet<>();
    private Set<String> comments = new HashSet<>();

    public EditCollectorRequest(String name) {
        this.name = name;
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

    public Set<String> getComments() {
        return comments;
    }

    public void setComments(Set<String> comments) {
        this.comments = comments;
    }
}
