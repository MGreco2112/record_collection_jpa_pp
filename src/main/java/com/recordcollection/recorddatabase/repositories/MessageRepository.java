package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySender_id(Long id);
    List<Message> findAllByReceiver_id(Long id);
    List<Message> findAllBySender_idAndReceiver_id(Long sId, Long rId);
}
