package com.web.meosocial.domain;

import com.web.meosocial.dto.SavedPostDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "savedpost")
public class SavedPost {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.web.meosocial.domain.User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    // Constructor to convert SavedPostDto to SavedPost entity
    public SavedPost(SavedPostDto savedPostDto) {
        if (savedPostDto != null) {
            this.id = savedPostDto.getId();
            this.user = new com.web.meosocial.domain.User();
            this.user.setId(savedPostDto.getUserId());
            this.post = new Post();
            this.post.setId(savedPostDto.getPostId());
            this.savedAt = savedPostDto.getSavedAt();
        }
    }
}