package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedPostService {
    List<SavedPostDto> getAllPostsSaved(Long userId);

    SavedPostDto savePost(Long userId, String postId);

    void deleteSavedPost(Long userId, String savedPostId);
}
