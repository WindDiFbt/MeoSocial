package com.web.meosocial.service.comment;

import com.web.meosocial.dto.comment.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    CommentDto createNewComment(CommentDto commentDto);

    List<CommentDto> getAllCommentOfUser(Long userId);

    List<CommentDto> getCommentOfPost(String postId);

    void deleteComment(String commentId);

    CommentDto getComment(String commentId);

    CommentDto updateComment(String commentId, CommentDto commentDto);
}
