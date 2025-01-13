package com.web.meosocial.repository;

import com.web.meosocial.domain.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, String> {
    @Query("SELECT p FROM PostMedia p WHERE p.post.id = :post_id")
    List<PostMedia> findAllByPostId(@Param("post_id") String post_id);
}
