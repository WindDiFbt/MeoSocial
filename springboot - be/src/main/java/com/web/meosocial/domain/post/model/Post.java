package com.web.meosocial.domain.post.model;

import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.post.dto.PostDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", length = 3000)
    private String content;

    @Column(name = "visibility_level")
    private Integer visibilityLevel;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "shared_post_id")
    private String sharedPostId;

    @Column(name = "shared_by_user_id")
    private Long sharedByUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post")
    private List<PostMedia> postmedia;

    @OneToMany(mappedBy = "post")
    private List<SavedPost> savedposts;

    public Post(PostDto postDto) {
        if (postDto != null) {
            this.id = postDto.getId();
            this.content = postDto.getContent();
            this.visibilityLevel = postDto.getVisibilityLevel();
            this.isDelete = postDto.getIsDelete();
            this.sharedPostId = postDto.getSharedPostId();
            this.sharedByUserId = postDto.getSharedByUserId();
            this.createdAt = postDto.getCreatedAt();
            this.updatedAt = postDto.getUpdatedAt();
            this.sharedAt = postDto.getSharedAt();
            if (postDto.getUserId() != null) {
                this.user = new User();
                this.user.setId(postDto.getUserId());
            }
            this.deletedAt = postDto.getDeletedAt();
        }
    }
}