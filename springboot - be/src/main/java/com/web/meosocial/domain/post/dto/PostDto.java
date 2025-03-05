package com.web.meosocial.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.meosocial.domain.post.model.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
    private String id;
    private String userId;
    private String userName;
    private String fullName;
    private String content;
    private Integer visibilityLevel;
    private Boolean isDelete;
    private Boolean isSharedPost;
    private String sharedPostId;
    private Boolean isSharedPostAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private List<PostMediaDto> media;

    // Constructor to map from Post entity to PostDto
    public PostDto(Post post) {
        if (post != null) {
            this.id = post.getId();
            this.userId = String.valueOf(post.getUser().getId());
            this.userName = post.getUser().getUserName();
            this.fullName = post.getUser().getUserinfo().getFullName();
            this.content = post.getContent();
            this.visibilityLevel = post.getVisibilityLevel();
            this.isDelete = post.getIsDelete();
            this.sharedPostId = post.getSharedPostId();
            this.isSharedPost = post.getIsSharedPost();
            this.isSharedPostAvailable = post.getIsSharedPostAvailable();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
            this.deletedAt = post.getDeletedAt();
            if (post.getPostmedia() != null) {
                this.media = post.getPostmedia().stream().map(PostMediaDto::new).collect(Collectors.toList());
            }
        }
    }
}
