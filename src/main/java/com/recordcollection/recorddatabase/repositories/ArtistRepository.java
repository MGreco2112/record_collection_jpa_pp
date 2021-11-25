package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
