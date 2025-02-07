package com.web.meosocial.domain.post.repository;

import com.web.meosocial.domain.post.model.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, String> {
    List<SavedPost> findByUserId(Long userId);

    @Query("SELECT sp FROM SavedPost sp WHERE sp.user.id = :userId AND sp.post.id = :postId")
    SavedPost findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") String postId);
}
