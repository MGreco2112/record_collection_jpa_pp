package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/collectors")
public class CollectorController {
    @Autowired
    CollectorRepository repository;
    @Autowired
    RecordRepository recordRepository;

    @GetMapping
    public List<Collector> getAllCollectors() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collector> getCollectorById(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @PostMapping
    public Collector createNewCollector(@RequestBody Collector collector) {
        return repository.save(collector);
    }

    @PostMapping("/record/{id}")
    public ResponseEntity<Collector> addNewRecordToCollector(@PathVariable Long id, @RequestBody Record record) {
        Optional<Collector> selCollector = repository.findById(id);

        if (selCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        recordRepository.save(record);

        selCollector.get().getRecords().add(record);

        return new ResponseEntity<>(repository.save(selCollector.get()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collector> updateCollectorById(@PathVariable Long id, @RequestBody Collector update) {
        Collector selCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (update.getName() != null) {
            selCollector.setName(update.getName());
        }
        if (update.getRecords() != null) {
            selCollector.getRecords().addAll(update.getRecords());
            //if record not in repository, save to repo,
            //or do we only allow records in repo to be added?
        }

        return new ResponseEntity<>(repository.save(selCollector), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Collector delCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(delCollector);

        return new ResponseEntity<>("Collector Deleted", HttpStatus.OK);
    }
}
