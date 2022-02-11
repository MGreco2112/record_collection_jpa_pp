package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Offer;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.CommentRepository;
import com.recordcollection.recorddatabase.repositories.OfferRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import com.recordcollection.recorddatabase.services.UserService;
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
    @Autowired
    UserService userService;

    //TODO Connect User Service to methods that use ID to get Current Logged In Collector

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
        return new ArrayList<>(repository.findAllByRecords_id(id, Sort.by("name")));
    }

    @GetMapping("/offers")
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @GetMapping("/sentoffers")
    public ResponseEntity<Set<Offer>> getSentOffers() {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(currentCollector.get().getSentOffers());
    }

    @GetMapping("/receivedoffers")
    public ResponseEntity<Set<Offer>> getReceivedOffers() {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(currentCollector.get().getReceivedOffers());
    }

    @PostMapping
    public ResponseEntity<Collector> createNewCollector(@RequestBody Collector collector) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        collector.setUser(currentUser);

        return ResponseEntity.ok(repository.save(collector));
    }

    @PostMapping("/comment")
    public ResponseEntity<Collector> createNewComment(@RequestBody Comment comment) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }


        if (comment == null || comment.getRecord().getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Record selRecord = recordRepository.findById(comment.getRecord().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment newComment = commentRepository.save(comment);

        newComment.setCollector(currentCollector.get());

        newComment.setRecord(selRecord);

        selRecord.getComments().add(newComment);

        recordRepository.save(selRecord);

        return new ResponseEntity<>(repository.save(currentCollector.get()), HttpStatus.OK);
    }

    @PostMapping("/offers")
    public ResponseEntity<Collector> createNewOffer(@RequestBody Offer offer) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector recCollector = repository.findById(offer.getReceiver().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Offer newOffer = offerRepository.save(offer);

        currentCollector.get().getSentOffers().add(newOffer);
        recCollector.getReceivedOffers().add(newOffer);

        repository.save(recCollector);

        return ResponseEntity.ok(repository.save(currentCollector.get()));
    }

    @PostMapping("/record")
    public ResponseEntity<Collector> addNewRecordToCollector(@RequestBody Record record) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        recordRepository.save(record);

        currentCollector.get().getRecords().add(record);

        return new ResponseEntity<>(repository.save(currentCollector.get()), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Collector> updateCollectorById(@RequestBody Collector update) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (update.getName() != null) {
            currentCollector.get().setName(update.getName());
        }
        if (update.getRecords() != null) {
            currentCollector.get().setRecords(update.getRecords());
            //if record not in repository, save to repo,
            //or do we only allow records in repo to be added?
        }
        if (update.getComments() != null) {
            currentCollector.get().setComments(update.getComments());
        }
        if (update.getSentOffers() != null) {
            currentCollector.get().setSentOffers(update.getSentOffers());
        }
        if (update.getReceivedOffers() != null) {
            currentCollector.get().setReceivedOffers(update.getReceivedOffers());
        }

        return new ResponseEntity<>(repository.save(currentCollector.get()), HttpStatus.OK);
    }

    @PutMapping("/acceptoffer")
    public ResponseEntity<Collector> acceptOffer(@RequestBody Offer offer) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Offer selOffer = offerRepository.findById(offer.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Collector sentCollector = repository.findById(offer.getSender().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        currentCollector.get().getRecords().remove(selOffer.getRecord());
        sentCollector.getRecords().add(selOffer.getRecord());

        selOffer.setRecord(null);
        selOffer.setReceiver(null);
        selOffer.setSender(null);

        offerRepository.delete(selOffer);
        repository.save(sentCollector);

        return ResponseEntity.ok(repository.save(currentCollector.get()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Collector delCollector = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(delCollector);

        return new ResponseEntity<>("Collector Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/delete/comment")
    public ResponseEntity<String> deleteComment(@RequestBody Comment comment) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Record selRecord = recordRepository.findById(comment.getRecord().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment selComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        currentCollector.get().getComments().remove(selComment);

        selRecord.getComments().remove(selComment);

        selComment.setCollector(null);
        selComment.setRecord(null);

        commentRepository.delete(selComment);

        repository.save(currentCollector.get());
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
