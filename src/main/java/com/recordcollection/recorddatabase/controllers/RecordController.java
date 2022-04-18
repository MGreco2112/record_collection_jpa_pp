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
        return repository.getAllRecordsSorted();
    }

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return artistRepository.getAllArtistsSorted();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @GetMapping("/release/{name}")
    public ResponseEntity<Record> getRecordsByName(@PathVariable String name) {
        Record selRecord = repository.getRecordByNameFormatted(name);

        if (selRecord == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(selRecord);
    }

    @GetMapping("/artist/{name}")
    public ResponseEntity<Artist> getArtistsByName(@PathVariable String name) {
        Artist selArtist = artistRepository.findByArtistNameFormatted(name);

        if (selArtist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(selArtist);
    }

    @GetMapping("/artistByRecord/{record}")
    public Artist getArtistByRecord(@PathVariable Long record) {
        Record selRecord = repository.findById(record).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        return selRecord.getArtist();
    }

    @GetMapping("/recordsByArtist/{artistNameFormatted}")
    public ResponseEntity<List<Record>> recordsByArtist(@PathVariable String artistNameFormatted) {
        Artist selArtist = artistRepository.findByArtistNameFormatted(artistNameFormatted);

        if (selArtist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(repository.getAllRecordsByArtist(selArtist.getId()));
    }

    @PostMapping
    public ResponseEntity<Record> addRecord(@RequestBody Record record) {
        return new ResponseEntity<>(repository.save(record), HttpStatus.CREATED);
    }

    @PostMapping("/artist")
    public ResponseEntity<Artist> createNewArtist(@RequestBody Artist artist) {
        return new ResponseEntity<>(artistRepository.save(artist), HttpStatus.CREATED);
    }

    @PostMapping("/artist/{recordId}&&{artistId}")
    public ResponseEntity<Record> addArtistsToRecord(@PathVariable Long recordId, @PathVariable Long artistId) {
        Optional<Record> selRecord = repository.findById(recordId);

        Artist selArtist = artistRepository.findById(artistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (selRecord.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        selArtist.getRecords().add(selRecord.get());

        selRecord.get().setArtist(selArtist);

        artistRepository.save(selArtist);

        return new ResponseEntity<>(repository.save(selRecord.get()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> updateRecordById(@PathVariable Long id, @RequestBody Record update) {
        Record selRecord = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (update.getName() != null) {
            selRecord.setName(update.getName());
        }
        if (update.getNameFormatted() != null) {
            selRecord.setNameFormatted(update.getNameFormatted());
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
        if (update.getArtist() != null) {
            selRecord.setArtist(update.getArtist());
        }
        if (update.getImageLink() != null) {
            selRecord.setImageLink(update.getImageLink());
        }

        return new ResponseEntity<>(repository.save(selRecord), HttpStatus.OK);
    }

    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> updateArtistById(@PathVariable Long id, @RequestBody Artist updates) {
        Artist selArtist = artistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updates.getArtistName() != null) {
            selArtist.setArtistName(updates.getArtistName());
        }
        if (updates.getArtistNameFormatted() != null) {
            selArtist.setArtistNameFormatted(updates.getArtistNameFormatted());
        }
        if (updates.getMembers() != null) {
            selArtist.setMembers(updates.getMembers());
        }
        if (updates.getRecords() != null) {
            selArtist.setRecords(updates.getRecords());
        }

        return ResponseEntity.ok(artistRepository.save(selArtist));
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

        selRecord.setArtist(null);

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
