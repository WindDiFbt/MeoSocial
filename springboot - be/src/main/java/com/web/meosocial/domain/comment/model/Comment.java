package com.web.meosocial.domain.comment.model;

import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.comment.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "content", length = 1023)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "parent_comment_id")
    private String parentCommentId;

    @OneToMany(mappedBy = "comment")
    private List<CommentMedia> commentmedia;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Constructor to convert CommentDto to Comment entity
    public Comment(CommentDto commentDto) {
        if (commentDto != null) {
            this.id = commentDto.getId();
            if (commentDto.getUserId() != null) {
                User user = new User();
                user.setId(commentDto.getUserId());
                this.user = user;
            }
            if (commentDto.getPostId() != null) {
                Post post = new Post();
                post.setId(commentDto.getPostId());
                this.post = post;
            }
            this.isDelete = commentDto.getIsDelete();
            this.content = commentDto.getContent();
            this.createdAt = commentDto.getCreatedAt();
            this.updatedAt = commentDto.getUpdatedAt();
            this.parentCommentId = commentDto.getParentCommentId();
        }
    }
}