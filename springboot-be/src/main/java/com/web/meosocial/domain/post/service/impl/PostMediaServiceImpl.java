package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.domain.cloudinary.CloudinaryService;
import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.post.dto.PostMediaDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.PostMedia;
import com.web.meosocial.domain.post.repository.PostMediaRepository;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.post.service.PostMediaService;
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
public class PostMediaServiceImpl implements PostMediaService {
    @Autowired
    private PostMediaRepository postMediaRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    @Override
    public ApiResponse<List<PostMediaDto>> getPostMediaByPostId(String postId) {
        return apiResponseUtils.success(postMediaRepository.findAllByPostId(postId, false).stream()
                .map(PostMediaDto::new).collect(Collectors.toList()), "Get All Post Media of Post Id " + postId + "successfully!");
    }

    @Transactional
    @Override
    public ApiResponse<PostMediaDto> createPostMedia(Long userId, String postId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }
        PostMedia postMedia = new PostMedia();
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getIsDelete()) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have authorization to create post media ");
        }
        try {
            postMedia.setPost(post);
            postMedia.setId(UUID.randomUUID().toString());
            if (validationService.isImage(file)) {
                postMedia.setMediaType(Enums.MediaType.IMAGE.getValue());
                postMedia.setMediaUrl(cloudinaryService.getImageUrlAfterUpload(file, Enums.FolderCloudinary.PostMedia.toString()));
                postMedia.setMediaSize(file.getSize());
            } else if (validationService.isVideo(file)) {
                postMedia.setMediaType(Enums.MediaType.VIDEO.getValue());
                postMedia.setMediaUrl(cloudinaryService.getVideoUrlAfterUpload(file, Enums.FolderCloudinary.PostMedia.toString()));
                postMedia.setMediaSize(file.getSize());
            } else {
                throw new IllegalArgumentException("Media type not supported");
            }
            postMedia.setCreatedAt(LocalDateTime.now());
            postMedia.setIsDelete(false);
            postMediaRepository.save(postMedia);
            return apiResponseUtils.success(new PostMediaDto(postMedia), "Create Post Media of Post Id " + postId + "successfully!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error uploading media to Cloudinary: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public PostMediaDto updatePostMedia(PostMediaDto postMediaDto) {
        return null;
    }

    @Transactional
    @Override
    public ApiResponse<Void> deletePostMedia(Long userId, String postMediaId) {
        PostMedia postMedia = postMediaRepository.findById(postMediaId).orElse(null);
        if (postMedia == null || postMedia.getIsDelete()) {
            throw new IllegalArgumentException("Post media not found");
        }
        if (!postMedia.getPost().getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have authorization to create post media ");
        }
        postMedia.setIsDelete(true);
        postMediaRepository.save(postMedia);
        return apiResponseUtils.success(null, "Delete Post Media of Post Id " + postMediaId + "successfully!");
    }

    @Transactional
    @Override
    public void deletePostMediaOfPost(String postId) {
        postMediaRepository.updateIsDeleteAndDeletedAtByPostId(postId, true, LocalDateTime.now());
    }

    @Override
    public ApiResponse<List<PostMediaDto>> getAllPostMediaOfUser(Long userId) {
        return apiResponseUtils.success(postMediaRepository.findAllNotDeletedByUserId(userId).stream()
                .map(PostMediaDto::new).collect(Collectors.toList()), "Get All Post Media of User Id " + userId + " successfully!");
    }
}
