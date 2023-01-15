package com.br.quarkus.social.rest;

import com.br.quarkus.social.domain.model.User;
import com.br.quarkus.social.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User(userRequest.getName(), userRequest.getAge());
        user.persist();

        return Response.ok(userRequest).build();
    }
    @GET
    public Response listAllUsers() {
        PanacheQuery<PanacheEntityBase> query = User.findAll();

        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        boolean isDeleted = User.deleteById(id);
        if(!isDeleted){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userRequest) {
        Optional<User> optionalUser = User.findByIdOptional(id);
      if(optionalUser.isPresent()) {
          User user = optionalUser.get();
          user.setName(userRequest.getName());
          user.setAge(userRequest.getAge());
          return Response.ok(user).build();
      }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
