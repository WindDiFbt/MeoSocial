package com.web.meosocial.domain;

import com.web.meosocial.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "full_name", length = 127)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "interestedUser", length = 258)
    private String interestedUser;

    @Lob
    @Column(name = "user_status")
    private String userStatus;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "recipient")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SavedPost> savedposts = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    private UserInfo userinfo;

    @OneToMany(mappedBy = "follower")
    private Set<UserRelationship> userrelationships = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userroles = new LinkedHashSet<>();

    public User(UserDto userDto) {
        if (userDto != null) {
            this.id = userDto.getId();
            this.fullName = userDto.getFullName();
            this.email = userDto.getEmail();
            this.avatarUrl = userDto.getAvatarUrl();
            this.interestedUser = userDto.getInterestedUser();
            this.userStatus = userDto.getUserStatus();
        }
    }
}