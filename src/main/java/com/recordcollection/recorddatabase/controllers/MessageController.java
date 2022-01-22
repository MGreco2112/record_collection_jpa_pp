package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Message;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.MessageRepository;
import com.recordcollection.recorddatabase.repositories.UserRepository;
import com.recordcollection.recorddatabase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(message);
    }

    @GetMapping("/sender/{id}")
    public List<Message> getMessagesBySenderId(@PathVariable Long id) {
        return repository.findAllBySender_id(id);
    }

    @GetMapping("/receiver/{id}")
    public List<Message> getMessagesByReceiverId(@PathVariable Long id) {
        return repository.findAllByReceiver_id(id);
    }
}
