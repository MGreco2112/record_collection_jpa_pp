package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
