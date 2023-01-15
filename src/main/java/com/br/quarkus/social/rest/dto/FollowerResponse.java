package com.br.quarkus.social.rest.dto;

import com.br.quarkus.social.domain.model.Follower;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerResponse {
    private Long id;
    private String name;


    public static FollowerResponse fromModel(Follower follower) {
        return FollowerResponse.builder()
                .id(follower.getId())
                .name(follower.getFollower().getName())
                .build();
    }
}
