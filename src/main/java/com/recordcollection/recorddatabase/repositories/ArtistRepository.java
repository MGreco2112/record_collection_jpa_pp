package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByArtistNameFormatted(String name);

    @Query(value = "SELECT * FROM artist ORDER BY artist_name ASC", nativeQuery = true)
    List<Artist> getAllArtistsSorted();
}
