package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentMediaDto;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CommentMediaService {
    ApiResponseDto<CommentMediaDto> createCommentMedia(Long userId, String commentId, MultipartFile file);

    ApiResponseDto<Void> deleteCommentMedia(Long userId, String commentMediaId);

    void deleteCommentMediaOfComment(String commentId);

    ApiResponseDto<List<CommentMediaDto>> getAllCommentMediaByCommentId(String commentId);
}
