package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Record;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> getAllRecordsByNameFormatted(String name, Sort sort);

    @Query(value = "SELECT * FROM record ORDER BY name ASC", nativeQuery = true)
    List<Record> getAllRecordsSorted();

    @Query(value = "SELECT * FROM record WHERE artist_id = :id ORDER BY name ASC", nativeQuery = true)
    List<Record> getAllRecordsByArtist(@Param("id") Long id);
}
