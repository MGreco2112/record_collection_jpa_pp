package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Record;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> getAllRecordsByNameFormatted(String name, Sort sort);

    @Query(value = "SELECT * FROM record ORDER BY name_formatted ASC", nativeQuery = true)
    List<Record> getAllRecordsSorted();

    @Query(value = "SELECT * FROM record WHERE name_formatted = :name", nativeQuery = true)
    Record getRecordByNameFormatted(@Param("name") String name);

    @Query(value = "SELECT * FROM record WHERE name = :name", nativeQuery = true)
    Record getRecordByName(@Param("name") String name);

    @Query(value = "SELECT * FROM record WHERE artist_id = :id ORDER BY name ASC", nativeQuery = true)
    List<Record> getAllRecordsByArtist(@Param("id") Long id);

    @Query(value = "SELECT * FROM record WHERE name LIKE %:query% ORDER BY name ASC", nativeQuery = true)
    List<Record> getAllRecordsByNameQuery(@Param("query") String query);

    @Query(value = "SELECT * FROM record WHERE name = :name", nativeQuery = true)
    List<Record> checkRecordExists(@Param("name") String name);

    //Query to return Records featuring specified Track Name
    //SELECT * FROM record WHERE id IN (SELECT record_id FROM track WHERE title LIKE "%title%");

    @Query(value = "SELECT * FROM record WHERE id IN (:recordIds)", nativeQuery = true)
    List<Record> getRecordsByBulkIds(@Param("recordIds") List<Long> recordIds);
}
