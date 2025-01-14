package com.web.meosocial.domain.post;

import com.web.meosocial.domain.user.User;
import com.web.meosocial.dto.post.LikeDto;
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

    public Like(LikeDto likeDto) {
        if (likeDto != null) {
            this.id = likeDto.getId();
            this.createdAt = likeDto.getCreatedAt();
            this.user = new User();
            this.user.setId(likeDto.getUserId());
            this.post = new Post();
            this.post.setId(likeDto.getPostId());
        }
    }
}