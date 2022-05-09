package com.recordcollection.recorddatabase.payloads.request;

public class ArtistAddRequest {
    private Long recordId;
    private Long artistId;

    public ArtistAddRequest() {
    }

    public ArtistAddRequest(Long recordId, Long artistId) {
        this.recordId = recordId;
        this.artistId = artistId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
}
