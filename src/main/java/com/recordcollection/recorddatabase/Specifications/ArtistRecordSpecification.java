package com.recordcollection.recorddatabase.Specifications;

import com.recordcollection.recorddatabase.models.Artist;
import com.recordcollection.recorddatabase.models.Record;
import org.springframework.cglib.core.Predicate;

import javax.persistence.criteria.Join;

public class ArtistRecordSpecification {

    public Specification<Artist> findRecordsByArtistWithTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            Join<Record, Artist> artistRecord = root.join("records");
            return (Predicate) criteriaBuilder.equal(artistRecord.get("name"), title);
        };
    }
}
