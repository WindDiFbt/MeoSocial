package com.web.meosocial.dto.post;

import com.web.meosocial.domain.post.SavedPost;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SavedPostDto {
    private String id;
    private Long userId;
    private String postId;
    private LocalDateTime savedAt;

    // Constructor to map from SavedPost entity to SavedPostDto
    public SavedPostDto(SavedPost savedPost) {
        if (savedPost != null) {
            this.id = savedPost.getId();
            this.userId = savedPost.getUser().getId();
            this.postId = savedPost.getPost().getId();
            this.savedAt = savedPost.getSavedAt();
        }
    }
}
