package com.web.meosocial.domain.post.repository;

import com.web.meosocial.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p WHERE p.isDelete = FALSE AND p.user.id = :userId")
    List<Post> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Post  p WHERE p.sharedPostId = :postId")
    List<Post> findAllBySharedPostId(@Param("postId") String postId);
}
