package com.web.meosocial.dto;

import com.web.meosocial.domain.PostMedia;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostMediaDto {
    private Long id;
    private Long postId;
    private Integer mediaType;
    private String mediaUrl;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private Long mediaSize;
    private Integer duration;

    // Constructor to map from PostMedia entity to PostMediaDto
    public PostMediaDto(PostMedia postMedia) {
        if (postMedia != null) {
            this.id = postMedia.getId();
            this.postId = postMedia.getPost() != null ? postMedia.getPost().getId() : null;
            this.mediaType = postMedia.getMediaType();
            this.mediaUrl = postMedia.getMediaUrl();
            this.thumbnailUrl = postMedia.getThumbnailUrl();
            this.createdAt = postMedia.getCreatedAt();
            this.mediaSize = postMedia.getMediaSize();
            this.duration = postMedia.getDuration();
        }
    }
}
