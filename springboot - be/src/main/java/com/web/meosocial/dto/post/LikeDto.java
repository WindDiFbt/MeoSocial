package com.web.meosocial.dto.post;

import com.web.meosocial.domain.post.Like;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LikeDto {
    private String id;
    private Long userId;
    private String postId;
    private LocalDateTime createdAt;

    // Constructor to map from Like entity to LikeDto
    public LikeDto(Like like) {
        if (like != null) {
            this.id = like.getId();
            this.userId = like.getUser().getId();
            this.postId = like.getPost().getId();
            this.createdAt = like.getCreatedAt();
        }
    }
}
