package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.dto.UserRelationshipDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.model.UserRelationship;
import com.web.meosocial.domain.user.repository.UserRelationshipRepository;
import com.web.meosocial.domain.user.service.UserRelationshipService;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.util.UUID64Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {
    @Autowired
    private UserRelationshipRepository userRelaRepository;
    @Autowired
    private UserService userService;
    private final UUID64Generator uuid64Generator = new UUID64Generator();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRelationshipServiceImpl.class);

    /**
     * This method checks if the follower and following users are different, retrieves the users and their current relationships
     * from the database, and then determines the appropriate action based on the current relationship status between the two users.
     * If no existing relationship is found, a new relationship is created with the status "FOLLOW". If there is an existing "UNFOLLOW"
     * relationship, it updates the status to "FOLLOW".
     *
     * @param userRelationshipDto The DTO containing the follower and following user IDs.
     * @return A {@link UserRelationshipDto} containing the details of the newly created or updated relationship.
     * @throws IllegalArgumentException If the follower and following users are the same or if the action is invalid.
     */
    @Transactional
    @Override
    public UserRelationshipDto followUser(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            throw new IllegalArgumentException("Follower and Following cannot be the same user.");
        }
        User follower = userService.getUserById(userRelationshipDto.getFollowerId());
        User following = userService.getUserById(userRelationshipDto.getFollowingId());
        UserRelationship followerToFollowing = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship followingToFollower = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        if (followerToFollowing == null) {
            return handleNoExistingFollowingToFollower(follower, following, followingToFollower);
        }
        return switch (Enums.RelationshipStatus.fromValue(followerToFollowing.getStatus())) {
            case UNFOLLOW -> handleExistingFollowingToFollower(followerToFollowing, followingToFollower);
            case FOLLOW ->
                    throw new IllegalArgumentException("User id: " + userRelationshipDto.getFollowerId() + " followed user id: " + userRelationshipDto.getFollowingId());
            case BLOCKED ->
                    throw new IllegalArgumentException("User id: " + userRelationshipDto.getFollowerId() + " is blocked user id: " + userRelationshipDto.getFollowingId());
        };
    }

    /**
     * Handles the scenario when there is no existing relationship between the follower and following users.
     * Creates a new relationship based on their statuses.
     */
    private UserRelationshipDto handleNoExistingFollowingToFollower(User follower, User following, UserRelationship followingToFollower) {
        if (followingToFollower == null || followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            return createNewRelationship(follower, following, Enums.RelationshipStatus.FOLLOW.getValue(), false);
        }
        switch (Enums.RelationshipStatus.fromValue(followingToFollower.getStatus())) {
            case FOLLOW:
                UserRelationshipDto newRelationshipDto = createNewRelationship(follower, following,
                        Enums.RelationshipStatus.FOLLOW.getValue(), true);
                updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), true);
                return newRelationshipDto;
            case BLOCKED:
                throw new IllegalArgumentException("Follower user blocked by following user.");
            default:
                throw new IllegalStateException("Unexpected relationship status.");
        }
    }

    /**
     * Handles the scenario when an existing relationship is found between the follower and following users.
     * Updates the relationship accordingly.
     */
    private UserRelationshipDto handleExistingFollowingToFollower(UserRelationship followerToFollowing, UserRelationship followingToFollower) {
        if (followingToFollower == null || followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            updateRelationship(followerToFollowing, Enums.RelationshipStatus.FOLLOW.getValue(), false);
            return new UserRelationshipDto(followerToFollowing);
        }
        switch (Enums.RelationshipStatus.fromValue(followingToFollower.getStatus())) {
            case FOLLOW:
                updateRelationship(followerToFollowing, Enums.RelationshipStatus.FOLLOW.getValue(), true);
                updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), true);
                return new UserRelationshipDto(followerToFollowing);
            case BLOCKED:
                throw new IllegalArgumentException("Follower user blocked by following user.");
            default:
                throw new IllegalStateException("Unexpected relationship status.");
        }
    }

    /**
     * Unfollows a user by updating the relationship status to "UNFOLLOW".
     * Checks if the follower is the same as the following user, if the user is already following,
     * and handles the cases where the user is blocked or already unfollowed.
     *
     * @param userRelationshipDto The DTO containing follower and following user IDs.
     * @return A {@link UserRelationshipDto} with the updated relationship.
     * @throws IllegalArgumentException If the follower and following are the same, not following, or blocked.
     */
    @Transactional
    @Override
    public UserRelationshipDto unfollowUser(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            throw new IllegalArgumentException("Follower and Following cannot be the same user.");
        }
        UserRelationship followerToFollowing = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship followingToFollower = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        if (followerToFollowing == null || followerToFollowing.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            throw new IllegalArgumentException("User didn't follow user target.");
        }
        switch (Enums.RelationshipStatus.fromValue(followerToFollowing.getStatus())) {
            case FOLLOW:
                if (followingToFollower == null || followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
                    updateRelationship(followerToFollowing, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
                    return new UserRelationshipDto(followerToFollowing);
                } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
                    updateRelationship(followerToFollowing, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
                    updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), false);
                    return new UserRelationshipDto(followerToFollowing);
                } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
                    throw new IllegalArgumentException("Follower user blocked by following user.");
                }
            case BLOCKED:
                throw new IllegalArgumentException("User blocked user target.");
            default:
                throw new IllegalStateException("Unexpected relationship status.");
        }
    }

    /**
     * Blocks a user by updating or creating a relationship with the status "BLOCKED".
     * If the blocker and blocked have an existing "FOLLOW" or "UNFOLLOW" relationship, it updates the status.
     * If no relationship exists, it creates a new "BLOCKED" relationship.
     *
     * @param userRelationshipDto The DTO containing the blocker and blocked user IDs.
     * @return {@link UserRelationshipDto} The updated or newly created user relationship.
     * @throws IllegalArgumentException If the blocker and blocked IDs are the same,
     *                                  if the blocked user has already blocked the blocker,
     *                                  or if the relationship is already in a "BLOCKED" state.
     */
    @Transactional
    @Override
    public UserRelationshipDto blockUser(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            throw new IllegalArgumentException("Blocker and Blocked cannot be the same user.");
        }
        User follower = userService.getUserById(userRelationshipDto.getFollowerId());
        User following = userService.getUserById(userRelationshipDto.getFollowingId());
        UserRelationship blockerToBlocked = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship blockedToBlocker = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        // Check if the blocked user has already blocked the blocker
        if (blockedToBlocker != null && blockedToBlocker.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
            throw new IllegalArgumentException("Cannot block. User is already blocked by the target user.");
        }
        // If no existing relationship, create a new one with the status "BLOCKED"
        if (blockerToBlocked == null) {
            UserRelationshipDto newRelationshipDto = createNewRelationship(follower, following,
                    Enums.RelationshipStatus.BLOCKED.getValue(), false);
            // If the blocked user was following, update their relationship to "UNFOLLOW"
            if (blockedToBlocker != null && blockedToBlocker.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
                updateRelationship(blockedToBlocker, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
            }
            return newRelationshipDto;
        }
        switch (Enums.RelationshipStatus.fromValue(blockerToBlocked.getStatus())) {
            // If the current relationship is "UNFOLLOW" or "FOLLOW", update to "BLOCKED"
            case FOLLOW:
            case UNFOLLOW:
                updateRelationship(blockerToBlocked, Enums.RelationshipStatus.BLOCKED.getValue(), false);
                // If the blocked user was following, update their relationship to "UNFOLLOW"
                if (blockedToBlocker != null && blockedToBlocker.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
                    updateRelationship(blockedToBlocker, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
                }
                return new UserRelationshipDto(blockerToBlocked);
            // If the relationship is already "BLOCKED", no further action is required
            case BLOCKED:
                throw new IllegalArgumentException("Relationship is already in BLOCKED status.");
            default:
                throw new IllegalArgumentException("Unexpected relationship status.");
        }
    }

    /**
     * Unblocks a user by changing the relationship status from "BLOCKED" to "UNFOLLOW".
     * Checks if the users are the same, if a block relationship exists, and if the user is blocked by the other user.
     *
     * @param userRelationshipDto The DTO containing follower and following user IDs.
     * @return A {@link UserRelationshipDto} with the updated relationship.
     * @throws IllegalArgumentException If the users are the same, no block relationship exists, or the user is blocked.
     */
    @Transactional
    @Override
    public UserRelationshipDto unblockUser(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            throw new IllegalArgumentException("Follower and Following cannot be the same user.");
        }
        UserRelationship blockerToBlocked = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship blockedToBlocker = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        if (blockerToBlocked == null || !blockerToBlocked.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
            throw new IllegalArgumentException("No existing block relationship found to unblock.");
        } else {
            updateRelationship(blockerToBlocked, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
        }
        if (blockedToBlocker != null && blockedToBlocker.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
            throw new IllegalArgumentException("User blocked by following user.");
        }
        return new UserRelationshipDto(blockerToBlocked);
    }

    @Override
    public UserRelationshipDto getUserRelationshipById(Long id) {
        return null;
    }

    /**
     * Creates a new user relationship between the follower and following users.
     *
     * @param follower        the user initiating the relationship (follower).
     * @param following       the user being followed or blocked (following).
     * @param status          the status of the relationship (e.g., FOLLOW, BLOCKED, etc.).
     * @param hasMutualFollow whether the relationship is mutual (true if both users follow each other).
     * @return {@link UserRelationshipDto} of created UserRelationship object.
     */
    private UserRelationshipDto createNewRelationship(User follower, User following, Integer status, Boolean hasMutualFollow) {
        UserRelationship relationship = new UserRelationship();
        relationship.setId(uuid64Generator.generateUUID64());
        relationship.setFollower(follower);
        relationship.setFollowing(following.getId());
        relationship.setStatus(status);
        relationship.setHasMutualFollow(hasMutualFollow);
        relationship.setCreatedAt(LocalDateTime.now());
        userRelaRepository.save(relationship);
        LOGGER.info("Creating new relationship: followerId - {}, followingId - {}, status - {}, hasMutualFollow - {}",
                follower.getId(), following.getId(), status, hasMutualFollow);
        return new UserRelationshipDto(relationship);
    }

    /**
     * Updates an existing user relationship with the given status and mutual follow flag.
     *
     * @param userRelationshipExisted the existing UserRelationship object to be updated.
     * @param status                  the new status of the relationship (e.g., FOLLOW, BLOCKED, etc.).
     * @param hasMutualFollow         whether the relationship is now mutual (true if both users follow each other).
     */
    private void updateRelationship(UserRelationship userRelationshipExisted, Integer status, Boolean hasMutualFollow) {
        userRelationshipExisted.setStatus(status);
        userRelationshipExisted.setHasMutualFollow(hasMutualFollow);
        userRelationshipExisted.setUpdatedAt(LocalDateTime.now());
        LOGGER.info("Updating relationship: relationshipId - {}, status - {}, hasMutualFollow - {}",
                userRelationshipExisted.getId(), status, hasMutualFollow);
        userRelaRepository.save(userRelationshipExisted);
    }
}
