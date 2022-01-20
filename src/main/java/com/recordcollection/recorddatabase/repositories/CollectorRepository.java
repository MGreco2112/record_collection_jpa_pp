package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Collector;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
    List<Collector> findAllByRecords_id(Long recordId, Sort sort);
    Optional<Collector> findByUser_id(Long id);
}
