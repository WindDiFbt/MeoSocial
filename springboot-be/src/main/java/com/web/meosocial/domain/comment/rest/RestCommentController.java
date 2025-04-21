package com.web.meosocial.domain.comment.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class RestCommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/new")
    public ResponseEntity<?> createNewComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.createNewComment(authUtils.getCurrentUserId(), commentDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentOfUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(commentService.getAllCommentOfUser(userId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentOfPost(@PathVariable String postId) {
        return ResponseEntity.ok().body(commentService.getCommentOfPost(authUtils.getCurrentUserId(), postId));
    }

    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.deleteComment(authUtils.getCurrentUserId(), commentId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable String commentId) {
        return ResponseEntity.ok().body(commentService.getComment(commentId));
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable String commentId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.updateComment(authUtils.getCurrentUserId(), commentId, commentDto));
    }
}
