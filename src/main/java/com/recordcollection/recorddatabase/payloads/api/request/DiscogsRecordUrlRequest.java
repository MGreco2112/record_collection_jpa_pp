package com.recordcollection.recorddatabase.payloads.api.request;

public class DiscogsRecordUrlRequest {
    private String discogsPath;

    public DiscogsRecordUrlRequest() {

    }

    public DiscogsRecordUrlRequest(String discogsPath) {
        this.discogsPath = discogsPath;
    }

    public String getDiscogsPath() {
        return discogsPath;
    }

    public void setDiscogsPath(String discogsPath) {
        this.discogsPath = discogsPath;
    }
}
