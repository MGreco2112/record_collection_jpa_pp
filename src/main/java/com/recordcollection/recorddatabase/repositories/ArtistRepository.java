package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByArtistNameFormatted(String name);

    @Query(value = "SELECT * FROM artist ORDER BY artist_name ASC", nativeQuery = true)
    List<Artist> getAllArtistsSorted();

    @Query(value = "SELECT * FROM artist WHERE artist_name LIKE %:query% ORDER BY artist_name ASC", nativeQuery = true)
    List<Artist> getArtistsByNameQuery(@Param("query") String query);

    @Query(value = "SELECT * FROM artist WHERE artist_name = :name", nativeQuery = true)
    List<Artist> checkArtistExists(@Param("name") String name);
}
