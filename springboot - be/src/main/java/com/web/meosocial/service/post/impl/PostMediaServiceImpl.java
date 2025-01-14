package com.web.meosocial.service.post.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.post.Post;
import com.web.meosocial.domain.post.PostMedia;
import com.web.meosocial.dto.post.PostMediaDto;
import com.web.meosocial.repository.post.PostMediaRepository;
import com.web.meosocial.repository.post.PostRepository;
import com.web.meosocial.service.cloudinary.CloudinaryService;
import com.web.meosocial.service.post.PostMediaService;
import com.web.meosocial.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private PostRepository postRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<PostMediaDto> getPostMediaByPostId(String postId) {
        return postMediaRepository.findAllByPostId(postId, false).stream()
                .map(PostMediaDto::new).collect(Collectors.toList());
    }

    @Override
    public void createPostMedia(String postId, MultipartFile file) {
        PostMedia postMedia = new PostMedia();
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
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
                throw new IllegalArgumentException("Media type unsupported");
            }
            postMedia.setCreatedAt(LocalDateTime.now());
            postMedia.setIsDeleted(false);
            postMediaRepository.save(postMedia);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error uploading media to Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public PostMediaDto updatePostMedia(PostMediaDto postMediaDto) {
        return null;
    }

    @Override
    public void deletePostMedia(String postMediaId) {
        PostMedia postMedia = postMediaRepository.findById(postMediaId).orElse(null);
        if (postMedia == null || postMedia.getIsDeleted()) {
            throw new IllegalArgumentException("Post media not found");
        }
        postMedia.setIsDeleted(true);
        postMediaRepository.save(postMedia);
    }
}
