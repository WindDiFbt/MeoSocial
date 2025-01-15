package com.web.meosocial.domain.comment.service.impl;

import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.domain.comment.repository.CommentRepository;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.user.repository.UserRepository;
import com.web.meosocial.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public CommentDto createNewComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        User user = userRepository.findById(commentDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        comment.setUser(user);
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("Post Not Found"));
        comment.setPost(post);
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setIsDelete(false);
        commentRepository.save(comment);
        return new CommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentOfUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found: " + userId));
        List<Comment> comments = commentRepository.findCommentsExistByUserId(userId);
        comments.forEach(comment -> {
            comment.setCommentmedia(comment.getCommentmedia().stream()
                    .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
        });
        return comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentOfPost(String postId) {
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post Not Found: " + postId));
        List<Comment> comments = commentRepository.findCommentsExistByPostId(postId);
        comments.forEach(comment -> {
            comment.setCommentmedia(comment.getCommentmedia().stream()
                    .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
        });
        return comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = getCommentById(commentId);
        comment.setIsDelete(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public CommentDto getComment(String commentId) {
        Comment comment = getCommentById(commentId);
        comment.setCommentmedia(comment.getCommentmedia().stream()
                .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
        return new CommentDto(comment);
    }

    @Override
    public CommentDto updateComment(String commentId, CommentDto commentDto) {
        Comment comment = getCommentById(commentId);
        comment.setContent(commentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return new CommentDto(comment);
    }

    private Comment getCommentById(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment not found or deleted: " + commentId);
        }
        return comment;
    }
}
