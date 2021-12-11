package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Offer;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.CommentRepository;
import com.recordcollection.recorddatabase.repositories.OfferRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    @Autowired
    OfferRepository offerRepository;

    @GetMapping
    public List<Collector> getAllCollectors() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collector> getCollectorById(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @GetMapping("/record/{id}")
    public List<Collector> getCollectorsByRecord(@PathVariable Long id) {
        return new ArrayList<>(repository.findAllByRecordsId(id, Sort.by("name")));
    }

    @GetMapping("/offers")
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @GetMapping("/sentOffers/{id}")
    public ResponseEntity<Set<Offer>> getOffersByCollectorId(@PathVariable Long id) {
        Collector selCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(selCollector.getSentOffers());
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

        Comment newComment = commentRepository.save(comment);

        newComment.setCollector(selCollector);

        newComment.setRecord(selRecord);

        selRecord.getComments().add(newComment);

        recordRepository.save(selRecord);

        return new ResponseEntity<>(repository.save(selCollector), HttpStatus.OK);
    }

    @PostMapping("/offers/{id}")
    public ResponseEntity<Collector> createNewOffer(@PathVariable Long id, @RequestBody Offer offer) {
        Collector selCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Collector recCollector = repository.findById(offer.getReceiver().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Offer newOffer = offerRepository.save(offer);

        selCollector.getSentOffers().add(newOffer);
        recCollector.getReceivedOffers().add(newOffer);

        repository.save(recCollector);

        return ResponseEntity.ok(repository.save(selCollector));
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
        if (update.getSentOffers() != null) {
            selCollector.setSentOffers(update.getSentOffers());
        }
        if (update.getReceivedOffers() != null) {
            selCollector.setReceivedOffers(update.getReceivedOffers());
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

        Comment selComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selCollector.getComments().remove(selComment);

        selRecord.getComments().remove(selComment);

        selComment.setCollector(null);
        selComment.setRecord(null);

        commentRepository.delete(selComment);

        repository.save(selCollector);
        recordRepository.save(selRecord);

        return new ResponseEntity<>("Comment Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/delete/offer/{id}")
    public ResponseEntity<String> deleteOfferById(@PathVariable Long id) {
        Offer selOffer = offerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selOffer.setRecord(null);
        selOffer.setSender(null);
        selOffer.setReceiver(null);

        offerRepository.delete(selOffer);

        return ResponseEntity.ok("Offer deleted");
    }
}
