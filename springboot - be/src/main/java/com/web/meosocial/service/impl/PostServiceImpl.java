package com.web.meosocial.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.Post;
import com.web.meosocial.domain.User;
import com.web.meosocial.dto.PostDto;
import com.web.meosocial.repository.PostRepository;
import com.web.meosocial.repository.UserRepository;
import com.web.meosocial.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PostDto createNewPost(PostDto postDto) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User Not Found: " + postDto.getUserId()));
        post.setUser(user);
        post.setContent(postDto.getContent());
        if (postDto.getVisibilityLevel() == null) {
            post.setVisibilityLevel(Enums.VisibilityLevel.PUBLIC.getValue());
        } else {
            post.setVisibilityLevel(postDto.getVisibilityLevel());
        }
        post.setIsDelete(false);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return new PostDto(post);
    }

    @Override
    public Set<PostDto> getAllPostsOfUser(Long userId, boolean isDeleted) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found: " + userId));
        List<Post> posts = postRepository.findByUserAndIsDelete(user, isDeleted);
        return posts.stream().map(PostDto::new).collect(Collectors.toSet());
    }

    @Override
    public void deletePost(String postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null && !post.getIsDelete()) {
            post.setIsDelete(true);
            post.setDeletedAt(LocalDateTime.now());
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
    }

    @Override
    public PostDto getPost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post Not Found: " + postId));
        if (post == null) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
        return new PostDto(post);
    }

    @Override
    public PostDto updatePost(String id, PostDto postDto) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && !post.getIsDelete()) {
            post.setContent(postDto.getContent());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Post Not Found or deleted: " + id);
        }
        return new PostDto(post);
    }
}
