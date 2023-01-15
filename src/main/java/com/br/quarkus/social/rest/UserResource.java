package com.br.quarkus.social.rest;

import com.br.quarkus.social.domain.model.User;
import com.br.quarkus.social.domain.repository.UserRepository;
import com.br.quarkus.social.rest.dto.CreateUserRequest;
import com.br.quarkus.social.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository userRepository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()) {
            ResponseError fromValidation = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(fromValidation).build();
        }
        User user = User.builder()
                .name(userRequest.getName())
                .age(userRequest.getAge())
                .build();

        userRepository.persist(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
    }
    @GET
    public Response listAllUsers() {
        PanacheQuery<User> allUsers = userRepository.findAll();

        return Response.ok(allUsers.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        boolean isDeleted = userRepository.deleteById(id);
        if(!isDeleted){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByIdOptional(id);
      if(optionalUser.isPresent()) {
          User user = optionalUser.get();
          user.setName(userRequest.getName());
          user.setAge(userRequest.getAge());
          return Response.ok(user).build();
      }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
