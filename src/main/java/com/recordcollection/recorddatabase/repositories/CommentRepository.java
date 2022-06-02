package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment WHERE collector_id = :cId && record_id = :rId", nativeQuery = true)
    Optional<Comment> getCommentByCollectorAndRecordIds(@Param("cId") Long cId, @Param("rId") Long rId);

    @Query(value = "SELECT * FROM comment WHERE user_comment = :comment", nativeQuery = true)
    Optional<Comment> getCommentByUserComment(@Param("comment") String comment);
}
