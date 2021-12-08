package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
