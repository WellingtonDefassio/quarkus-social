package com.br.quarkus.social.rest;

import com.br.quarkus.social.domain.model.Post;
import com.br.quarkus.social.domain.model.User;
import com.br.quarkus.social.domain.repository.FollowerRepository;
import com.br.quarkus.social.domain.repository.PostRepository;
import com.br.quarkus.social.domain.repository.UserRepository;
import com.br.quarkus.social.rest.dto.CreatePostRequest;
import com.br.quarkus.social.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("users/{userId}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;
    private Validator validator;


    @Inject
    public PostResource(UserRepository userRepository, Validator validator, PostRepository postRepository, FollowerRepository followerRepository){
        this.userRepository = userRepository;
        this.validator = validator;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response createPost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        Optional<User> optionalUser = userRepository.findByIdOptional(userId);
        if(optionalUser.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = Post.builder()
                .text(postRequest.getText())
                .userId(optionalUser.get())
                .build();

        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }
    @GET
    public Response listPost(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        Optional<User> optionalUser = userRepository.findByIdOptional(userId);
        Optional<User> optionalFollower = userRepository.findByIdOptional(followerId);
        if(optionalUser.isEmpty() || optionalFollower.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
       if(!followerRepository.follows(optionalUser.get(), optionalFollower.get())) {
           return Response.status(Response.Status.FORBIDDEN).build();
       }

        List<PostResponse> postResponseList = postRepository.find("userId", Sort.by("timestamp", Sort.Direction.Descending), optionalUser.get())
                .list().stream().map(PostResponse::fromModel).collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(postResponseList).build();
    }


}
