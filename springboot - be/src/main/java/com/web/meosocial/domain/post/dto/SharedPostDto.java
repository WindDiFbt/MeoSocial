package com.web.meosocial.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SharedPostDto {
    private String content;
    private Integer visibilityLevel;
    private String sharedPostId;
}
