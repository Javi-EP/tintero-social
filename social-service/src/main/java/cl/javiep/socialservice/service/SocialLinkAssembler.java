package cl.javiep.socialservice.service;

import cl.javiep.socialservice.controller.SocialController;
import cl.javiep.socialservice.dto.FollowResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SocialLinkAssembler {

    public EntityModel<FollowResponseDTO> toModel(FollowResponseDTO follow) {
        EntityModel<FollowResponseDTO> model = EntityModel.of(follow);

        model.add(linkTo(methodOn(SocialController.class)
                .getFollowers(follow.getFollowedId())).withRel("followers"));

        model.add(linkTo(methodOn(SocialController.class)
                .getFollowing(follow.getFollowerId())).withRel("following"));

        model.add(linkTo(methodOn(SocialController.class)
                .getStats(follow.getFollowedId())).withRel("stats"));

        return model;
    }

    public CollectionModel<EntityModel<FollowResponseDTO>> toCollectionModel(
            java.util.List<EntityModel<FollowResponseDTO>> follows) {
        CollectionModel<EntityModel<FollowResponseDTO>> collection = CollectionModel.of(follows);
        collection.add(linkTo(methodOn(SocialController.class)
                .getFollowers(0L)).withRel("self"));
        return collection;
    }
}
