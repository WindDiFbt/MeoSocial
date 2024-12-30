package com.web.meosocial.domain;

import com.web.meosocial.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", length = 45)
    private String userName;

    @Column(name = "password", length = 45)
    private String password;

    @Lob
    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "recipient")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SavedPost> savedPosts = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    private UserInfo userinfo;

    @OneToMany(mappedBy = "follower")
    private Set<UserRelationship> userRelationships = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

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