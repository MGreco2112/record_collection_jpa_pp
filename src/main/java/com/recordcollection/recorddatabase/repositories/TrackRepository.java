package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
