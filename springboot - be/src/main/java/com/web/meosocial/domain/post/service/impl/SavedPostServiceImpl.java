package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.SavedPost;
import com.web.meosocial.domain.post.repository.SavedPostRepository;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.post.service.SavedPostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SavedPostServiceImpl implements SavedPostService {
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApiResponseUtil apiResponseUtil;

    @Override
    public ApiResponseDto<List<SavedPostDto>> getAllPostsSaved(Long userId) {
        List<SavedPostDto> list = savedPostRepository.findByUserId(userId)
                .stream()
                .map(SavedPostDto::new)
                .toList();
        return apiResponseUtil.success(list, "Get all saved posts successfully!");
    }

    @Transactional
    @Override
    public ApiResponseDto<SavedPostDto> savePost(Long userId, String postId) {
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(userId, postId);
        if (savedPost != null) {
            if (savedPost.getIsDelete()) {
                savedPost.setIsDelete(false);
                savedPost.setSavedAt(LocalDateTime.now());
                savedPostRepository.save(savedPost);
                return apiResponseUtil.success(new SavedPostDto(savedPost), "Saved post successfully!");
            } else {
                throw new IllegalArgumentException("Post already saved!");
            }
        }
        SavedPost newSavePost = new SavedPost();
        User user = userService.getUserById(userId);
        Post post = postService.getPostById(postId);
        newSavePost.setId(UUID.randomUUID().toString());
        newSavePost.setUser(user);
        newSavePost.setPost(post);
        newSavePost.setSavedAt(LocalDateTime.now());
        newSavePost.setIsDelete(false);
        savedPostRepository.save(newSavePost);
        return ApiResponseDto.<SavedPostDto>builder()
                .status(String.valueOf(HttpStatus.OK))
                .message(List.of("Saved post successfully!"))
                .response(new SavedPostDto(newSavePost))
                .build();
    }

    @Transactional
    @Override
    public ApiResponseDto<Void> deleteSavedPost(Long userId, String savedPostId) {
        SavedPost savedPost = savedPostRepository.findById(savedPostId).orElseThrow(() -> new IllegalArgumentException("Saved Post not found"));
        if (!savedPost.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this post");
        }
        if (savedPost.getIsDelete()) {
            throw new IllegalArgumentException("Post already deleted!");
        }
        savedPost.setIsDelete(true);
        savedPostRepository.save(savedPost);
        return apiResponseUtil.success(null, "Deleted post successfully!");
    }
}
