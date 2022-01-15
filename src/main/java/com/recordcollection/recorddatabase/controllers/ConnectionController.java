package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.ConnectionRepository;
import com.recordcollection.recorddatabase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
