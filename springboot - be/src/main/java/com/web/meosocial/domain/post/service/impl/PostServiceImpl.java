package com.web.meosocial.domain.post.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.comment.service.CommentService;
import com.web.meosocial.domain.notification.service.NotificationService;
import com.web.meosocial.domain.post.dto.ChangeVisibilityDto;
import com.web.meosocial.domain.post.dto.PostDto;
import com.web.meosocial.domain.post.dto.SharedPostDto;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.post.repository.PostRepository;
import com.web.meosocial.domain.post.service.PostMediaService;
import com.web.meosocial.domain.post.service.PostService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserRelationshipService;
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
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostMediaService postMediaService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRelationshipService userRelationshipService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;
    @Autowired
    private NotificationService notificationService;

    /**
     * Creates a new post.
     *
     * @param userId  The ID of the user creating the post.
     * @param postDto The DTO containing details of the new post.
     * @return ApiResponseDto&lt;PostDto&gt; The response containing the created post details.
     */
    @Transactional
    @Override
    public ApiResponse<PostDto> createNewPost(Long userId, PostDto postDto) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(userId);
        post.setUser(user);
        post.setIsSharedPost(false);
        post = savePostInformation(post, postDto.getContent(), postDto.getVisibilityLevel());
        return apiResponseUtils.success(new PostDto(post), "Post created successfully!");
    }

    /**
     * Saves post information, setting default values if necessary.
     *
     * @param post            The Post entity to be saved.
     * @param content         The content of the post.
     * @param visibilityLevel The visibility level of the post (default is PUBLIC if null).
     * @return Post The saved post entity.
     */
    private Post savePostInformation(Post post, String content, Integer visibilityLevel) {
        post.setContent(content);
        post.setVisibilityLevel(visibilityLevel != null ? visibilityLevel : Enums.VisibilityLevel.PUBLIC.getValue());
        post.setIsDelete(false);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    /**
     * Retrieves all posts of a user.
     *
     * @param userId The ID of the user whose posts are to be retrieved.
     * @return ApiResponseDto&lt;List&lt;PostDto&gt;> A response containing a list of the user's posts.
     */
    @Override
    public ApiResponse<List<PostDto>> getAllPostsOfUser(Long userId) {
        User user = userService.getUserById(userId);
        List<Post> posts = postRepository.findAllByUserId(user.getId());
        for (Post post : posts) {
            post.setPostmedia(post.getPostmedia().stream()
                    .filter(pm -> !pm.getIsDelete()).collect(Collectors.toList()));
        }
        return apiResponseUtils.success(posts.stream().map(PostDto::new).collect(Collectors.toList()), "All posts found!");
    }

    /**
     * Deletes a post and marks related shared posts as unavailable.
     *
     * @param userId The ID of the user requesting the deletion.
     * @param postId The ID of the post to be deleted.
     * @return ApiResponseDto&lt;Void&gt; A response indicating the deletion status.
     * @throws UnauthorizedException If the user is not authorized to delete the post.
     */
    @Transactional
    @Override
    public ApiResponse<Void> deletePost(Long userId, String postId) {
        Post post = getPostById(postId);
        List<Post> sharedPosts = postRepository.findAllBySharedPostId(post.getId());
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to delete this post");
        }
        post.setIsDelete(true);
        post.setDeletedAt(LocalDateTime.now());
        postMediaService.deletePostMediaOfPost(postId);
        commentService.deleteCommentOfPost(postId);
        sharedPosts.forEach(sharedPost ->
                sharedPost.setIsSharedPostAvailable(false));
        postRepository.save(post);
        postRepository.saveAll(sharedPosts);
        return apiResponseUtils.success(null, "Post deleted successfully!");
    }

    /**
     * Retrieves a post by its ID and filters out deleted media.
     *
     * @param postId The ID of the post to retrieve.
     * @return ApiResponseDto&lt;PostDto&gt; containing the requested post.
     */
    @Override
    public ApiResponse<PostDto> getPost(String postId) {
        Post post = getPostById(postId);
        post.setPostmedia(post.getPostmedia().stream()
                .filter(pm -> !pm.getIsDelete()).collect(Collectors.toList()));
        return apiResponseUtils.success(new PostDto(post), "Post found!");
    }

    /**
     * Changes the visibility level of a post.
     *
     * @param userId              The ID of the user requesting the visibility change.
     * @param changeVisibilityDto The DTO containing post ID and new visibility level.
     * @return ApiResponseDto&lt;PostDto&gt; containing the updated post.
     * @throws UnauthorizedException If the user is not authorized to modify the post.
     */
    @Transactional
    @Override
    public ApiResponse<PostDto> changeVisibilityLevel(Long userId, ChangeVisibilityDto changeVisibilityDto) {
        Post post = getPostById(changeVisibilityDto.getPostId());
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to continue action!");
        }
        post.setVisibilityLevel(changeVisibilityDto.getVisibility());
        post.setUpdatedAt(LocalDateTime.now());
        // If the post is not a shared post, update visibility of related shared posts
        if (!post.getIsSharedPost()) {
            List<Post> sharedPosts = postRepository.findAllBySharedPostId(post.getId());
            for (Post sharedPost : sharedPosts) {
                switch (Enums.VisibilityLevel.fromValue(changeVisibilityDto.getVisibility())) {
                    case PUBLIC:
                        sharedPost.setIsSharedPostAvailable(true);
                        break;
                    case PRIVATE:
                        sharedPost.setIsSharedPostAvailable(false);
                        break;
                    case FOLLOWER:
                        sharedPost.setIsSharedPostAvailable(userRelationshipService.IsUserFollow(sharedPost.getUser().getId(), userId));
                        break;
                    case FRIENDS:
                        sharedPost.setIsSharedPostAvailable(userRelationshipService.IsUserRelaMutualFollow(sharedPost.getUser().getId(), userId));
                        break;
                }
            }
            postRepository.saveAll(sharedPosts);
        }
        postRepository.save(post);
        return apiResponseUtils.success(new PostDto(post), "Post's visibility changed successfully!");
    }

    /**
     * Updates the content of an existing post.
     *
     * @param userId  The ID of the user making the request.
     * @param postId  The ID of the post to be updated.
     * @param postDto The DTO containing updated content.
     * @return ApiResponseDto&lt;PostDto&gt; containing the updated post details.
     * @throws UnauthorizedException If the user is not authorized to update the post.
     */
    @Transactional
    @Override
    public ApiResponse<PostDto> updatePost(Long userId, String postId, PostDto postDto) {
        Post post = getPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to continue action!");
        }
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
        return apiResponseUtils.success(new PostDto(post), "Post updated successfully!");
    }

    @Override
    public Post getPostById(String postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getIsDelete()) {
            throw new IllegalArgumentException("Post Not Found or deleted: " + postId);
        }
        return post;
    }

    /**
     * Allows a user to share an existing post, ensuring proper authorization and visibility level restrictions.
     *
     * @param userId        The ID of the user attempting to share the post.
     * @param sharedPostDto DTO containing details of the shared post, including the original post ID.
     * @return ApiResponseDto&lt;PostDto&gt; The response containing the shared post details.
     */
    @Transactional
    @Override
    public ApiResponse<PostDto> sharePost(Long userId, SharedPostDto sharedPostDto) {
        Post post = getPostById(sharedPostDto.getSharedPostId());
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to continue action!");
        }
        if (post.getIsDelete()) {
            throw new IllegalArgumentException("Shared Post is deleted!");
        }
        Enums.VisibilityLevel visibilityLevel = Enums.VisibilityLevel.fromValue(post.getVisibilityLevel());
        // If the post is private, sharing is not allowed
        if (visibilityLevel == Enums.VisibilityLevel.PRIVATE) {
            throw new IllegalArgumentException("Shared Post is private!");
        }
        // If the post is FRIENDS only, check if the user is a mutual friend
        if (visibilityLevel == Enums.VisibilityLevel.FRIENDS && !userRelationshipService.IsUserRelaMutualFollow(userId, post.getUser().getId())) {
            throw new IllegalArgumentException("You do not have permission to share this post!");
        }
        // If the post is FOLLOWER only, check if the user follows the original poster
        if (visibilityLevel == Enums.VisibilityLevel.FOLLOWER && !userRelationshipService.IsUserFollow(userId, post.getUser().getId())) {
            throw new IllegalArgumentException("You do not have permission to share this post!");
        }
        // Create and return the shared post
        return apiResponseUtils.success(createSharedPost(userId, sharedPostDto), "Shared Post created successfully!");
    }

    /**
     * Creates a new shared post based on the given {@link SharedPostDto}.
     *
     * @param userId        The ID of the user creating the shared post.
     * @param sharedPostDto The DTO containing details of the shared post.
     * @return A {@link PostDto} representing the newly created shared post.
     */
    private PostDto createSharedPost(Long userId, SharedPostDto sharedPostDto) {
        Post newPost = new Post();
        newPost.setId(UUID.randomUUID().toString());
        User user = userService.getUserById(userId);
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setIsSharedPost(true);
        newPost.setSharedPostId(sharedPostDto.getSharedPostId());
        newPost.setIsSharedPostAvailable(true);
        newPost = savePostInformation(newPost, sharedPostDto.getContent(), sharedPostDto.getVisibilityLevel());
        notificationService.createU2UNotification(
                getPostById(sharedPostDto.getSharedPostId()).getUser().getId(),
                userId,
                Enums.NotificationType.SHARE_POST.getValue(),
                "User " + user.getUserName() + " shared your post!"
        );
        return new PostDto(newPost);
    }
}
