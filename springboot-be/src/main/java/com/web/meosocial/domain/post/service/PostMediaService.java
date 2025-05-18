package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.PostMediaDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PostMediaService {
    ApiResponse<List<PostMediaDto>> getPostMediaByPostId(String postId);

    ApiResponse<PostMediaDto> createPostMedia(Long userId, String postId, MultipartFile file);

    ApiResponse<Void> deletePostMedia(Long userId, String postMediaId);

    void deletePostMediaOfPost(String postId);

    ApiResponse<List<PostMediaDto>> getAllPostMediaOfUser(Long userId);

    PostMediaDto updatePostMedia(PostMediaDto postMediaDto);
}
