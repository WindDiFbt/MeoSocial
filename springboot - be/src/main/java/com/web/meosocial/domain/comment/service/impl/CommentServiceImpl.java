package com.web.meosocial.domain.comment.service.impl;

import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.comment.repository.CommentRepository;
import com.web.meosocial.domain.comment.service.CommentMediaService;
import com.web.meosocial.domain.comment.service.CommentService;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentMediaService commentMediaService;
    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Override
    public CommentDto createNewComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(commentDto.getUserId());
        comment.setUser(user);
        Post post = getPostById(commentDto.getPostId());
        comment.setPost(post);
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setIsDelete(false);
        commentRepository.save(comment);
        return new CommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentOfUser(Long userId) {
        User user = userService.getUserById(userId);
        List<Comment> comments = commentRepository.findCommentsExistByUserId(user.getId());
        comments.forEach(comment ->
                comment.setCommentmedia(comment.getCommentmedia().stream()
                        .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()))
        );
        return comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentOfPost(String postId) {
        List<Comment> comments = commentRepository.findCommentsExistByPostId(postId);
        comments.forEach(comment ->
                comment.setCommentmedia(comment.getCommentmedia().stream()
                        .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()))
        );
        return comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteComment(String commentId) {
        Comment comment = getCommentById(commentId);
        comment.setIsDelete(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentMediaService.deleteCommentMediaOfComment(commentId);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteCommentOfPost(String postId) {
        List<Comment> comments = commentRepository.findCommentsExistByPostId(postId);
        comments.forEach(comment -> {
            comment.setIsDelete(true);
            comment.setDeletedAt(LocalDateTime.now());
            commentMediaService.deleteCommentMediaOfComment(comment.getId());
        });
        commentRepository.saveAll(comments);
    }

    @Override
    public CommentDto getComment(String commentId) {
        Comment comment = getCommentById(commentId);
        comment.setCommentmedia(comment.getCommentmedia().stream()
                .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
        return new CommentDto(comment);
    }

    @Transactional
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

    private Post getPostById(String postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getIsDelete()) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
        return post;
    }
}
