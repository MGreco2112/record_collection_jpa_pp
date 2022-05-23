package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Collector;
import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Offer;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.models.auth.ERole;
import com.recordcollection.recorddatabase.models.auth.Role;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.payloads.request.EditCollectorRequest;
import com.recordcollection.recorddatabase.payloads.request.UpdateCollectorRequest;
import com.recordcollection.recorddatabase.payloads.request.UpdateUserRequest;
import com.recordcollection.recorddatabase.repositories.*;
import com.recordcollection.recorddatabase.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/collectors")
public class CollectorController {
    @Autowired
    private CollectorRepository repository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Value("${Spring.datasource.driver-class-name}")
    private String myDriver;

    @Value("${Spring.datasource.url}")
    private String myUrl;

    @Value("${Spring.datasource.username}")
    private String username;

    @Value("${Spring.datasource.password}")
    private String password;

    @Autowired
    private PasswordEncoder encoder;


    @GetMapping
    public List<Collector> getAllCollectors() {
        return repository.findAll();
    }

    @GetMapping("/currentCollector")
    public ResponseEntity<Collector> getLoggedInCollector() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector currentCollector = repository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(currentCollector);
    }

    @GetMapping("/formattedCollector")
    public ResponseEntity<EditCollectorRequest> editCurrentCollector() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collector currentCollector = repository.findByUser_id(currentUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        EditCollectorRequest request = new EditCollectorRequest(currentCollector.getName());

        for (Record record : currentCollector.getRecords()) {
            request.getRecords().add(record.getName());
        }

        for (Comment comment : currentCollector.getComments()) {
            request.getComments().add(comment);
        }

        return ResponseEntity.ok(request);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Collector> getCollectorByUsername(@PathVariable String username) {
        Optional<User> selUser = userRepository.findByUsername(username);

        if (selUser.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Optional<Collector> selCollector = repository.findByUser_id(selUser.get().getId());

        if (selCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(selCollector.get());
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

    @GetMapping("/user/{collectorId}")
    public ResponseEntity<User> getUserFromCollectorId(@PathVariable Long collectorId) {
        Collector selCollector = repository.findById(collectorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(selCollector.getUser());
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

        if (comment == null || comment.getRecord().getName() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Record selRecord = recordRepository.getRecordByName(comment.getRecord().getName());

        if (selRecord == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        for (Comment selComment : currentCollector.get().getComments()) {
            if (Objects.equals(selComment.getRecord().getId(), selRecord.getId())) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }

        currentCollector.get().getComments().add(comment);

        comment.setCollector(currentCollector.get());

        comment.setRecord(selRecord);

        selRecord.getComments().add(comment);

        repository.save(currentCollector.get());

        recordRepository.save(selRecord);

        commentRepository.save(comment);

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

    @PostMapping("/record/add")
    public ResponseEntity<String> addRecordToCollectorById(@RequestBody Record recordId) {
        User user = userService.getCurrentUser();

        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Optional<Collector> selCollector = repository.findByUser_id(user.getId());

        if (selCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Record selRecord = recordRepository.findById(recordId.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (selCollector.get().getRecords().contains(selRecord)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            Connection conn = DriverManager.getConnection(myUrl, username, password);
            Class.forName(myDriver);
            String query = "INSERT INTO collector_record (record_id, collector_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, Long.toString(selRecord.getId()));
            statement.setString(2, Long.toString(selCollector.get().getId()));

            statement.executeUpdate();

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(CollectorController.class);
            System.out.println(e.getMessage());

        }

        return ResponseEntity.ok("Record Added To Collector");
    }

    @PutMapping
    public ResponseEntity<Collector> updateCollectorById(@RequestBody UpdateCollectorRequest update) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (update.getName() != null) {
            currentCollector.get().setName(update.getName());
        }
        if (update.getRecords() != null) {
            Set<Record> newRecords = new HashSet<>();

            for (String record : update.getRecords()) {
                Record selRecord = recordRepository.getRecordByName(record);

                if (selRecord == null) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                newRecords.add(selRecord);
            }

            currentCollector.get().setRecords(newRecords);
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

    @PutMapping("/user")
    public ResponseEntity<User> updateCurrentUser(@RequestBody UpdateUserRequest updates) {
        User selUser = userService.getCurrentUser();

        if (selUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (updates.getUsername() != null) {
            selUser.setUsername(updates.getUsername());
        }

        if (updates.getPassword() != null && !Objects.equals(updates.getPassword(), "") && !Objects.equals(updates.getPassword(), selUser.getPassword())) {
            selUser.setPassword(encoder.encode(updates.getPassword()));
        }

        if (updates.getRoles() != null) {
            Set<Role> roles = new HashSet<>();

            updates.getRoles().forEach(role -> {
                switch(role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);

                        break;
                    case "artist":
                        Role artistRole = roleRepository.findByName(ERole.ROLE_ARTIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(artistRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });

            selUser.setRoles(roles);
        }

        return ResponseEntity.ok(userRepository.save(selUser));
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

    @DeleteMapping("/delete/record/{id}")
    public ResponseEntity<String> removeRecordFromCollector(@PathVariable Long id) {
        User user = userService.getCurrentUser();

        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Optional<Collector> selCollector = repository.findByUser_id(user.getId());

        if (selCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Record selRecord = recordRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selCollector.get().getRecords().remove(selRecord);
        selRecord.getCollectors().remove(selCollector.get());

        repository.save(selCollector.get());
        recordRepository.save(selRecord);

        return ResponseEntity.ok("Record Removed");
    }

    @DeleteMapping("/delete/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        Optional<Collector> currentCollector = repository.findByUser_id(userService.getCurrentUser().getId());

        if (currentCollector.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Record selRecord = recordRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment selComment = commentRepository.getCommentByCollectorAndRecordIds(currentCollector.get().getId(), selRecord.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
