package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.connections.Connection;
import com.recordcollection.recorddatabase.connections.EConnection;
import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.payloads.response.MessageResponse;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.ConnectionRepository;
import com.recordcollection.recorddatabase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/connections")
public class ConnectionController {
    @Autowired
    private ConnectionRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private CollectorRepository collectorRepository;

    @GetMapping("/friends")
    public ResponseEntity<?> getAllFriends() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid user"), HttpStatus.BAD_REQUEST);
        }

        Collector collector = collectorRepository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<Connection> rels = repository.findAllByOriginator_idAndType(collector.getId(), EConnection.ACCEPTED);

        Set<Connection> invRels = repository.findAllByRecipient_idAndType(collector.getId(), EConnection.ACCEPTED);

        rels.addAll(invRels);

        return new ResponseEntity<>(rels, HttpStatus.OK);
    }

    //todo add other friend routes

    @PostMapping("/add/{rId}")
    public ResponseEntity<MessageResponse> addRelationship(@PathVariable Long rId) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid user"), HttpStatus.BAD_REQUEST);
        }

        Collector originator = collectorRepository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Collector recipient = collectorRepository.findById(rId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<Connection> rel = repository.findAllByOriginator_idAndRecipient_id(originator.getId(), recipient.getId());

        if (rel.isPresent()) {
            return new ResponseEntity<>(new MessageResponse("Nice try, be patient"), HttpStatus.OK);
        }

        Optional<Connection> inverseRel = repository.findAllByOriginator_idAndRecipient_id(recipient.getId(), originator.getId());

        if (inverseRel.isPresent()) {
            switch (inverseRel.get().getType()) {
                case PENDING:
                    inverseRel.get().setType(EConnection.ACCEPTED);
                    repository.save(inverseRel.get());
                    return new ResponseEntity<>(new MessageResponse("Request Accepted"), HttpStatus.CREATED);
                case ACCEPTED:
                    return new ResponseEntity<>(new MessageResponse("Already Accepted"), HttpStatus.OK);
                case BLOCKED:
                    return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.CREATED);
                default:
                    return new ResponseEntity<>(new MessageResponse("SERVER_ERROR: DEFAULT RELATIONSHIP"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        repository.save(new Connection(originator, recipient, EConnection.PENDING));

        return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.CREATED);
    }

    @PostMapping("/block/{rId}")
    public ResponseEntity<MessageResponse> blockRecipient(@PathVariable Long rId) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid user"), HttpStatus.BAD_REQUEST);
        }

        Collector originator = collectorRepository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Collector recipient = collectorRepository.findById(rId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        Optional<Connection> rel = repository.findAllByOriginator_idAndRecipient_id(originator.getId(), recipient.getId());

        if (rel.isPresent()) {
            switch (rel.get().getType()) {
                case PENDING:
                case ACCEPTED:
                    rel.get().setType(EConnection.BLOCKED);
                    repository.save(rel.get());
                    return new ResponseEntity<>(new MessageResponse("Blocked"), HttpStatus.OK);
                case BLOCKED:
                    return new ResponseEntity<>(new MessageResponse("Blocked"), HttpStatus.OK);
                default:
                    return new ResponseEntity<>(new MessageResponse("SERVER_ERROR: INVALID RELATIONSHIP STATUS"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Optional<Connection> inverseRel = repository.findAllByOriginator_idAndRecipient_id(recipient.getId(), originator.getId());

        if (inverseRel.isPresent()) {
            switch (inverseRel.get().getType()) {
                case PENDING:
                case ACCEPTED:
                    inverseRel.get().setType(EConnection.BLOCKED);
                    repository.save(inverseRel.get());
                    return new ResponseEntity<>(new MessageResponse("Blocked"), HttpStatus.OK);
                case BLOCKED:
                    return new ResponseEntity<>(new MessageResponse("Blocked"), HttpStatus.OK);
                default:
                    return new ResponseEntity<>(new MessageResponse("SERVER_ERROR: INVALID RELATIONSHIP STATUS"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        try {
            repository.save(new Connection(originator, recipient, EConnection.BLOCKED));
        } catch (Exception e) {
            System.out.println("error " + e.getLocalizedMessage());
            return new ResponseEntity<>(new MessageResponse("SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new MessageResponse("User Blocked"), HttpStatus.CREATED);
    }


    @PutMapping("/approve/{id}")
    public ResponseEntity<MessageResponse> approveRelationship(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid user"), HttpStatus.BAD_REQUEST);
        }

        Collector recipient = collectorRepository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Connection rel = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (rel.getRecipient().getId() != recipient.getId()) {
            return new ResponseEntity<>(new MessageResponse("UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
        }

        if (rel.getType() == EConnection.PENDING) {
            rel.setType(EConnection.ACCEPTED);
            repository.save(rel);
        }

        return new ResponseEntity<>(new MessageResponse("Approved"), HttpStatus.OK);

    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<MessageResponse> removeRelationship(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(new MessageResponse("Invalid user"), HttpStatus.BAD_REQUEST);
        }

        Collector collector = collectorRepository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Connection rel = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (rel.getOriginator().getId() != collector.getId() && rel.getRecipient().getId() != collector.getId()) {
            return new ResponseEntity<>(new MessageResponse("UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
        }

        if (rel.getType() != EConnection.BLOCKED) {
            repository.delete(rel);
        }

        return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.OK);
    }


}
