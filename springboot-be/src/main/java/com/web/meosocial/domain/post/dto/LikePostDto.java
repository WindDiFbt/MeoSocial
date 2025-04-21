package com.web.meosocial.domain.post.dto;

import com.web.meosocial.domain.post.model.Like;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LikePostDto {
    private String id;
    private Long userId;
    private String postId;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    // Constructor to map from Like entity to LikeDto
    public LikePostDto(Like like) {
        if (like != null) {
            this.id = like.getId();
            this.userId = like.getUser().getId();
            this.postId = like.getPost().getId();
            this.createdAt = like.getCreatedAt();
            this.isDeleted = like.getIsDeleted();
        }
    }
}
