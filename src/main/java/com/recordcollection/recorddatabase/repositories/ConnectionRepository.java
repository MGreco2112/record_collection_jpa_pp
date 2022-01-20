package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.connections.Connection;
import com.recordcollection.recorddatabase.connections.EConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findAllByOriginator_id(Long id);
    Set<Connection> findAllByRecipient_idAndType(Long id, EConnection type);
    Set<Connection> findAllByOriginator_idAndType(Long id, EConnection type);
    Optional<Connection> findAllByOriginator_idAndRecipient_id(long oId, Long rId);
}
