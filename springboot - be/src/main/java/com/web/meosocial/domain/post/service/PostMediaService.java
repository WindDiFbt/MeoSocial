package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.PostMediaDto;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PostMediaService {
    ApiResponseDto<List<PostMediaDto>> getPostMediaByPostId(String postId);

    ApiResponseDto<PostMediaDto> createPostMedia(Long userId, String postId, MultipartFile file);

    ApiResponseDto<Void> deletePostMedia(Long userId, String postMediaId);

    void deletePostMediaOfPost(String postId);

    PostMediaDto updatePostMedia(PostMediaDto postMediaDto);
}
