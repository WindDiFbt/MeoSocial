package com.web.meosocial.dto.post;

import com.web.meosocial.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostDto {
    private String id;
    private Long userId;
    private String content;
    private Integer visibilityLevel;
    private Boolean isDelete;
    private String sharedPostId;
    private Long sharedByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime sharedAt;
    private LocalDateTime deletedAt;

    private List<PostMediaDto> media;

    // Constructor to map from Post entity to PostDto
    public PostDto(Post post) {
        if (post != null) {
            this.id = post.getId();
            this.userId = post.getUser() != null ? post.getUser().getId() : null;
            this.content = post.getContent();
            this.visibilityLevel = post.getVisibilityLevel();
            this.isDelete = post.getIsDelete();
            this.sharedPostId = post.getSharedPostId();
            this.sharedByUserId = post.getSharedByUserId();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
            this.sharedAt = post.getSharedAt();
            this.deletedAt = post.getDeletedAt();
            if (post.getPostmedia() != null) {
                this.media = post.getPostmedia().stream().map(PostMediaDto::new).collect(Collectors.toList());
            }
        }
    }
}
