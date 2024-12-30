package com.web.meosocial.dto;

import com.web.meosocial.domain.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String interestedUser;
    private String userStatus;

    private Set<Long> commentIds;
    private Set<Long> likeIds;
    private Set<Long> notificationIds;
    private Set<Long> postIds;
    private Set<Long> savedPostIds;
    private Long userInfoId;
    private Set<Long> userRelationshipIds;
    private Set<Long> userRoleIds;

    public UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.avatarUrl = user.getAvatarUrl();
            this.interestedUser = user.getInterestedUser();
            this.userStatus = user.getUserStatus();
            this.commentIds = mapCommentsToIds(user.getComments());
            this.likeIds = mapLikesToIds(user.getLikes());
            this.notificationIds = mapNotificationsToIds(user.getNotifications());
            this.postIds = mapPostsToIds(user.getPosts());
            this.savedPostIds = mapSavedPostsToIds(user.getSavedposts());
            this.userInfoId = user.getUserinfo() != null ? user.getUserinfo().getId() : null;
            this.userRelationshipIds = mapUserRelationshipsToIds(user.getUserrelationships());
            this.userRoleIds = mapUserRolesToIds(user.getUserroles());
        }
    }

    private Set<Long> mapCommentsToIds(Set<Comment> comments) {
        return comments.stream().map(Comment::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapLikesToIds(Set<Like> likes) {
        return likes.stream().map(Like::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapNotificationsToIds(Set<Notification> notifications) {
        return notifications.stream().map(Notification::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapPostsToIds(Set<Post> posts) {
        return posts.stream().map(Post::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapSavedPostsToIds(Set<SavedPost> savedPosts) {
        return savedPosts.stream().map(SavedPost::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapUserRelationshipsToIds(Set<UserRelationship> userRelationships) {
        return userRelationships.stream().map(UserRelationship::getId).collect(Collectors.toSet());
    }

    private Set<Long> mapUserRolesToIds(Set<UserRole> userRoles) {
        return userRoles.stream().map(UserRole::getId).collect(Collectors.toSet());
    }
}
