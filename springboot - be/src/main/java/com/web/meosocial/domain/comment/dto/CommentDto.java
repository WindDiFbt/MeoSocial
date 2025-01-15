package com.web.meosocial.domain.comment.dto;

import com.web.meosocial.domain.comment.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private String id;
    private Long userId;
    private String postId;
    private Boolean isDelete;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentCommentId;
    private LocalDateTime deletedAt;

    // Constructor to map from Comment entity to CommentDto
    public CommentDto(Comment comment) {
        if (comment != null) {
            this.id = comment.getId();
            this.userId = comment.getUser() != null ? comment.getUser().getId() : null;
            this.postId = comment.getPost() != null ? comment.getPost().getId() : null;
            this.isDelete = comment.getIsDelete();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.parentCommentId = comment.getParentCommentId();
            this.deletedAt = comment.getDeletedAt();
        }
    }
}
