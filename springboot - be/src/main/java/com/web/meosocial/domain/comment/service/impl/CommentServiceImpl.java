package com.web.meosocial.domain.comment.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.comment.dto.CommentDto;
import com.web.meosocial.domain.comment.model.Comment;
import com.web.meosocial.domain.comment.repository.CommentRepository;
import com.web.meosocial.domain.comment.service.CommentMediaService;
import com.web.meosocial.domain.comment.service.CommentService;
import com.web.meosocial.domain.notification.service.NotificationService;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private ValidationService validationService;
    @Autowired
    private NotificationService notificationService;

    /**
     * Service method to create a new comment on a post.
     *
     * @param userId     The ID of the user creating the comment.
     * @param commentDto The DTO containing comment details.
     * @return ApiResponse containing the created CommentDto.
     * @throws IllegalArgumentException if the user does not have permission to comment or
     *                                  if the parent comment does not exist in the post.
     */
    @Transactional
    @Override
    public ApiResponse<CommentDto> createNewComment(Long userId, CommentDto commentDto) {
        if (commentDto.getContent().isBlank()) {
            throw new IllegalArgumentException("Please enter content of the comment!");
        }
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(userId);
        Post post = getPostById(commentDto.getPostId());
        // Check permission to comment on the post
        if (validationService.hasNotPermissionToAction(user, post, Enums.VisibilityLevel.fromValue(post.getVisibilityLevel()))) {
            throw new IllegalArgumentException("You do not have permission to comment on this post!");
        }
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setIsDelete(false);
        // Handle parent comment if it exists
        if (commentDto.getParentCommentId() != null) {
            // Validate if the parent comment belongs to the same post
            if (!commentRepository.existsByCommentIdAndPostId(commentDto.getParentCommentId(), commentDto.getPostId())) {
                throw new IllegalArgumentException("Comment does not exist in this post!");
            }
            // Retrieve the parent comment
            Comment parentComment = commentRepository.findById(commentDto.getParentCommentId())
                    .orElseThrow((() -> new IllegalArgumentException("Parent comment not found")));
            comment.setParentCommentId(parentComment.getId());
        }
        commentRepository.save(comment);
        if (commentDto.getParentCommentId() == null) {
            notificationService.createU2UNotification(
                    post.getUser().getId(),
                    userId,
                    Enums.NotificationType.COMMENT_ON_POST.getValue(),
                    "User " + user.getUserName() + " has commented on your post."
            );
        } else {
            notificationService.createU2UNotification(
                    post.getUser().getId(),
                    userId,
                    Enums.NotificationType.REPLY_COMMENT.getValue(),
                    "User " + user.getUserName() + " has replied your comment."
            );
        }
        return apiResponseUtils.success(new CommentDto(comment), "Create new comment successfully!");
    }

    @Override
    public ApiResponse<List<CommentDto>> getAllCommentOfUser(Long userId) {
        User user = userService.getUserById(userId);
        List<Comment> comments = commentRepository.findCommentsExistByUserIdOrderByCreatedAtDesc(user.getId());
        comments.forEach(comment ->
                comment.setCommentmedia(comment.getCommentmedia().stream()
                        .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()))
        );
        return apiResponseUtils.success(comments.stream().map(CommentDto::new).collect(Collectors.toList()), "Get all comments successfully!");
    }

    /**
     * Retrieves all comments for a specific post while maintaining a parent-child structure.
     * Only users with the appropriate visibility permissions can access the comments.
     *
     * @param userId The ID of the user requesting the comments.
     * @param postId The ID of the post for which comments are retrieved.
     * @return ApiResponse containing a list of CommentDto objects representing the post's comments.
     * @throws IllegalArgumentException if the user does not have permission to view comments on the post.
     */
    @Override
    public ApiResponse<List<CommentDto>> getCommentOfPost(Long userId, String postId) {
        User user = userService.getUserById(userId);
        Post post = getPostById(postId);
        // Check if the user has permission to view the comments on this post
        if (validationService.hasNotPermissionToAction(user, post, Enums.VisibilityLevel.fromValue(post.getVisibilityLevel()))) {
            throw new IllegalArgumentException("You do not have permission to comment on this post!");
        }
        List<Comment> allComments = commentRepository.findParentsCommentsExistByPostIdOrderByCreatedAtDesc(postId);
        // Map to store CommentDto objects by their ID for easy lookup
        Map<String, CommentDto> commentMap = new HashMap<>();
        // Iterate over the list of comments to populate the map
        for (Comment comment : allComments) {
            comment.setCommentmedia(comment.getCommentmedia().stream()
                    .filter(cm -> !cm.getIsDelete()).collect(Collectors.toList()));
            commentMap.put(comment.getId(), new CommentDto(comment));
        }
        List<CommentDto> result = new ArrayList<>();
        // Organize comments into a parent-child hierarchy
        for (Comment comment : allComments) {
            CommentDto commentDto = commentMap.get(comment.getId());
            if (commentDto.getParentCommentId() != null) {
                CommentDto parentComment = commentMap.get(commentDto.getParentCommentId());
                if (parentComment != null) {
                    parentComment.getReplies().add(commentDto);
                }
            } else {
                result.add(commentDto);
            }
        }
        return apiResponseUtils.success(result, "Get all comments of post successfully!");
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteComment(Long userId, String commentId) {
        Comment comment = getCommentById(commentId);
        if (comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment is deleted");
        }
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        comment.setContent("Deleted comment!");
        comment.setIsDelete(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentMediaService.deleteCommentMediaOfComment(commentId);
        commentRepository.save(comment);
        return apiResponseUtils.success(null, "Comment deleted successfully!");
    }

    @Transactional
    @Override
    public void deleteCommentOfPost(String postId) {
        List<Comment> comments = commentRepository.findAllCommentsExistByPostId(postId);
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
        if (commentDto.getContent().isBlank()) {
            throw new IllegalArgumentException("Please enter content of the comment!");
        }
        Comment comment = getCommentById(commentId);
        if (comment.getIsDelete()) {
            throw new IllegalArgumentException("Comment is deleted");
        }
        if (!comment.getUser().getId().equals(userId)) {
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
