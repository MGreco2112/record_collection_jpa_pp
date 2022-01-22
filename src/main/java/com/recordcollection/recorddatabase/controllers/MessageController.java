package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Message;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.MessageRepository;
import com.recordcollection.recorddatabase.repositories.UserRepository;
import com.recordcollection.recorddatabase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
