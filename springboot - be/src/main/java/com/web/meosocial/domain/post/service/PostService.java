package com.web.meosocial.domain.post.service;

import com.web.meosocial.domain.post.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    PostDto createNewPost(PostDto postDto);

    List<PostDto> getAllPostsOfUser(Long userId);

    void deletePost(String postId);

    PostDto getPost(String postId);

    PostDto updatePost(String id, PostDto postDto);
}
