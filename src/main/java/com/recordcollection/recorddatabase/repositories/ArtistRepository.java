package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findAllByArtistName(String name, Sort sort);
}
