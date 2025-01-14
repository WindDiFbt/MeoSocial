package com.web.meosocial.domain.user;

import com.web.meosocial.dto.user.UserRelationshipDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "userrelationship")
public class UserRelationship {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private User follower;

    @Column(name = "following")
    private Long following;

    // Constructor to convert UserRelationshipDto to UserRelationship entity
    public UserRelationship(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto != null) {
            this.id = userRelationshipDto.getId();
            if (userRelationshipDto.getFollowerId() != null) {
                User follower = new User();
                follower.setId(userRelationshipDto.getFollowerId());
                this.follower = follower;
            }
            this.following = userRelationshipDto.getFollowing();
        }
    }
}