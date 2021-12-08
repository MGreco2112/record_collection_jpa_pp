package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Admin;
import com.recordcollection.recorddatabase.repositories.AdminRepository;
import com.recordcollection.recorddatabase.repositories.ArtistRepository;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminRepository repository;
    @Autowired
    CollectorRepository collectorRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping
    public List<Admin> getAllAdmins() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    public Admin createNewAdmin(@RequestBody Admin admin) {
        return repository.save(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdminById(@PathVariable Long id, @RequestBody Admin updates) {
        Optional<Admin> selAdmin = repository.findById(id);

        if (selAdmin.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (updates.getId() != null) {
            selAdmin.get().setId(updates.getId());
        }
        if (updates.getName() != null) {
            selAdmin.get().setName(updates.getName());
        }

        return ResponseEntity.ok(repository.save(selAdmin.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdminById(@PathVariable Long id) {
        Admin selAdmin = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(selAdmin);

        return new ResponseEntity<>("Admin deleted", HttpStatus.OK);
    }

}
