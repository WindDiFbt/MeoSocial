package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.LikePostDto;
import com.web.meosocial.payload.ApiResponseDto;

public interface LikeService {
    ApiResponseDto<LikePostDto> likePost(Long userId, String postId);

    ApiResponseDto<LikePostDto> unlikePost(Long userId, String postId);
}
