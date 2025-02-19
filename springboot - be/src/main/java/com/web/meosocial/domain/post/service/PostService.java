package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.ChangeVisibilityDto;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.dto.SharedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    ApiResponseDto<PostDto> createNewPost(Long userId, PostDto postDto);

    ApiResponseDto<List<PostDto>> getAllPostsOfUser(Long userId);

    ApiResponseDto<Void> deletePost(Long userId, String postId);

    ApiResponseDto<PostDto> getPost(String postId);

    ApiResponseDto<PostDto> changeVisibilityLevel(Long userId, ChangeVisibilityDto changeVisibilityDto);

    ApiResponseDto<PostDto> updatePost(Long userId, String id, PostDto postDto);

    Post getPostById(String postId);

    ApiResponseDto<PostDto> sharePost(Long userId, SharedPostDto sharedPostDto);
}
