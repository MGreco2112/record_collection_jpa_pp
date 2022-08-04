package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.Artist;
import com.recordcollection.recorddatabase.models.Track;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.models.discogs.DiscogsArtist;
import com.recordcollection.recorddatabase.models.discogs.DiscogsRecord;
import com.recordcollection.recorddatabase.models.discogs.DiscogsSearchResults;
import com.recordcollection.recorddatabase.models.discogs.FullDiscogsArtist;
import com.recordcollection.recorddatabase.payloads.api.request.DiscogsRecordUrlRequest;
import com.recordcollection.recorddatabase.payloads.api.response.DiscogsArtistSearchResponse;
import com.recordcollection.recorddatabase.payloads.api.response.DiscogsRecordSearchResponse;
import com.recordcollection.recorddatabase.payloads.request.SaveDiscogsRecordRequest;
import com.recordcollection.recorddatabase.repositories.ArtistRepository;
import com.recordcollection.recorddatabase.repositories.CollectorRepository;
import com.recordcollection.recorddatabase.repositories.RecordRepository;
import com.recordcollection.recorddatabase.repositories.TrackRepository;
import com.recordcollection.recorddatabase.services.UserService;
import com.recordcollection.recorddatabase.models.Record;
import kong.unirest.PagedList;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/api/discogs")
public class DiscogsApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private CollectorRepository collectorRepository;
    @Autowired
    private TrackRepository trackRepository;

    @Value("${recorddatabase.app.testURL}")
    private String testURL;

    @Value("${recorddatabase.app.consumerKey}")
    private String consumerKey;

    @Value("${recorddatabase.app.consumerSecret}")
    private String consumerSecret;

    @Value("${recorddatabase.app.searchReleaseURL}")
    private String searchReleaseURL;

    @Value("${recorddatabase.app.searchArtistURL}")
    private String searchArtistURL;

    private final String recordSearchParams = "&type=master&per_page=20";

    private final String artistSearchParams = "&type=artist&per_page=20";

    @Value("${recorddatabase.app.artistInfoPageURL}")
    private String artistInfoPageURL;

    //Database Pull from Discogs
    /*
    start at a number
    Create List
    Call API for x times, incrementing ids starting at chosen number
    Save API response as formatted Object
    Save formatted Object into SQL Server
     */

    //Search flow
    /*
    Take query param from frontend
    Call API with param, using filters for Release Name or Artist
        Release Name format incoming list of Releases as Vinyl Hub Records and sent to Frontend
        Artist will format incoming list of Artists as Vinyl Hub Artists and sent to Frontend
    Return Lists as Response Entities
    */

    @GetMapping("/searchRecords/{recordName}")
    private ResponseEntity<List<DiscogsRecordSearchResponse>> callDiscogsRecordsByQuery(@PathVariable String recordName) {
        List<DiscogsRecordSearchResponse> records = new ArrayList<>();


        DiscogsSearchResults discogsResults = Unirest.get(searchReleaseURL + recordName + recordSearchParams)
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(DiscogsSearchResults.class)
                .getBody();

        for (DiscogsSearchResults.Result record : discogsResults.getResults()) {
            DiscogsRecordSearchResponse newRecord = new DiscogsRecordSearchResponse(record.getTitle(), record.getResource_url());

            records.add(newRecord);
        }

        if (records.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(records);
    }

    @GetMapping("/searchArtists/{artistName}")
    private ResponseEntity<List<DiscogsArtistSearchResponse>> callDiscogsArtistsByQuery(@PathVariable String artistName) {
        List<DiscogsArtistSearchResponse> artists = new ArrayList<>();

        DiscogsSearchResults discogsResults = Unirest.get(searchArtistURL + artistName + artistSearchParams)
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(DiscogsSearchResults.class)
                .getBody();

        for (DiscogsSearchResults.Result artist : discogsResults.getResults()) {
            DiscogsArtistSearchResponse newArtist = new DiscogsArtistSearchResponse(artist.getTitle(), artist.getResource_url(), artist.getCover_image());

            artists.add(newArtist);
        }

        if (artists.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(artists);
    }

    @GetMapping("/findArtist/{artistId}")
    public ResponseEntity<FullDiscogsArtist> locateArtistById(@PathVariable Long artistId) {
        /*
        send in artistId
        Call endpoint for artist page and format into FullDiscogsArtist
        Return in place of Artist
         */
        FullDiscogsArtist artist = Unirest.get(artistInfoPageURL + artistId)
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(FullDiscogsArtist.class)
                .getBody();

        return ResponseEntity.ok(artist);
    }

    @PostMapping("/saveDiscogsRecord")
    public ResponseEntity<Record> saveDiscogsRecord(@RequestBody SaveDiscogsRecordRequest discogsRecord) {
        Record checkRepository = recordRepository.getRecordByName(discogsRecord.getName());

        if (checkRepository != null) {

            //if discogsRecord.tracklist == checkRepository.tracklist
                //return checkRepository
            return new ResponseEntity<>(checkRepository, HttpStatus.OK);
        }

        Record newRecord = new Record(
                discogsRecord.getName(),
                discogsRecord.getNameFormatted(),
                discogsRecord.getReleaseYear(),
                discogsRecord.getNumberOfTracks(),
                null,
                discogsRecord.getImageLink()
        );

        newRecord.setArtist(discogsRecord.getArtist());

        artistRepository.save(discogsRecord.getArtist());

        Record savedRecord = recordRepository.save(newRecord);

        Set<Track> trackList = new LinkedHashSet<>();

        for (Track track : discogsRecord.getTracks()) {
            Track newTrack = new Track(track.getTitle(), savedRecord);

            trackList.add(newTrack);
        }

        trackRepository.saveAll(trackList);

        savedRecord.setTracks(trackList);

        return new ResponseEntity<>(recordRepository.save(savedRecord), HttpStatus.CREATED);
    }

    @PostMapping("/convertBulkRecords")
    private ResponseEntity<List<Record>> convertBulkDiscogsRecords(@RequestBody List<DiscogsRecordSearchResponse> records) {

        List<Record> formattedRecords = new ArrayList<>();

        for (DiscogsRecordSearchResponse response : records) {
            DiscogsRecord newRecord = Unirest.get(response.getResource_url())
                    .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                    .header("User-Agent", "TheVinylHub/v1.0")
                    .header("Accept", "application/vnd.discogs.v2.discogs+json")
                    .asObject(DiscogsRecord.class)
                    .getBody();

            formattedRecords.add(discogsToRecordConversion(newRecord));
        }


        return ResponseEntity.ok(formattedRecords);
    }

    @PostMapping("/convertRecord")
    private ResponseEntity<Record> conversionRoute(@RequestBody DiscogsRecordUrlRequest request) {
        DiscogsRecord record = Unirest.get(request.getDiscogsPath())
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(DiscogsRecord.class)
                .getBody();

        Record formattedRecord = discogsToRecordConversion(record);

        return ResponseEntity.ok(formattedRecord);
    }

    //format record method
    private Record discogsToRecordConversion(DiscogsRecord discogsRecord) {
        if (discogsRecord == null) {
            return null;
        }

        Record formattedRecord = new Record(
                discogsRecord.getTitle(),
                discogsRecord.getTitle().replace(" ", "_"),
                discogsRecord.getYear().toString(),
                null,
                null,
                discogsRecord.getImages()[0].getUri()
        );

        Set<Track> trackList = new LinkedHashSet<>();

        Long id = 1L;

        for (DiscogsRecord.Track track : discogsRecord.getTracklist()) {
            Track newTrack = new Track(id++, track.getTitle());
            trackList.add(newTrack);
        }

        formattedRecord.setNumberOfTracks(Integer.toString(trackList.size()));
        formattedRecord.setTracks(trackList);

        List<String> members = new ArrayList<>();

        FullDiscogsArtist discogsArtist = Unirest.get(artistInfoPageURL + discogsRecord.getArtists()[0].getId())
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(FullDiscogsArtist.class)
                .getBody();


        if (discogsArtist.getMembers() != null) {
            for (FullDiscogsArtist.Member member : discogsArtist.getMembers()) {
            members.add(member.getName());
            }
        }

        Artist artist = new Artist(
                discogsRecord.getArtists()[0].getName(),
                discogsRecord.getArtists()[0].getName().replace(" ", "_"),
                members
        );

        formattedRecord.setArtist(artist);

        artist.setRecords(new HashSet<>(List.of(formattedRecord)));

        return formattedRecord;
    }



    //Test routes

    @GetMapping("/test")
    public ResponseEntity<Record> testDiscogsAPI() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        DiscogsRecord response = Unirest.get(testURL)
                .header("Authorization", "Discogs key=\"" + consumerKey + "\", secret=\"" + consumerSecret + "\"")
                .header("User-Agent", "TheVinylHub/v1.0")
                .header("Accept", "application/vnd.discogs.v2.discogs+json")
                .asObject(DiscogsRecord.class)
                .getBody();

        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Record formattedRecord = discogsToRecordConversion(response);


        return ResponseEntity.ok(formattedRecord);
    }


}
