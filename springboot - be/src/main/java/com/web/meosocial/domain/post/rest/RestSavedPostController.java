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
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(savedPostService.savePost(userId, postId));
    }

    @GetMapping()
    public ResponseEntity<?> getAllSavedPosts() {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(savedPostService.getAllPostsSaved(userId));
    }

    @PatchMapping("/remove/{savedPostId}")
    public ResponseEntity<?> deleteSavedPost(@PathVariable String savedPostId) {
        long userId = authUtils.getCurrentUserId();
        return ResponseEntity.ok().body(savedPostService.deleteSavedPost(userId, savedPostId));
    }
}
