package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    ApiResponseDto<CommentDto> createNewComment(Long userId, CommentDto commentDto);

    ApiResponseDto<List<CommentDto>> getAllCommentOfUser(Long userId);

    ApiResponseDto<List<CommentDto>> getCommentOfPost(String postId);

    ApiResponseDto<Void> deleteComment(Long userId, String commentId);

    void deleteCommentOfPost(String postId);

    ApiResponseDto<CommentDto> getComment(String commentId);

    ApiResponseDto<CommentDto> updateComment(Long userId, String commentId, CommentDto commentDto);
}
