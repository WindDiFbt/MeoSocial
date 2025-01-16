package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    CommentDto createNewComment(CommentDto commentDto);

    List<CommentDto> getAllCommentOfUser(Long userId);

    List<CommentDto> getCommentOfPost(String postId);

    void deleteComment(String commentId);

    void deleteCommentOfPost(String postId);

    CommentDto getComment(String commentId);

    CommentDto updateComment(String commentId, CommentDto commentDto);
}
