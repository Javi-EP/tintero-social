package cl.javiep.userservice.service;

import cl.javiep.userservice.controller.UserController;
import cl.javiep.userservice.dto.UserResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkAssembler {

    public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {
        EntityModel<UserResponseDTO> model = EntityModel.of(user);

        model.add(linkTo(methodOn(UserController.class)
                .findById(user.getId())).withSelfRel());

        model.add(linkTo(methodOn(UserController.class)
                .findAll()).withRel("all"));

        model.add(linkTo(methodOn(UserController.class)
                .update(user.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(UserController.class)
                .delete(user.getId())).withRel("delete"));

        return model;
    }

    public CollectionModel<EntityModel<UserResponseDTO>> toCollectionModel(
            java.util.List<EntityModel<UserResponseDTO>> users) {
        CollectionModel<EntityModel<UserResponseDTO>> collection = CollectionModel.of(users);
        collection.add(linkTo(methodOn(UserController.class)
                .findAll()).withSelfRel());
        return collection;
    }
}
