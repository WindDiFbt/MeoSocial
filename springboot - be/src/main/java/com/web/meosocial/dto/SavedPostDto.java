package com.web.meosocial.dto;

import com.web.meosocial.domain.SavedPost;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SavedPostDto {
    private Long id;
    private Long userId;
    private Long postId;
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
