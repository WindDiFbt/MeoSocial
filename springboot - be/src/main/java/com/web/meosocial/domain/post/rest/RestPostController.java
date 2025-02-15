package com.web.meosocial.domain.post.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.post.dto.ChangeVisibilityDto;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.dto.SharedPostDto;
import com.web.meosocial.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class RestPostController {
    @Autowired
    private PostService postService;
    @Autowired
    private AuthUtils authUtils;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPost(@PathVariable Long userId) {
        authUtils.validateUserAuthenticated();
        return ResponseEntity.ok().body(postService.getAllPostsOfUser(userId));
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.createNewPost(userId, postDto));
    }

    @PatchMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.deletePost(userId, postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.getPost(userId, postId));
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId, @RequestBody PostDto postDto) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.updatePost(userId, postId, postDto));
    }

    @PostMapping("/update/visibility")
    public ResponseEntity<?> updatePostVisibility(@RequestBody ChangeVisibilityDto changeVisibilityDto) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.changeVisibilityLevel(userId, changeVisibilityDto));
    }

    @PostMapping("/share")
    public ResponseEntity<?> sharePost(@RequestBody SharedPostDto sharedPostDto) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(postService.sharePost(userId, sharedPostDto));
    }
}
