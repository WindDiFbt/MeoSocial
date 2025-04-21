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
        return ResponseEntity.ok().body(savedPostService.savePost(authUtils.getCurrentUserId(), postId));
    }

    @GetMapping()
    public ResponseEntity<?> getAllSavedPosts() {
        return ResponseEntity.ok().body(savedPostService.getAllPostsSaved(authUtils.getCurrentUserId()));
    }

    @PatchMapping("/remove/{savedPostId}")
    public ResponseEntity<?> deleteSavedPost(@PathVariable String savedPostId) {
        return ResponseEntity.ok().body(savedPostService.deleteSavedPost(authUtils.getCurrentUserId(), savedPostId));
    }
}
