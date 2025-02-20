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
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.util.ApiResponseUtils;
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
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    @Transactional
    @Override
    public ApiResponse<CommentDto> createNewComment(Long userId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(userId);
        comment.setUser(user);
        Post post = getPostById(commentDto.getPostId());
        comment.setPost(post);
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setIsDelete(false);
        commentRepository.save(comment);
        return apiResponseUtils.success(new CommentDto(comment), "Create new comment successfully!");
    }

    @Override
    public ApiResponse<List<CommentDto>> getAllCommentOfUser(Long userId) {
        User user = userService.getUserById(userId);
        List<Comment> comments = commentRepository.findCommentsExistByUserId(user.getId());
        comments.forEach(comment ->
                comment.setCommentmedia(comment.getCommentmedia().stream()
                        .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()))
        );
        return apiResponseUtils.success(comments.stream().map(CommentDto::new).collect(Collectors.toList()), "Get all comments successfully!");
    }

    @Override
    public ApiResponse<List<CommentDto>> getCommentOfPost(String postId) {
        List<Comment> comments = commentRepository.findCommentsExistByPostId(postId);
        comments.forEach(comment ->
                comment.setCommentmedia(comment.getCommentmedia().stream()
                        .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()))
        );
        return apiResponseUtils.success(comments.stream().map(CommentDto::new).collect(Collectors.toList()), "Get all comments of post successfully!");
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteComment(Long userId, String commentId) {
        Comment comment = getCommentById(commentId);
        if(comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment is deleted");
        }
        if(!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        comment.setIsDelete(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentMediaService.deleteCommentMediaOfComment(commentId);
        commentRepository.save(comment);
        return apiResponseUtils.success(null, "Comment deleted successfully!");
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
    public ApiResponse<CommentDto> getComment(String commentId) {
        Comment comment = getCommentById(commentId);
        comment.setCommentmedia(comment.getCommentmedia().stream()
                .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
        return apiResponseUtils.success(new CommentDto(comment), "Get comment successfully!");
    }

    @Transactional
    @Override
    public ApiResponse<CommentDto> updateComment(Long userId, String commentId, CommentDto commentDto) {
        Comment comment = getCommentById(commentId);
        if(comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment is deleted");
        }
        if(!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        comment.setContent(commentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return apiResponseUtils.success(new CommentDto(comment), "Update comment successfully!");
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
