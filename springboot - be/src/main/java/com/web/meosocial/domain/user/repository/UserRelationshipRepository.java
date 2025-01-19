package com.web.meosocial.domain.user.repository;

import com.web.meosocial.domain.user.model.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
    /**
     * Retrieves the relationship between two users based on their IDs.
     *
     * @param followerId  the ID of the user who is following
     * @param followingId the ID of the user being followed
     * @return the Relationship object if found, or null if no relationship exists.
     */
    @Query("SELECT ur FROM UserRelationship ur WHERE ur.follower.id = :followerId AND ur.following = :followingId")
    UserRelationship getUserRelationship(@Param("followerId") long followerId, @Param("followingId") Long followingId);
}
