package com.web.meosocial.domain.comment.service.impl;

import com.web.meosocial.domain.cloudinary.CloudinaryService;
import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.comment.dto.CommentMediaDto;
import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.comment.model.CommentMedia;
import com.web.meosocial.domain.comment.repository.CommentMediaRepository;
import com.web.meosocial.domain.comment.repository.CommentRepository;
import com.web.meosocial.domain.comment.service.CommentMediaService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentMediaServiceImpl implements CommentMediaService {
    @Autowired
    private CommentMediaRepository commentMediaRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    @Transactional
    @Override
    public ApiResponse<CommentMediaDto> createCommentMedia(Long userId, String commentId, MultipartFile file) {
        CommentMedia commentMedia = new CommentMedia();
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment not found or deleted: " + commentId);
        }
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have authorization to create comment media");
        }
        try {
            commentMedia.setId(UUID.randomUUID().toString());
            commentMedia.setComment(comment);
            if (validationService.isImage(file)) {
                commentMedia.setMediaType(Enums.MediaType.IMAGE.getValue());
                commentMedia.setMediaUrl(cloudinaryService.getImageUrlAfterUpload(file, Enums.FolderCloudinary.CommentMedia.toString()));
                commentMedia.setMediaSize(file.getSize());
            } else if (validationService.isVideo(file)) {
                commentMedia.setMediaType(Enums.MediaType.VIDEO.getValue());
                commentMedia.setMediaUrl(cloudinaryService.getVideoUrlAfterUpload(file, Enums.FolderCloudinary.CommentMedia.toString()));
                commentMedia.setMediaSize(file.getSize());
            } else {
                throw new IllegalArgumentException("Media type not supported");
            }
            commentMedia.setCreatedAt(LocalDateTime.now());
            commentMedia.setIsDelete(false);
            commentMediaRepository.save(commentMedia);
            return apiResponseUtils.success(new CommentMediaDto(commentMedia), "Created comment media successfully");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error uploading media to Cloudinary: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteCommentMedia(Long userId, String commentMediaId) {
        CommentMedia commentMedia = commentMediaRepository.findById(commentMediaId).orElse(null);
        if (commentMedia == null || commentMedia.getIsDelete()) {
            throw new IllegalArgumentException("Comment not found or deleted: " + commentMediaId);
        }
        if (!commentMedia.getComment().getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have authorization to delete comment media");
        }
        commentMedia.setIsDelete(true);
        commentMediaRepository.save(commentMedia);
        return apiResponseUtils.success(null, "Deleted comment media successfully");
    }

    @Transactional
    @Override
    public void deleteCommentMediaOfComment(String commentId) {
        List<CommentMedia> medias = commentMediaRepository.findAllByCommentId(commentId);
        medias.forEach(media -> {
            media.setIsDelete(true);
            media.setDeletedAt(LocalDateTime.now());
        });
        commentMediaRepository.saveAll(medias);
    }

    @Override
    public ApiResponse<List<CommentMediaDto>> getAllCommentMediaByCommentId(String commentId) {
        return apiResponseUtils.success(commentMediaRepository.findAllByCommentId(commentId).stream()
                .map(CommentMediaDto::new).collect(Collectors.toList()), "Get all comment media successfully");
    }
}
