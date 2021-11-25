package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.repositories.ArtistRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/records")
public class RecordController {
    @Autowired
    private RecordRepository repository;
    @Autowired
    private ArtistRepository artistRepository;

    @GetMapping
    public List<Record> getAllRecords() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Record> addRecord(@RequestBody Record record) {
        return new ResponseEntity<>(repository.save(record), HttpStatus.CREATED);
    }


}
