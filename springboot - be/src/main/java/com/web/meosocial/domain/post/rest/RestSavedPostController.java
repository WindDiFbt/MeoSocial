package com.web.meosocial.domain.post.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.post.service.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/saved-post")
public class RestSavedPostController {
    @Autowired
    private SavedPostService savedPostService;
    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/save/{postId}")
    public ResponseEntity<?> save(@PathVariable String postId) {
        Long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(savedPostService.savePost(userId, postId));
    }

    @GetMapping()
    public ResponseEntity<?> getAllSavedPosts() {
        Long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(savedPostService.getAllPostsSaved(userId));
    }

    @PostMapping("/{savedPostId}")
    public ResponseEntity<?> deleteSavedPost(@PathVariable String savedPostId) {
        Long userId = authUtils.getCurrentUserId();
        savedPostService.deleteSavedPost(userId, savedPostId);
        return ResponseEntity.ok().body("Post remove successfully");
    }
}
