package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.LikePostDto;
import com.web.meosocial.payload.response.ApiResponse;

public interface LikeService {
    ApiResponse<LikePostDto> likePost(Long userId, String postId);

    ApiResponse<LikePostDto> unlikePost(Long userId, String postId);
}
