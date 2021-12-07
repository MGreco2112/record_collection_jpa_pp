package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Record;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> getAllRecordsByNameFormatted(String name, Sort sort);
}
