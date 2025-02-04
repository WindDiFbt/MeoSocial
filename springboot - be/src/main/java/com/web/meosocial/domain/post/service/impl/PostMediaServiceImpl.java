package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.cloudinary.CloudinaryService;
import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.post.dto.PostMediaDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.PostMedia;
import com.web.meosocial.domain.post.repository.PostMediaRepository;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.post.service.PostMediaService;
import com.web.meosocial.domain.validator.service.ValidationService;
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

    @Override
    public List<PostMediaDto> getPostMediaByPostId(String postId) {
        return postMediaRepository.findAllByPostId(postId, false).stream()
                .map(PostMediaDto::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PostMediaDto createPostMedia(String postId, MultipartFile file) {
        PostMedia postMedia = new PostMedia();
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getIsDelete()) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
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
            return new PostMediaDto(postMedia);
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
    public void deletePostMedia(String postMediaId) {
        PostMedia postMedia = postMediaRepository.findById(postMediaId).orElse(null);
        if (postMedia == null || postMedia.getIsDelete()) {
            throw new IllegalArgumentException("Post media not found");
        }
        postMedia.setIsDelete(true);
        postMediaRepository.save(postMedia);
    }

    @Transactional
    @Override
    public void deletePostMediaOfPost(String postId) {
        List<PostMedia> medias = postMediaRepository.findAllByPostId(postId, false);
        medias.forEach(media -> {
            media.setIsDelete(true);
            media.setDeletedAt(LocalDateTime.now());
        });
        postMediaRepository.saveAll(medias);
    }
}
