package com.web.meosocial.rest.post;

import com.web.meosocial.dto.post.PostDto;
import com.web.meosocial.service.post.PostService;
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

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.ok().body("Successfully deleted post.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable String id) {
        return ResponseEntity.ok().body(postService.getPost(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.updatePost(id, postDto));
    }
}
