package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.comment.service.CommentService;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.post.service.PostMediaService;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostMediaService postMediaService;
    @Autowired
    private CommentService commentService;

    @Override
    public PostDto createNewPost(PostDto postDto) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(postDto.getUserId());
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
    public List<PostDto> getAllPostsOfUser(Long userId) {
        User user = userService.getUserById(userId);
        List<Post> posts = postRepository.findPostExistWithUserId(user.getId());
        posts.forEach(post ->
                post.setPostmedia(post.getPostmedia().stream()
                        .filter(pm -> !pm.getIsDelete()).collect(Collectors.toList()))
        );
        return posts.stream().map(PostDto::new).collect(Collectors.toList());
    }

    @Override
    public void deletePost(String postId) {
        Post post = getPostById(postId);
        post.setIsDelete(true);
        post.setDeletedAt(LocalDateTime.now());
        postMediaService.deletePostMediaOfPost(postId);
        commentService.deleteCommentOfPost(postId);
        postRepository.save(post);
    }

    @Override
    public PostDto getPost(String postId) {
        Post post = getPostById(postId);
        post.setPostmedia(post.getPostmedia().stream()
                .filter(pm -> !pm.getIsDelete()).collect(Collectors.toList()));
        return new PostDto(post);
    }

    @Override
    public PostDto updatePost(String postId, PostDto postDto) {
        Post post = getPostById(postId);
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
        return new PostDto(post);
    }

    private Post getPostById(String postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getIsDelete()) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
        return post;
    }
}
