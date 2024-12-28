package com.web.meosocial.domain;

import com.web.meosocial.dto.CommentMediaDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "commentmedia")
public class CommentMedia {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private com.web.meosocial.domain.Comment comment;

    @Lob
    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "media_size")
    private Long mediaSize;

    @Column(name = "duration")
    private Integer duration;

    public CommentMedia(CommentMediaDto commentMediaDto) {
        if (commentMediaDto != null) {
            this.id = commentMediaDto.getId();
            this.mediaType = commentMediaDto.getMediaType();
            this.mediaUrl = commentMediaDto.getMediaUrl();
            this.thumbnailUrl = commentMediaDto.getThumbnailUrl();
            this.createdAt = commentMediaDto.getCreatedAt();
            this.mediaSize = commentMediaDto.getMediaSize();
            this.duration = commentMediaDto.getDuration();
            this.comment = new Comment();
            this.comment.setId(commentMediaDto.getCommentId());
        }
    }
}