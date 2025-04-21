package com.web.meosocial.domain.user.model;

import com.web.meosocial.domain.user.dto.UserRelationshipDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Lob
    @Column(name = "status")
    private Integer status;

    @Column(name = "has_mutual_follow")
    private Boolean hasMutualFollow ;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor to convert UserRelationshipDto to UserRelationship entity
    public UserRelationship(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto != null) {
            this.id = userRelationshipDto.getId();
            if (userRelationshipDto.getFollowerId() != null) {
                User follower = new User();
                follower.setId(userRelationshipDto.getFollowerId());
                this.follower = follower;
            }
            this.following = userRelationshipDto.getFollowingId();
            this.status = userRelationshipDto.getStatus();
            this.hasMutualFollow = userRelationshipDto.getHasMutualFollow();
            this.createdAt = userRelationshipDto.getCreatedAt();
            this.updatedAt = userRelationshipDto.getUpdatedAt();
        }
    }
}