package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.models.discogs.DiscogsRecord;
import com.recordcollection.recorddatabase.services.UserService;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    Call API for x times, increnting ids starting at chosen number
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

        Date date = new Date();

        long timestamp = date.getTime();

        DiscogsRecord response = Unirest.get(testURL)
                .header("Authorization", "OAuth oauth_consumer_key=\"" + consumerKey +
                        "\",oauth_nonce=\"" + timestamp +
                        "\",oauth_token=\"" + currentUser.getDiscogsToken().substring(11) +
                        "\",oauth_signature=\"" + consumerSecret + "&" + currentUser.getDiscogsSecret().substring(18) +
                        "\",oauth_signature_method=\"PLAINTEXT\"" +
                        ",oauth_timestamp=\"" + timestamp + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .asObject(DiscogsRecord.class)
                .getBody();

        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok(response);
    }
}
