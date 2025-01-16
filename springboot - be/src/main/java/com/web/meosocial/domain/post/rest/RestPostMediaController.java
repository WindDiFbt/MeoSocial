package com.web.meosocial.domain.post.rest;

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

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addMedia(@PathVariable String postId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body( postMediaService.createPostMedia(postId, file));
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
