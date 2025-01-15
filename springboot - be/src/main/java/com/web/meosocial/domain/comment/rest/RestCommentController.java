package com.web.meosocial.domain.comment.rest;

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

    @PostMapping("/new")
    public ResponseEntity<?> createNewComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.createNewComment(commentDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentOfUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(commentService.getAllCommentOfUser(userId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentOfPost(@PathVariable String postId) {
        return ResponseEntity.ok().body(commentService.getCommentOfPost(postId));
    }

    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully!");
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable String commentId) {
        return ResponseEntity.ok().body(commentService.getComment(commentId));
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable String commentId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.updateComment(commentId, commentDto));
    }
}
