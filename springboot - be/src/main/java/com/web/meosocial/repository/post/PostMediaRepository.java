package com.web.meosocial.repository.post;

import com.web.meosocial.domain.post.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, String> {
    @Query("SELECT p FROM PostMedia p WHERE p.post.id = :post_id AND p.isDelete = :isDeleted")
    List<PostMedia> findAllByPostId(@Param("post_id") String post_id, @Param("isDeleted") boolean isDeleted);
}
