package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.domain.post.dto.LikePostDto;
import com.web.meosocial.domain.post.model.Like;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.LikeRepository;
import com.web.meosocial.domain.post.service.LikeService;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
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

    @Transactional
    @Override
    public ApiResponse<LikePostDto> likePost(Long userId, String postId) {
        Post post = postService.getPostById(postId);
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
        User user = userService.getUserById(userId);
        Like like = new Like();
        like.setId(UUID.randomUUID().toString());
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());
        like.setIsDeleted(false);
        likeRepository.save(like);
        return apiResponseUtils.success(new LikePostDto(like), "Like Successfully!");
    }

    @Transactional
    @Override
    public ApiResponse<LikePostDto> unlikePost(Long userId, String postId) {
        Post post = postService.getPostById(postId);
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
