package com.web.meosocial.domain.post;

import com.web.meosocial.dto.post.PostMediaDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "postmedia")
public class PostMedia {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Lob
    @Column(name = "media_type")
    private Integer mediaType;

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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public PostMedia(PostMediaDto postMediaDto) {
        if (postMediaDto != null) {
            this.id = postMediaDto.getId();
            this.mediaType = postMediaDto.getMediaType();
            this.mediaUrl = postMediaDto.getMediaUrl();
            this.thumbnailUrl = postMediaDto.getThumbnailUrl();
            this.createdAt = postMediaDto.getCreatedAt();
            this.mediaSize = postMediaDto.getMediaSize();
            this.duration = postMediaDto.getDuration();
//            if (postMediaDto.getPostId() != null) {
//                this.post = new Post();
//                this.post.setId(postMediaDto.getPostId());
//            }
            this.isDeleted = postMediaDto.getIsDeleted();
        }
    }
}