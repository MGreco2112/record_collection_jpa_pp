package com.recordcollection.recorddatabase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/discogs")
public class DiscogsApiController {

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
}
