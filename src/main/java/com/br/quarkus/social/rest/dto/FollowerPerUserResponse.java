package com.br.quarkus.social.rest.dto;

import com.br.quarkus.social.domain.model.Follower;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FollowerPerUserResponse {
    private Integer followersCount;
    private List<FollowerResponse> content;


    public static FollowerPerUserResponse fromModelList(List<Follower> followerList) {
        return FollowerPerUserResponse.builder()
                .followersCount(followerList.size())
                .content(followerList.stream().map(FollowerResponse::fromModel)
                        .collect(Collectors.toList()))
                .build();
    }

}
