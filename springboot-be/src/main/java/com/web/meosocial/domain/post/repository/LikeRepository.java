package com.web.meosocial.domain.post.repository;

import com.web.meosocial.domain.post.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    @Query("SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId")
    Like findByPostIdAndUserId(@Param("postId") String postId, @Param("userId") Long userId);
}
