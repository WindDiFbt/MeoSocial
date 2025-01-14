package com.web.meosocial.repository.post;

import com.web.meosocial.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT DISTINCT p FROM Post p JOIN PostMedia pm ON p.id = pm.post.id AND pm.isDeleted = FALSE WHERE p.isDelete = FALSE AND p.user.id = :userId")
    List<Post> findPostExistWithUserId(@Param("userId") Long userId);
}
