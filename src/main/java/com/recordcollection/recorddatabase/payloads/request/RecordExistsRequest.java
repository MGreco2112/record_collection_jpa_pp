package com.recordcollection.recorddatabase.payloads.request;

public class RecordExistsRequest {
    private String name;

    public RecordExistsRequest() {
    }

    public RecordExistsRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
