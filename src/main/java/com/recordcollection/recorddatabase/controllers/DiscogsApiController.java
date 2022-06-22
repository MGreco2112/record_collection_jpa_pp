package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Artist;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.models.discogs.DiscogsArtist;
import com.recordcollection.recorddatabase.models.discogs.DiscogsRecord;
import com.recordcollection.recorddatabase.services.UserService;
import com.recordcollection.recorddatabase.models.Record;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/discogs")
public class DiscogsApiController {

    @Autowired
    private UserService userService;

    @Value("${recorddatabase.app.testURL}")
    private String testURL;

    @Value("${recorddatabase.app.consumerKey}")
    private String consumerKey;

    @Value("${recorddatabase.app.consumerSecret}")
    private String consumerSecret;

    /*
    start at a number
    Create List
    Call API for x times, incrementing ids starting at chosen number
    Save API response as formatted Object
    Save formatted Object into SQL Server
     */

    @GetMapping("/callNewRecords")
    public ResponseEntity<String> getNewDiscogsListings() {



        return ResponseEntity.ok("");
    }

    @GetMapping("/test")
    public ResponseEntity<DiscogsRecord> testDiscogsAPI() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        DiscogsRecord response = Unirest.get(testURL)
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(DiscogsRecord.class)
                .getBody();

        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok(response);
    }

    @GetMapping("/frontendTest")
    public ResponseEntity<Record> testRecordCasting() {
        DiscogsRecord testRecord = testDiscogsAPI().getBody();

        if (testRecord == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        int tracklistCount = 0;

        List<String> tracklist = new ArrayList<>();

        for (DiscogsRecord.Track track : testRecord.getTracklist()) {
            tracklist.add(track.getTitle());

            tracklistCount++;
        }

        Record formattedRecord = new Record(
                testRecord.getTitle(),
                testRecord.getTitle().replace(" ", "_"),
                testRecord.getYear().toString(),
                Integer.toString(tracklistCount),
                tracklist,
                testRecord.getImages()[0].getUri()
                );

        List<String> members = new ArrayList<>();

        for (DiscogsArtist artist : testRecord.getArtists()) {
            members.add(artist.getName());
        }

        Artist artist = new Artist(
                testRecord.getArtists()[0].getName(),
                testRecord.getArtists()[0].getName().replace(" ", "_"),
                members);

        formattedRecord.setArtist(artist);

        return ResponseEntity.ok(formattedRecord);
    }
}
