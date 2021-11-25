package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
}
