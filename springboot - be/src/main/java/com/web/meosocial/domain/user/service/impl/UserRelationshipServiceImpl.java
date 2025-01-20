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
     * Creates a new user relationship if it does not already exist.
     *
     * @param userRelationshipDto the DTO containing details of the user relationship to be created
     * @return the created UserRelationshipDto containing the details of the new relationship
     * @throws IllegalArgumentException if a relationship between the specified follower and following already exists
     */
    @Transactional
    @Override
    public UserRelationshipDto followUser(UserRelationshipDto userRelationshipDto) {
        LOGGER.info("Starting followUser process for followerId: {}. followingId; {}",
                userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            LOGGER.error("FollowerId and FollowingId are the same: {}", userRelationshipDto.getFollowerId());
            throw new IllegalArgumentException("Follower and Following cannot be the same user.");
        }
        User follower = userService.getUserById(userRelationshipDto.getFollowerId());
        User following = userService.getUserById(userRelationshipDto.getFollowingId());
        UserRelationship followerToFollowing = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship followingToFollower = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        if (followerToFollowing == null) {
            LOGGER.info("No existing follower-to-following relationship found. Creating new relationship.");
            return handleNoExistingFollowingToFollower(follower, following, followingToFollower);
        } else if (followerToFollowing.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            LOGGER.info("Existing follower-to-following relationship found with UNFOLLOW status. Updating relationship.");
            return handleExistingFollowingToFollower(followerToFollowing, followingToFollower);
        }
        LOGGER.warn("No valid action could be performed for followerId: {}, followingId: {}",
                userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        throw new IllegalArgumentException("User id: " + userRelationshipDto.getFollowerId() + " followed user id: " + userRelationshipDto.getFollowingId());
    }

    @Transactional
    @Override
    public UserRelationshipDto unfollowUser(UserRelationshipDto userRelationshipDto) {
        if (userRelationshipDto.getFollowerId().equals(userRelationshipDto.getFollowingId())) {
            throw new IllegalArgumentException("Follower and Following cannot be the same user.");
        }
        UserRelationship followerToFollowing = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowerId(), userRelationshipDto.getFollowingId());
        UserRelationship followingToFollower = userRelaRepository.getUserRelationship(userRelationshipDto.getFollowingId(), userRelationshipDto.getFollowerId());
        if (followerToFollowing == null && followingToFollower == null) {
            throw new IllegalArgumentException("Follower and Following don't have a relationship.");
        } else if (followerToFollowing != null && followerToFollowing.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
            if (followingToFollower == null ||
                    followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
                updateRelationship(followerToFollowing, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
                return new UserRelationshipDto(followerToFollowing);
            } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
                updateRelationship(followerToFollowing, Enums.RelationshipStatus.UNFOLLOW.getValue(), false);
                updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), false);
                return new UserRelationshipDto(followerToFollowing);
            } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
                throw new IllegalArgumentException("Follower user blocked by following user.");
            }
        } else {
            throw new IllegalArgumentException("Exception");
        }
        return null;
    }

    @Transactional
    @Override
    public UserRelationshipDto blockUser(UserRelationshipDto userRelationshipDto) {
        return null;
    }

    @Transactional
    @Override
    public UserRelationshipDto unblockUser(UserRelationshipDto userRelationshipDto) {
        return null;
    }

    @Override
    public UserRelationshipDto getUserRelationshipById(Long id) {
        return null;
    }

    private UserRelationship createNewRelationship(User follower, User following, Integer status, Boolean hasMutualFollow) {
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
        return relationship;
    }

    private void updateRelationship(UserRelationship userRelationshipExisted, Integer status, Boolean hasMutualFollow) {
        userRelationshipExisted.setStatus(status);
        userRelationshipExisted.setHasMutualFollow(hasMutualFollow);
        userRelationshipExisted.setUpdatedAt(LocalDateTime.now());
        LOGGER.info("Updating relationship: relationshipId - {}, status - {}, hasMutualFollow - {}",
                userRelationshipExisted.getId(), status, hasMutualFollow);
        userRelaRepository.save(userRelationshipExisted);
    }

    private UserRelationshipDto handleNoExistingFollowingToFollower(User follower, User following, UserRelationship followingToFollower) {
        if (followingToFollower == null || followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            UserRelationship relationship = createNewRelationship(follower, following, Enums.RelationshipStatus.FOLLOW.getValue(), false);
            return new UserRelationshipDto(relationship);
        } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
            UserRelationship relationship = createNewRelationship(follower, following, Enums.RelationshipStatus.FOLLOW.getValue(), true);
            updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), true);
            return new UserRelationshipDto(relationship);
        } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
            throw new IllegalArgumentException("Follower user blocked by following user.");
        }
        return null;
    }

    private UserRelationshipDto handleExistingFollowingToFollower(UserRelationship followerToFollowing, UserRelationship followingToFollower) {
        if (followingToFollower == null || followingToFollower.getStatus().equals(Enums.RelationshipStatus.UNFOLLOW.getValue())) {
            updateRelationship(followerToFollowing, Enums.RelationshipStatus.FOLLOW.getValue(), false);
            return new UserRelationshipDto(followerToFollowing);
        } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.FOLLOW.getValue())) {
            updateRelationship(followerToFollowing, Enums.RelationshipStatus.FOLLOW.getValue(), true);
            updateRelationship(followingToFollower, Enums.RelationshipStatus.FOLLOW.getValue(), true);
            return new UserRelationshipDto(followerToFollowing);
        } else if (followingToFollower.getStatus().equals(Enums.RelationshipStatus.BLOCKED.getValue())) {
            throw new IllegalArgumentException("Follower user blocked by following user.");
        }
        return null;
    }
}
