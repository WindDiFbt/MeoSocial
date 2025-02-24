package com.web.meosocial.domain.comment.repository;

import com.web.meosocial.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt ASC")
    List<Comment> findCommentsExistByUserIdOrderByCreatedAtAsc(@Param("userId") Long userId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<Comment> findParentsCommentsExistByPostIdOrderByCreatedAtAsc(@Param("postId") String postId);

    @Query("SELECT c FROM Comment c WHERE c.parentCommentId = :parentCommentId ORDER BY c.createdAt ASC")
    List<Comment> findChildrenCommentsExistByParentCommentIdOrderByCreatedAtAsc(@Param("parentCommentId") String parentCommentId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> findAllCommentsExistByPostId(@Param("postId") String postId);

    @Query("SELECT COUNT(c) > 0 FROM Comment c  WHERE c.id = :commentId AND c.post.id = :postId")
    boolean existsByCommentIdAndPostId(@Param("commentId") String commentId, @Param("postId") String postId);

}
