package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.CommentRepository;
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
    @Autowired
    CommentRepository commentRepository;

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

    @PostMapping("/comment/{id}")
    public ResponseEntity<Collector> createNewComment(@PathVariable Long id, @RequestBody Comment comment) {
        Collector selCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (comment == null || comment.getRecord().getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Record selRecord = recordRepository.findById(comment.getRecord().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selRecord.getComments().add(comment);

        recordRepository.save(selRecord);

        comment.setCollector(selCollector);

        commentRepository.save(comment);

        return new ResponseEntity<>(repository.save(selCollector), HttpStatus.OK);
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
            selCollector.setRecords(update.getRecords());
            //if record not in repository, save to repo,
            //or do we only allow records in repo to be added?
        }
        if (update.getComments() != null) {
            selCollector.setComments(update.getComments());
        }

        return new ResponseEntity<>(repository.save(selCollector), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Collector delCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(delCollector);

        return new ResponseEntity<>("Collector Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/delete/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestBody Comment comment) {
        Collector selCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Record selRecord = recordRepository.findById(comment.getRecord().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selCollector.getComments().remove(comment);

        selRecord.getComments().remove(comment);

        comment.setCollector(null);
        comment.setRecord(null);

        commentRepository.delete(comment);

        repository.save(selCollector);
        recordRepository.save(selRecord);

        return new ResponseEntity<>("Comment Deleted", HttpStatus.OK);
    }
}
