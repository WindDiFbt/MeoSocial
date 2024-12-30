package com.web.meosocial.domain;

import com.web.meosocial.dto.PostDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.web.meosocial.domain.User user;

    @Column(name = "content", length = 3000)
    private String content;

    @Column(name = "visibility_level")
    private Integer visibilityLevel;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "shared_post_id")
    private Long sharedPostId;

    @Column(name = "shared_by_user_id")
    private Long sharedByUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostMedia> postmedia = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<SavedPost> savedposts = new LinkedHashSet<>();

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
        }
    }
}