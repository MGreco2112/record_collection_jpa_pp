package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
