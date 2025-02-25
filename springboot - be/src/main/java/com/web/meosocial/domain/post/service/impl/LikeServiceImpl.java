package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.post.dto.LikePostDto;
import com.web.meosocial.domain.post.model.Like;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.LikeRepository;
import com.web.meosocial.domain.post.service.LikeService;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;
    @Autowired
    private ValidationService validationService;

    /**
     * Likes a post on behalf of a user. If the user has already liked the post,
     * it will throw an exception. If the like was previously deleted, it will be restored.
     *
     * @param userId the ID of the user who is liking the post
     * @param postId the ID of the post to be liked
     * @return an {@link ApiResponse} containing a {@link LikePostDto} object with the like details
     */
    @Transactional
    @Override
    public ApiResponse<LikePostDto> likePost(Long userId, String postId) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);
        if (validationService.hasNotPermissionToAction(user, post, Enums.VisibilityLevel.fromValue(post.getVisibilityLevel()))) {
            throw new IllegalArgumentException("You do not have permission to comment on this post!");
        }
        if (post.getIsDelete()) {
            throw new IllegalArgumentException("Post is deleted!");
        }
        Like liked = likeRepository.findByPostIdAndUserId(postId, userId);
        if (liked != null) {
            if (liked.getIsDeleted()) {
                liked.setIsDeleted(false);
                likeRepository.save(liked);
                return apiResponseUtils.success(new LikePostDto(liked), "Like Successfully!");
            } else {
                throw new IllegalArgumentException("User already liked this post!");
            }
        }
        Like like = new Like();
        like.setId(UUID.randomUUID().toString());
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());
        like.setIsDeleted(false);
        likeRepository.save(like);
        return apiResponseUtils.success(new LikePostDto(like), "Like Successfully!");
    }

    /**
     * Removes a like from a post on behalf of a user. If the user has not liked the post,
     * or the like is already marked as deleted, an exception is thrown.
     *
     * @param userId the ID of the user who wants to unlike the post
     * @param postId the ID of the post to be unliked
     * @return an {@link ApiResponse} containing a {@link LikePostDto} object with the updated like details
     */
    @Transactional
    @Override
    public ApiResponse<LikePostDto> unlikePost(Long userId, String postId) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);
        if (validationService.hasNotPermissionToAction(user, post, Enums.VisibilityLevel.fromValue(post.getVisibilityLevel()))) {
            throw new IllegalArgumentException("You do not have permission to comment on this post!");
        }
        if (post.getIsDelete()) {
            throw new IllegalArgumentException("Post is deleted!");
        }
        Like liked = likeRepository.findByPostIdAndUserId(postId, userId);
        if (liked == null || liked.getIsDeleted()) {
            throw new IllegalArgumentException("User already liked this post!");
        }
        liked.setIsDeleted(true);
        likeRepository.save(liked);
        return apiResponseUtils.success(new LikePostDto(liked), "Unlike Successfully!");
    }
}
