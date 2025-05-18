package com.web.meosocial.domain.post.repository;

import com.web.meosocial.domain.post.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, String> {
    @Query("SELECT pm FROM PostMedia pm WHERE pm.post.id = :postId AND pm.isDelete = :isDeleted")
    List<PostMedia> findAllByPostId(@Param("postId") String postId, @Param("isDeleted") boolean isDeleted);

    @Modifying
    @Query("UPDATE PostMedia pm SET pm.isDelete = :isDelete, pm.deletedAt = :deletedAt WHERE pm.post.id = :postId AND pm.isDelete = false")
    void updateIsDeleteAndDeletedAtByPostId(@Param("postId") String postId, @Param("isDelete") boolean isDelete, @Param("deletedAt") LocalDateTime deletedAt);

    @Query("SELECT pm FROM PostMedia pm " +
            "JOIN pm.post p " +
            "WHERE pm.isDelete = false AND p.user.id = :userId")
    List<PostMedia> findAllNotDeletedByUserId(@Param("userId") Long userId);
}
