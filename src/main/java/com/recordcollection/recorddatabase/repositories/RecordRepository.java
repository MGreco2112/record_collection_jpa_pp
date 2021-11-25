package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

}
