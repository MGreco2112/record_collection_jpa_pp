package com.recordcollection.recorddatabase.controllers;


import com.recordcollection.recorddatabase.models.*;
import com.recordcollection.recorddatabase.models.Record;
import com.recordcollection.recorddatabase.payloads.request.*;
import com.recordcollection.recorddatabase.payloads.response.EditArtistResponse;
import com.recordcollection.recorddatabase.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/records")
public class RecordController {
    @Autowired
    private RecordRepository repository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private CollectorRepository collectorRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Value("${Spring.datasource.driver-class-name}")
    private String myDriver;

    @Value("${Spring.datasource.url}")
    private String myUrl;

    @Value("${Spring.datasource.username}")
    private String username;

    @Value("${Spring.datasource.password}")
    private String password;


    @GetMapping
    public List<Record> getAllRecords() {
        return repository.getAllRecordsSorted();
    }

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return artistRepository.getAllArtistsSorted();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @GetMapping("/release/{name}")
    public ResponseEntity<Record> getRecordsByName(@PathVariable String name) {
        Record selRecord = repository.getRecordByNameFormatted(name);

        if (selRecord == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(selRecord);
    }

    @GetMapping("/artist/{name}")
    public ResponseEntity<Artist> getArtistsByName(@PathVariable String name) {
        Artist selArtist = artistRepository.findByArtistNameFormatted(name);

        if (selArtist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(selArtist);
    }

    @GetMapping("/artistToEdit/{id}")
    public ResponseEntity<EditArtistResponse> getEditArtistByName(@PathVariable Long id) {
        Artist selArtist = artistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<String> members = new ArrayList<>();

        for (Member name : selArtist.getMembers()) {
            members.add(name.getName());
        }

        EditArtistResponse newResponse = new EditArtistResponse(selArtist.getId(), selArtist.getArtistName(), members);

        return ResponseEntity.ok(newResponse);
    }

    @GetMapping("/artistByRecord/{record}")
    public Artist getArtistByRecord(@PathVariable Long record) {
        Record selRecord = repository.findById(record).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        return selRecord.getArtist();
    }

    @GetMapping("/recordsByArtist/{artistNameFormatted}")
    public ResponseEntity<List<Record>> recordsByArtist(@PathVariable String artistNameFormatted) {
        Artist selArtist = artistRepository.findByArtistNameFormatted(artistNameFormatted);

        if (selArtist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(repository.getAllRecordsByArtist(selArtist.getId()));
    }

    @GetMapping("/search/name/{query}")
    public List<Record> recordsByNameSearchQuery(@PathVariable String query) {
        return repository.getAllRecordsByNameQuery(query);
    }

    @GetMapping("/search/artist_name/{query}")
    public List<Artist> artistsByNameSearchQuery(@PathVariable String query) {
        return artistRepository.getArtistsByNameQuery(query);
    }

    //not yet working
    @PostMapping(path = "/recordExists", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<List<Record>> recordExistsByName(@RequestBody RecordExistsRequest request) {
        List<Record> comparableRecords = repository.checkRecordExists(request.getName());

        return ResponseEntity.ok(comparableRecords);
    }

    @GetMapping("/artistExists/{name}")
    public ResponseEntity<List<Artist>> artistExistsByName(@PathVariable String name) {
        List<Artist> artists = artistRepository.checkArtistExists(name);

        return ResponseEntity.ok(artists);
    }

    @PostMapping
    public ResponseEntity<Record> addRecord(@RequestBody NewRecordRequest record) {

        Record newRecord = repository.save(new Record(
                record.getName(),
                record.getName(),
                record.getReleaseUear(),
                record.getNumberOfTracks(),
                null,
                record.getImageLink()));

        newRecord.setNameFormatted(newRecord.getNameFormatted() + "_" + newRecord.getId());

        Set<Track> trackList = new LinkedHashSet<>();

        for (String track : record.getTracks()) {
            Track newTrack = new Track(track, newRecord);

            trackList.add(newTrack);
        }

        trackRepository.saveAll(trackList);

        newRecord.setTracks(trackList);

        return new ResponseEntity<>(repository.save(newRecord), HttpStatus.CREATED);
    }

    @PostMapping("/bulkAddRecords_Artists")
    public ResponseEntity<String> addBulkRecords(@RequestBody List<SaveDiscogsRecordRequest> records) {
        for (SaveDiscogsRecordRequest record : records) {
                Artist artist = artistRepository.getArtistByName(record.getArtist().getArtistName());

                Record newRecord = new Record(
                        record.getName(),
                        record.getNameFormatted(),
                        record.getReleaseYear(),
                        record.getNumberOfTracks(),
                        null,
                        record.getImageLink()
                );

                repository.save(newRecord);

                if (artist == null) {
                    List<String> membersList = new ArrayList<>();

                    for (Member member : record.getArtist().getMembers()) {
                        membersList.add(member.getName());
                    }

                    PostArtistRequest request = new PostArtistRequest(
                            record.getArtist().getArtistName(),
                            record.getArtist().getArtistNameFormatted(),
                            membersList
                    );

                    Artist newArtist = createNewArtist(request).getBody();

                    assert newArtist != null;
                    newArtist.setRecords(new HashSet<>(List.of(newRecord)));
                    newRecord.setArtist(newArtist);

                    Set<Track> trackList = new LinkedHashSet<>();

                    for (Track track : record.getTracks()) {
                        Track newTrack = new Track(track.getTitle(), newRecord);
                        trackList.add(newTrack);
                    }

                    trackRepository.saveAll(trackList);

                    newRecord.setTracks(trackList);

                    newRecord.setNameFormatted(newRecord.getName().replaceAll(" ", "_") + "_" + newRecord.getId());

                    repository.save(newRecord);

                    Artist updateNewArtist = artistRepository.save(newArtist);

                    updateNewArtist.setArtistNameFormatted(updateNewArtist.getArtistName() + updateNewArtist.getId());

                    artistRepository.save(updateNewArtist);
                } else {

                    Artist newArtist = artistRepository.save(artist);

                    Set<Track> trackList = new LinkedHashSet<>();

                    for (Track track : record.getTracks()) {
                        Track newTrack = new Track(track.getTitle(), newRecord);
                        trackList.add(newTrack);
                    }

                    trackRepository.saveAll(trackList);

                    newRecord.setTracks(trackList);

                    repository.save(newRecord);

                    newRecord.setNameFormatted(newRecord.getName().replaceAll(" ", "_") + "_" + newRecord.getId());

                    newRecord.setArtist(newArtist);

                    repository.save(newRecord);

                    newArtist.getRecords().add(newRecord);

                    artistRepository.save(newArtist);

                }
            }

        return ResponseEntity.ok("Saved All");
    }

    @PostMapping("/artist")
    public ResponseEntity<Artist> createNewArtist(@RequestBody PostArtistRequest artist) {

        Artist newArtist = artistRepository.save(new Artist(
                artist.getArtistName(),
                artist.getArtistNameFormatted()
        ));

        Set<Member> members = new HashSet<>();

        for (String member : artist.getMembers()) {
            Member newMember = new Member(member, newArtist);

            members.add(newMember);
        }

        memberRepository.saveAll(members);

        newArtist.setArtistNameFormatted(newArtist.getArtistName() + "_" + newArtist.getId());

        return new ResponseEntity<>(artistRepository.save(newArtist), HttpStatus.CREATED);
    }

    @PostMapping("/artistAdd")
    public ResponseEntity<Record> addArtistsToRecord(@RequestBody ArtistAddRequest info) {
        Optional<Record> selRecord = repository.findById(info.getRecordId());

        Artist selArtist = artistRepository.findById(info.getArtistId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (selRecord.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        selArtist.getRecords().add(selRecord.get());

        selRecord.get().setArtist(selArtist);

        artistRepository.save(selArtist);

        return new ResponseEntity<>(repository.save(selRecord.get()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> updateRecordById(@PathVariable Long id, @RequestBody Record update) {
        Record selRecord = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (update.getName() != null) {
            selRecord.setName(update.getName());

            selRecord.setNameFormatted(update.getName() + "_" + selRecord.getId());
        }
        if (update.getNameFormatted() != null) {
            selRecord.setNameFormatted(update.getNameFormatted() + "_" + selRecord.getId());
        }
        if (update.getReleaseYear() != null) {
            selRecord.setReleaseYear(update.getReleaseYear());
        }
        if (update.getNumberOfTracks() != null) {
            selRecord.setNumberOfTracks(update.getNumberOfTracks());
        }
        if (update.getTracks() != null) {
            selRecord.setTracks(update.getTracks());
            /*
            delete all current tracks from repo
            create new trackList LinkedHashSet with title and record from update
            save all in repo
             */
        }
        if (update.getArtist() != null) {
            selRecord.setArtist(update.getArtist());
        }
        if (update.getImageLink() != null) {
            selRecord.setImageLink(update.getImageLink());
        }

        return new ResponseEntity<>(repository.save(selRecord), HttpStatus.OK);
    }

    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> updateArtistById(@PathVariable Long id, @RequestBody UpdateArtistRequest updates) {
        Artist selArtist = artistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updates.getArtistName() != null) {
            selArtist.setArtistName(updates.getArtistName());

            selArtist.setArtistNameFormatted(updates.getArtistName() + "_" + selArtist.getId());
        }
        if (updates.getArtistNameFormatted() != null) {
            selArtist.setArtistNameFormatted(updates.getArtistNameFormatted() + "_" + selArtist.getId());
        }
        if (updates.getMembers() != null) {
            Set<Member> updatedMembers = new HashSet<>();

            for (String member : updates.getMembers()) {
                updatedMembers.add(new Member(member, selArtist));
            }

            memberRepository.saveAll(updatedMembers);

            memberRepository.deleteAll(selArtist.getMembers());

            selArtist.setMembers(updatedMembers);

        }
        if (updates.getRecords() != null) {
            selArtist.setRecords(updates.getRecords());
        }

        return ResponseEntity.ok(artistRepository.save(selArtist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecordById(@PathVariable Long id) {
        Optional<Record> selRecord = repository.findById(id);

        if (selRecord.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (selRecord.get().getArtist() != null) {

            Optional<Artist> selArtist = artistRepository.findById(selRecord.get().getArtist().getId());

            if (selArtist.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            selArtist.get().getRecords().remove(selRecord.get());

            selRecord.get().setArtist(null);
        }


        selRecord.get().getCollectors().clear();

        for (Collector collector : selRecord.get().getCollectors()) {
            collector.getRecords().remove(selRecord.get());

            collectorRepository.save(collector);
        }

        for (Comment comment : selRecord.get().getComments()) {
            comment.setRecord(null);

            selRecord.get().getComments().remove(comment);

            commentRepository.save(comment);

            commentRepository.delete(comment);
        }

        repository.save(selRecord.get());

        trackRepository.deleteAll(selRecord.get().getTracks());

        repository.delete(selRecord.get());

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Record> removeArtistsFromRecord(@PathVariable Long id) {
        Record selRecord = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        selRecord.setArtist(null);

        return new ResponseEntity<>(repository.save(selRecord), HttpStatus.OK);
    }

    @DeleteMapping("/removeArtists/{id}")
    public ResponseEntity<String> removeArtistFromRepo(@PathVariable Long id) {
        Optional<Artist> selArtist = artistRepository.findById(id);

        if (selArtist.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        for (Record record : selArtist.get().getRecords()) {
            record.setArtist(null);
            repository.save(record);
        }

        artistRepository.save(selArtist.get());

        memberRepository.deleteAll(selArtist.get().getMembers());

        artistRepository.delete(selArtist.get());

        return new ResponseEntity<>("Artist Deleted", HttpStatus.OK);
    }

    //method to add artist to collector via database connection

//    private void connectArtistToRecord(Long artistId, Long recordId) {
//        Artist selArtist = artistRepository.findById(artistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        Record selRecord = repository.findById(recordId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        try {
//            Connection conn = DriverManager.getConnection(myUrl, username, password);
//            Class.forName(myDriver);
//            String query = "INSERT INTO artist_records (artist_id, records_id) VALUES (?, ?)";
//            PreparedStatement statement = conn.prepareStatement(query);
//
//            statement.setString(1, Long.toString(selArtist.getId()));
//            statement.setString(2, Long.toString(selRecord.getId()));
//
//            statement.executeUpdate();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }


}
