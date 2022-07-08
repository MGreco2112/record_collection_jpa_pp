package com.recordcollection.recorddatabase.payloads.api.request;

public class DiscogsRecordUrlRequest {
    private String path;

    public DiscogsRecordUrlRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
