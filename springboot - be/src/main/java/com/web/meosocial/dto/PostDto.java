package com.web.meosocial.dto;

import com.web.meosocial.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private Long userId;
    private String content;
    private String visibility;
    private Boolean isDelete;
    private Long sharedPostId;
    private Long sharedByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime sharedAt;

    // Constructor to map from Post entity to PostDto
    public PostDto(Post post) {
        if (post != null) {
            this.id = post.getId();
            this.userId = post.getUser() != null ? post.getUser().getId() : null;
            this.content = post.getContent();
            this.visibility = post.getVisibility();
            this.isDelete = post.getIsDelete();
            this.sharedPostId = post.getSharedPostId();
            this.sharedByUserId = post.getSharedByUserId();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
            this.sharedAt = post.getSharedAt();
        }
    }
}
