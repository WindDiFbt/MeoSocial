package com.web.meosocial.domain.comment.repository;

import com.web.meosocial.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.isDelete = FALSE")
    List<Comment> findCommentsExistByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.isDelete = FALSE")
    List<Comment> findCommentsExistByPostId(@Param("postId") String postId);
}
