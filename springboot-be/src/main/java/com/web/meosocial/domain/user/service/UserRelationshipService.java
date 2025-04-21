package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.UserRelationshipDto;
import org.springframework.stereotype.Service;

@Service
public interface UserRelationshipService {
    UserRelationshipDto followUser(Long followerId, UserRelationshipDto userRelationshipDto);

    UserRelationshipDto unfollowUser(Long followerId, UserRelationshipDto userRelationshipDto);

    UserRelationshipDto blockUser(Long followerId, UserRelationshipDto userRelationshipDto);

    UserRelationshipDto unblockUser(Long followerId, UserRelationshipDto userRelationshipDto);

    UserRelationshipDto getUserRelationshipById(Long id);

    Boolean IsUserRelaMutualFollow(Long followerId, Long followingId);

    Boolean IsUserFollow(Long followerId, Long followingId);

    Boolean IsUserBlocked(Long followerId, Long followingId);
}
