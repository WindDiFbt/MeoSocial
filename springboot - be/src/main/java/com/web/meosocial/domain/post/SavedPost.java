package com.web.meosocial.domain.post;

import com.web.meosocial.domain.user.User;
import com.web.meosocial.dto.post.SavedPostDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "savedpost")
public class SavedPost {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @Column(name = "is_delete")
    private Boolean isDelete;

    // Constructor to convert SavedPostDto to SavedPost entity
    public SavedPost(SavedPostDto savedPostDto) {
        if (savedPostDto != null) {
            this.id = savedPostDto.getId();
            this.user = new User();
            this.user.setId(savedPostDto.getUserId());
            this.post = new Post();
            this.post.setId(savedPostDto.getPostId());
            this.savedAt = savedPostDto.getSavedAt();
        }
    }
}