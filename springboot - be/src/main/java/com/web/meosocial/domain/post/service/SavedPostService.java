package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedPostService {
    ApiResponseDto<List<SavedPostDto>> getAllPostsSaved(Long userId);

    ApiResponseDto<SavedPostDto> savePost(Long userId, String postId);

    ApiResponseDto<Void> deleteSavedPost(Long userId, String savedPostId);
}
