package com.web.meosocial.domain.user.model;

import com.web.meosocial.auth.models.RefreshToken;
import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.notification.Notification;
import com.web.meosocial.domain.post.model.Like;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.SavedPost;
import com.web.meosocial.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", length = 45)
    private String userName;

    @Column(name = "password", length = 64)
    private String password;

    @Lob
    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToMany(mappedBy = "recipient")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<SavedPost> savedPosts;

    @OneToOne(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private UserInfo userinfo;

    @OneToMany(mappedBy = "follower")
    private List<UserRelationship> userRelationships;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userroles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    public User(UserDto userDto) {
        if (userDto != null) {
            this.id = userDto.getId();
            this.userName = userDto.getUserName();
            this.password = userDto.getPassword();
            this.userStatus = userDto.getUserStatus();
            this.createdAt = userDto.getCreatedAt();
            this.updatedAt = userDto.getUpdatedAt();
        }
    }
}