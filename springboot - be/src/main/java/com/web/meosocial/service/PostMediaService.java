package com.web.meosocial.service;

import com.web.meosocial.dto.PostMediaDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PostMediaService {
    List<PostMediaDto> getPostMediaByPostId(String id);

    void createPostMedia(String postId, MultipartFile file);

    void deletePostMedia(String postMediaId);

    PostMediaDto updatePostMedia(PostMediaDto postMediaDto);
}
