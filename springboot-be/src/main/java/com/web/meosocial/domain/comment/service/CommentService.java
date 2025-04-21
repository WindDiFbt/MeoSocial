package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    ApiResponse<CommentDto> createNewComment(Long userId, CommentDto commentDto);

    ApiResponse<List<CommentDto>> getAllCommentOfUser(Long userId);

    ApiResponse<List<CommentDto>> getCommentOfPost(Long userId, String postId);

    ApiResponse<Void> deleteComment(Long userId, String commentId);

    void deleteCommentOfPost(String postId);

    ApiResponse<CommentDto> getComment(String commentId);

    ApiResponse<CommentDto> updateComment(Long userId, String commentId, CommentDto commentDto);
}
