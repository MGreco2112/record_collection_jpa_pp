package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
