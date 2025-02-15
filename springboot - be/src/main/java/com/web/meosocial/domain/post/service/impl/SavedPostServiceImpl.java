package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.domain.post.dto.SavedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.model.SavedPost;
import com.web.meosocial.domain.post.repository.SavedPostRepository;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.post.service.SavedPostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SavedPostServiceImpl implements SavedPostService {
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    /**
     * Retrieves all posts saved by a specific user.
     *
     * @param userId The ID of the user whose saved posts are being retrieved.
     * @return {@code ApiResponseDto<List<SavedPostDto>>} A response containing a list of saved posts.
     */
    @Override
    public ApiResponseDto<List<SavedPostDto>> getAllPostsSaved(Long userId) {
        List<SavedPostDto> list = savedPostRepository.findByUserId(userId)
                .stream()
                .map(SavedPostDto::new)
                .toList();
        return apiResponseUtils.success(list, "Get all saved posts successfully!");
    }

    /**
     * Saves a post for a user. If the post was previously saved and deleted, it will be reactivated.
     *
     * @param userId The ID of the user saving the post.
     * @param postId The ID of the post to be saved.
     * @return {@code ApiResponseDto<SavedPostDto>} A response containing the saved post details.
     * @throws IllegalArgumentException If the post is already saved and not deleted.
     */
    @Transactional
    @Override
    public ApiResponseDto<SavedPostDto> savePost(Long userId, String postId) {
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(userId, postId);
        if (savedPost != null) {
            // If the post was deleted, restore it
            if (savedPost.getIsDelete()) {
                savedPost.setIsDelete(false);
                savedPost.setSavedAt(LocalDateTime.now());
                savedPostRepository.save(savedPost);
                return apiResponseUtils.success(new SavedPostDto(savedPost), "Saved post successfully!");
            } else {
                throw new IllegalArgumentException("Post already saved!");
            }
        }
        // Save the new post
        SavedPost newSavePost = new SavedPost();
        User user = userService.getUserById(userId);
        Post post = postService.getPostById(postId);
        newSavePost.setId(UUID.randomUUID().toString());
        newSavePost.setUser(user);
        newSavePost.setPost(post);
        newSavePost.setSavedAt(LocalDateTime.now());
        newSavePost.setIsDelete(false);
        savedPostRepository.save(newSavePost);
        return ApiResponseDto.<SavedPostDto>builder()
                .status(String.valueOf(HttpStatus.OK))
                .message(List.of("Saved post successfully!"))
                .response(new SavedPostDto(newSavePost))
                .build();
    }

    /**
     * Removes a saved post for a specific user.
     *
     * @param userId The ID of the user who wants to remove the saved post.
     * @param savedPostId The ID of the saved post to be deleted.
     * @return {@code ApiResponseDto<Void>} A response indicating the success of the operation.
     * @throws IllegalArgumentException If the saved post is not found or has already been removed.
     * @throws UnauthorizedException If the user is not authorized to delete this post.
     */
    @Transactional
    @Override
    public ApiResponseDto<Void> deleteSavedPost(Long userId, String savedPostId) {
        SavedPost savedPost = savedPostRepository.findById(savedPostId).orElseThrow(() -> new IllegalArgumentException("Saved Post not found"));
        if (!savedPost.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to remove this post");
        }
        if (savedPost.getIsDelete()) {
            throw new IllegalArgumentException("Post already removed!");
        }
        savedPost.setIsDelete(true);
        savedPostRepository.save(savedPost);
        return apiResponseUtils.success(null, "Removed post successfully!");
    }
}
