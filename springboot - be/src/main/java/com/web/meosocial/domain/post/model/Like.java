package com.web.meosocial.domain.post.model;

import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.post.dto.LikePostDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Like(LikePostDto likePostDto) {
        if (likePostDto != null) {
            this.id = likePostDto.getId();
            this.createdAt = likePostDto.getCreatedAt();
            this.user = new User();
            this.user.setId(likePostDto.getUserId());
            this.post = new Post();
            this.post.setId(likePostDto.getPostId());
        }
    }
}