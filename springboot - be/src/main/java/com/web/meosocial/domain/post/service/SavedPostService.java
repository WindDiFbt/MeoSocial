package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedPostService {
    ApiResponse<List<SavedPostDto>> getAllPostsSaved(Long userId);

    ApiResponse<SavedPostDto> savePost(Long userId, String postId);

    ApiResponse<Void> deleteSavedPost(Long userId, String savedPostId);
}
