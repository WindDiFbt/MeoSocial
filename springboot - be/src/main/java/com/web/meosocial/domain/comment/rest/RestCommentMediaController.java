package com.web.meosocial.domain.comment.rest;

import com.web.meosocial.domain.comment.service.CommentMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/comment-media")
public class RestCommentMediaController {
    @Autowired
    private CommentMediaService commentMediaService;

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentMediaByCommentId(@PathVariable String commentId) {
        return ResponseEntity.ok(commentMediaService.getAllCommentMediaByCommentId(commentId));
    }

    @PostMapping("/add/{commentId}")
    public ResponseEntity<?> addCommentMedia(@PathVariable String commentId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(commentMediaService.createCommentMedia(commentId, file));
    }

    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteCommentMedia(@PathVariable String commentId) {
        commentMediaService.deleteCommentMedia(commentId);
        return ResponseEntity.ok().body("Deleted comment media successfully.");
    }
}
