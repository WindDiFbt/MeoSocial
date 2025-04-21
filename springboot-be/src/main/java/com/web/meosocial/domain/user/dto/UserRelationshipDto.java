package com.web.meosocial.domain.user.dto;

import com.web.meosocial.domain.user.model.UserRelationship;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserRelationshipDto {
    private Long id;
    private Long followerId;
    private Long followingId;
    private Integer status;
    private Boolean hasMutualFollow ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor to map from UserRelationship entity to UserRelationshipDto
    public UserRelationshipDto(UserRelationship userRelationship) {
        if (userRelationship != null) {
            this.id = userRelationship.getId();
            this.followerId = userRelationship.getFollower() != null ? userRelationship.getFollower().getId() : null;
            this.followingId = userRelationship.getFollowing();
            this.status = userRelationship.getStatus();
            this.hasMutualFollow  = userRelationship.getHasMutualFollow();
            this.createdAt = userRelationship.getCreatedAt();
            this.updatedAt = userRelationship.getUpdatedAt();
        }
    }
}
