package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query(value = "SELECT record_id FROM track WHERE title LIKE %:query% ORDER BY title ASC", nativeQuery = true)
    List<Long> getRecordIdsByTrackTitle(@Param("query") String query);
}
