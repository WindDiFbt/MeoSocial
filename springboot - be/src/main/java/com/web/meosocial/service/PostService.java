package com.web.meosocial.service;

import com.web.meosocial.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface PostService {
    PostDto createNewPost(PostDto postDto);

    Set<PostDto> getAllPostsOfUser(Long userId, boolean isDeleted);

    void deletePost(String postId);

    PostDto getPost(String postId);

    PostDto updatePost(String id, PostDto postDto);
}
