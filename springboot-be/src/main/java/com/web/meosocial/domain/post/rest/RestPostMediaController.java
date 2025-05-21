package com.web.meosocial.domain.post.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.post.service.PostMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post-media")
public class RestPostMediaController {
    @Autowired
    private PostMediaService postMediaService;
    @Autowired
    private AuthUtils authUtils;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPostMediaOfUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(postMediaService.getAllPostMediaOfUser(userId));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addPostMedia(@PathVariable String postId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(postMediaService.createPostMedia(authUtils.getCurrentUserId(), postId, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostMedia(@PathVariable String id) {
        return ResponseEntity.ok().body(postMediaService.getPostMediaByPostId(id));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deletePostMedia(@PathVariable String id) {
        return ResponseEntity.ok().body(postMediaService.deletePostMedia(authUtils.getCurrentUserId(), id));
    }
}
