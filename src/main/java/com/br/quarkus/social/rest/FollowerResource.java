package com.br.quarkus.social.rest;

import com.br.quarkus.social.domain.model.Follower;
import com.br.quarkus.social.domain.model.User;
import com.br.quarkus.social.domain.repository.FollowerRepository;
import com.br.quarkus.social.domain.repository.UserRepository;
import com.br.quarkus.social.rest.dto.FollowerPerUserResponse;
import com.br.quarkus.social.rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
  private FollowerRepository followerRepository;
  private UserRepository userRepository;
    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request) {
        Optional<User> userOptional = userRepository.findByIdOptional(userId);
        Optional<User> followOptional = userRepository.findByIdOptional(request.getFollowerId());
        if(validateUsers(userOptional, followOptional)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!followerRepository.follows(userOptional.get(),followOptional.get())) {
             Follower followers = Follower.builder()
                    .user(userOptional.get())
                    .follower(followOptional.get())
                    .build();
            followerRepository.persist(followers);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        if(userRepository.findByIdOptional(userId).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Follower> followerList = followerRepository.findByUser(userId);
        return Response.status(Response.Status.OK).entity(FollowerPerUserResponse.fromModelList(followerList)).build();

    }
    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId,@QueryParam("unfollowId") Long unfollowId){
        Optional<User> userOptional = userRepository.findByIdOptional(userId);
        Optional<User> followOptional = userRepository.findByIdOptional(unfollowId);
        if(validateUsers(userOptional, followOptional)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(followerRepository.follows(userOptional.get(),followOptional.get())) {
            followerRepository.deleteByUserAndFollower(userId, unfollowId);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }



    private Boolean validateUsers(Optional<User> userOptional, Optional<User> followOptional) {
        return !(userOptional.isPresent() && followOptional.isPresent() && !Objects.equals(followOptional.get().getId(), userOptional.get().getId()));
    }

}
