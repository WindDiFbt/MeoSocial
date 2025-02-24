package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentMediaDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CommentMediaService {
    ApiResponse<CommentMediaDto> createCommentMedia(Long userId, String commentId, MultipartFile file);

    ApiResponse<Void> deleteCommentMedia(Long userId, String commentMediaId);

    void deleteCommentMediaOfComment(String commentId);

    ApiResponse<List<CommentMediaDto>> getAllCommentMediaByCommentId(String commentId);
}
