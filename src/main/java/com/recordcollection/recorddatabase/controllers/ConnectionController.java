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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

        Collector developer = collectorRepository.findByUser_Id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<Connection> rels = repository.findAllByOriginator_IdAndType(developer.getId(), EConnection.ACCEPTED);

        Set<Connection> invRels = repository.findAllByRecipient_IdAndType(developer.getId(), EConnection.ACCEPTED);

        rels.addAll(invRels);

        return new ResponseEntity<>(rels, HttpStatus.OK);
    }

    //todo add other friend routes

}
