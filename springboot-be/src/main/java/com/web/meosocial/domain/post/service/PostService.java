package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.ChangeVisibilityDto;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.dto.SharedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    ApiResponse<PostDto> createNewPost(Long userId, PostDto postDto);

    ApiResponse<List<PostDto>> getAllPostsOfUser(Long userId);

    ApiResponse<Void> deletePost(Long userId, String postId);

    ApiResponse<PostDto> getPost(String postId);

    ApiResponse<PostDto> changeVisibilityLevel(Long userId, ChangeVisibilityDto changeVisibilityDto);

    ApiResponse<PostDto> updatePost(Long userId, String id, PostDto postDto);

    Post getPostById(String postId);

    ApiResponse<PostDto> sharePost(Long userId, SharedPostDto sharedPostDto);
}
