package com.web.meosocial.domain.comment.service;

import com.web.meosocial.domain.comment.dto.CommentMediaDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CommentMediaService {
    CommentMediaDto createCommentMedia(String commentId, MultipartFile file);

    void deleteCommentMedia(String commentMediaId);

    void deleteCommentMediaOfComment(String commentId);

    List<CommentMediaDto> getAllCommentMediaByCommentId(String commentId);
}
