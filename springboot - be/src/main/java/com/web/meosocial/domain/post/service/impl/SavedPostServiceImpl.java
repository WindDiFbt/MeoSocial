package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.SavedPost;
import com.web.meosocial.domain.post.repository.SavedPostRepository;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.post.service.SavedPostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SavedPostServiceImpl implements SavedPostService {
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Override
    public List<SavedPostDto> getAllPostsSaved(Long userId) {
        return savedPostRepository.findByUserId(userId).stream().map(SavedPostDto::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SavedPostDto savePost(Long userId, String postId) {
        if (userId == null) {
            throw new UnauthorizedException("User not authorized");
        }
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(userId, postId);
        if (savedPost != null) {
            if (savedPost.getIsDelete()) {
                savedPost.setIsDelete(false);
                savedPost.setSavedAt(LocalDateTime.now());
                savedPostRepository.save(savedPost);
                return new SavedPostDto(savedPost);
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
        return new SavedPostDto(newSavePost);
    }

    @Transactional
    @Override
    public void deleteSavedPost(Long userId, String savedPostId) {
        SavedPost savedPost = savedPostRepository.findById(savedPostId).orElseThrow(() -> new IllegalArgumentException("Saved Post not found"));
        if (!savedPost.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this post");
        }
        if (savedPost.getIsDelete()) {
            throw new IllegalArgumentException("Post already deleted!");
        }
        savedPost.setIsDelete(true);
        savedPostRepository.save(savedPost);
    }
}
