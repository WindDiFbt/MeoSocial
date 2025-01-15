package com.web.meosocial.domain.user.model;

import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.notification.Notification;
import com.web.meosocial.domain.post.model.Like;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.SavedPost;
import com.web.meosocial.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
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

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return "";
    }
}