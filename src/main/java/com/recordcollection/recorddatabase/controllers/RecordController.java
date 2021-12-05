package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Artist;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.repositories.ArtistRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @GetMapping("/artist/{name}")
    public List<Artist> getArtistsByName(@PathVariable String name) {
        return new ArrayList<>(artistRepository.findAllByArtistName(name, Sort.by("artistName")));
    }

    @PostMapping
    public ResponseEntity<Record> addRecord(@RequestBody Record record) {
        return new ResponseEntity<>(repository.save(record), HttpStatus.CREATED);
    }

    @PostMapping("/artist")
    public ResponseEntity<Artist> createNewArtist(@RequestBody Artist artist) {
        return new ResponseEntity<>(artistRepository.save(artist), HttpStatus.CREATED);
    }

    @PostMapping("/artist/{id}")
    public ResponseEntity<Record> addArtistsToRecord(@PathVariable Long id, @RequestBody Set<Artist> artists) {
        Optional<Record> selRecord = repository.findById(id);

        if (selRecord.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        selRecord.get().setArtists(artists);

        return new ResponseEntity<>(repository.save(selRecord.get()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> updateRecordById(@PathVariable Long id, @RequestBody Record update) {
        Record selRecord = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (update.getName() != null) {
            selRecord.setName(update.getName());
        }
        if (update.getReleaseYear() != null) {
            selRecord.setReleaseYear(update.getReleaseYear());
        }
        if (update.getNumberOfTracks() != null) {
            selRecord.setNumberOfTracks(update.getNumberOfTracks());
        }
        if (update.getTracks() != null) {
            selRecord.setTracks(update.getTracks());
        }
        if (update.getArtists() != null) {
            selRecord.setArtists(update.getArtists());
        }

        return new ResponseEntity<>(repository.save(selRecord), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecordById(@PathVariable Long id) {
        Optional<Record> selRecord = repository.findById(id);

        if (selRecord.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        repository.delete(selRecord.get());

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Record> removeArtistsFromRecord(@PathVariable Long id) {
        Record selRecord = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selRecord.setArtists(null);

        return new ResponseEntity<>(repository.save(selRecord), HttpStatus.OK);
    }

    @DeleteMapping("/removeArtists/{id}")
    public ResponseEntity<String> removeArtistFromRepo(@PathVariable Long id) {
        Optional<Artist> selArtist = artistRepository.findById(id);

        if (selArtist.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        artistRepository.delete(selArtist.get());

        return new ResponseEntity<>("Artist Deleted", HttpStatus.OK);
    }


}
