package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Message;
import com.recordcollection.recorddatabase.models.Reply;
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
import java.util.Optional;

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

    @GetMapping("/all/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(message);
    }

    @GetMapping("/all/sender/{id}")
    public List<Message> getMessagesBySenderId(@PathVariable Long id) {
        return repository.findAllBySender_id(id);
    }

    @GetMapping("/all/receiver/{id}")
    public List<Message> getMessagesByReceiverId(@PathVariable Long id) {
        return repository.findAllByReceiver_id(id);
    }

    @GetMapping("/current/sender/{id}")
    public ResponseEntity<List<Message>> getMessagesToCurrentCollector(@PathVariable Long id) {
        Optional<Collector> currentCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector sender = collectorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(repository.findAllBySender_idAndReceiver_id(sender.getId(), currentCollector.get().getId()));
    }

    @GetMapping("/current/receiver/{id}")
    public ResponseEntity<List<Message>> getMessagesFromCurrentCollector(@PathVariable Long id) {
        Optional<Collector> currentCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector receiver = collectorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(repository.findAllBySender_idAndReceiver_id(currentCollector.get().getId(), receiver.getId()));
    }

    @GetMapping("/current/all/{id}")
    public ResponseEntity<List<Message>> getAllMessagesIncludingCurrentCollector(@PathVariable Long id) {
        Optional<Collector> currentCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        List<Message> messages = repository.findAllBySender_id(id);
        List<Message> sentMessages = repository.findAllByReceiver_id(id);

        messages.addAll(sentMessages);

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/current/{id}")
    public ResponseEntity<Message> sendNewMessageToReceiver(@PathVariable Long id, Message message) {
        Optional<Collector> currentCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector receiver = collectorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Message newMessage = new Message(currentCollector.get(), receiver);

        return new ResponseEntity<>(repository.save(newMessage), HttpStatus.CREATED);
    }

    @PutMapping("/view/{id}")
    public ResponseEntity<Message> openMessage(@PathVariable Long id) {
        Optional<Collector> currentCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Message unreadMessage = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        unreadMessage.setWasSeen(true);

        return ResponseEntity.ok(repository.save(unreadMessage));
    }


    @PutMapping("/respond/{id}")
    public ResponseEntity<Message> respondToMessage(@PathVariable Long id, String responseText) {
        Message appendedMessage = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Collector loggedInCollector = collectorRepository.findByUser_id(userService.getCurrentUser().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        appendedMessage.getContent().add(new Reply(responseText, loggedInCollector));

        collectorRepository.save(loggedInCollector);

        return ResponseEntity.ok(repository.save(appendedMessage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        Message deletedMessage = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(deletedMessage);

        return ResponseEntity.ok("Deleted Message");
    }

}
