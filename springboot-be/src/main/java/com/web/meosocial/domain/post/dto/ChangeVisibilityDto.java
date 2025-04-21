package com.web.meosocial.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeVisibilityDto {
    private String postId;
    private Integer visibility;
}
