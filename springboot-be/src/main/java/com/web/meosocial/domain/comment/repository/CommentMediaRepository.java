package com.web.meosocial.domain.comment.repository;

import com.web.meosocial.domain.comment.model.CommentMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMediaRepository extends JpaRepository<CommentMedia, String> {
    @Query("SELECT cm FROM CommentMedia cm WHERE cm.comment.id = :commentId AND cm.isDelete = FALSE")
    List<CommentMedia> findAllByCommentId(@Param("commentId") String commentId);
}
