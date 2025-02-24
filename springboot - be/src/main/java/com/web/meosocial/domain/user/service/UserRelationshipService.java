package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.UserRelationshipDto;
import org.springframework.stereotype.Service;

@Service
public interface UserRelationshipService {
    UserRelationshipDto followUser(UserRelationshipDto userRelationshipDto);

    UserRelationshipDto unfollowUser(UserRelationshipDto userRelationshipDto);

    UserRelationshipDto blockUser(UserRelationshipDto userRelationshipDto);

    UserRelationshipDto unblockUser(UserRelationshipDto userRelationshipDto);

    UserRelationshipDto getUserRelationshipById(Long id);

    Boolean IsUserRelaMutualFollow(Long followerId, Long followingId);

    Boolean IsUserFollow(Long followerId, Long followingId);

    Boolean IsUserBlocked(Long followerId, Long followingId);
}
