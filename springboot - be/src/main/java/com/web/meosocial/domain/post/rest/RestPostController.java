package com.web.meosocial.domain.post.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.post.dto.ChangeVisibilityDto;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.dto.SharedPostDto;
import com.web.meosocial.domain.post.service.LikeService;
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
    private LikeService likeService;
    @Autowired
    private AuthUtils authUtils;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPost(@PathVariable Long userId) {
        authUtils.validateUserAuthenticated();
        return ResponseEntity.ok().body(postService.getAllPostsOfUser(userId));
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.createNewPost(authUtils.getCurrentUserId(), postDto));
    }

    @PatchMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        return ResponseEntity.ok().body(postService.deletePost(authUtils.getCurrentUserId(), postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId, @RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.updatePost(authUtils.getCurrentUserId(), postId, postDto));
    }

    @PostMapping("/update/visibility")
    public ResponseEntity<?> updatePostVisibility(@RequestBody ChangeVisibilityDto changeVisibilityDto) {
        return ResponseEntity.ok().body(postService.changeVisibilityLevel(authUtils.getCurrentUserId(), changeVisibilityDto));
    }

    @PostMapping("/share")
    public ResponseEntity<?> sharePost(@RequestBody SharedPostDto sharedPostDto) {
        return ResponseEntity.ok().body(postService.sharePost(authUtils.getCurrentUserId(), sharedPostDto));
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable String postId) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(likeService.likePost(userId, postId));
    }

    @PostMapping("/unlike/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable String postId) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(likeService.unlikePost(userId, postId));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAllPostsOfUser(authUtils.getCurrentUserId()));
    }
}
