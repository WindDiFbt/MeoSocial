package com.web.meosocial.dto.user;

import com.web.meosocial.domain.user.UserRelationship;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRelationshipDto {
    private Long id;
    private Long followerId;
    private Long following;

    // Constructor to map from UserRelationship entity to UserRelationshipDto
    public UserRelationshipDto(UserRelationship userRelationship) {
        if (userRelationship != null) {
            this.id = userRelationship.getId();
            this.followerId = userRelationship.getFollower() != null ? userRelationship.getFollower().getId() : null;
            this.following = userRelationship.getFollowing();
        }
    }
}
