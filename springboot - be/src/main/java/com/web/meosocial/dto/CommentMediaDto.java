package com.web.meosocial.dto;

import com.web.meosocial.domain.CommentMedia;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentMediaDto {
    private Long id;
    private Long commentId;
    private String mediaType;
    private String mediaUrl;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private Long mediaSize;
    private Integer duration;

    // Constructor to map from CommentMedia entity to CommentMediaDto
    public CommentMediaDto(CommentMedia commentMedia) {
        if (commentMedia != null) {
            this.id = commentMedia.getId();
            this.commentId = commentMedia.getComment().getId();
            this.mediaType = commentMedia.getMediaType();
            this.mediaUrl = commentMedia.getMediaUrl();
            this.thumbnailUrl = commentMedia.getThumbnailUrl();
            this.createdAt = commentMedia.getCreatedAt();
            this.mediaSize = commentMedia.getMediaSize();
            this.duration = commentMedia.getDuration();
        }
    }
}
