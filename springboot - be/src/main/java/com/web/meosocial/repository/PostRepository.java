package com.web.meosocial.repository;

import com.web.meosocial.domain.Post;
import com.web.meosocial.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserAndIsDelete(User user, Boolean isDelete);
}
