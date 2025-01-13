package com.web.meosocial.rest;

import com.web.meosocial.service.PostMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post-media")
public class RestPostMediaController {
    @Autowired
    private PostMediaService postMediaService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addMedia(@PathVariable String postId, @RequestParam("file") MultipartFile file) {
        postMediaService.createPostMedia(postId, file);
        return ResponseEntity.ok().body("Added Media to postId: " + postId + " successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedia(@PathVariable String id) {
        return ResponseEntity.ok().body(postMediaService.getPostMediaByPostId(id));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable String id) {
        postMediaService.deletePostMedia(id);
        return ResponseEntity.ok().body("Deleted Media: " + id);
    }
}
