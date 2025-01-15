package com.web.meosocial.domain.post.rest;

import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class RestPostController {
    @Autowired
    private PostService postService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPost(@PathVariable Long userId) {
        return ResponseEntity.ok().body(postService.getAllPostsOfUser(userId));
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.createNewPost(postDto));
    }

    @PatchMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().body("Successfully deleted post.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId, @RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.updatePost(postId, postDto));
    }
}
