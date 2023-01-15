package com.br.quarkus.social.rest.dto;

import com.br.quarkus.social.domain.model.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {

    private String text;
    private LocalDateTime timestamp;

    public static PostResponse fromModel(Post post) {
        return PostResponse.builder()
                .text(post.getText())
                .timestamp(post.getTimestamp())
                .build();
    }

}


