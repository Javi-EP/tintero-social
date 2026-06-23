package cl.javiep.reviewservice.service;

import cl.javiep.reviewservice.controller.ReviewController;
import cl.javiep.reviewservice.dto.ReviewResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReviewLinkAssembler {

    public EntityModel<ReviewResponseDTO> toModel(ReviewResponseDTO review) {
        EntityModel<ReviewResponseDTO> model = EntityModel.of(review);

        model.add(linkTo(methodOn(ReviewController.class)
                .getByBook(review.getBookId())).withRel("book-reviews"));

        model.add(linkTo(methodOn(ReviewController.class)
                .getByUser(review.getUserId())).withRel("user-reviews"));

        model.add(linkTo(methodOn(ReviewController.class)
                .update(review.getId(), null, null)).withRel("update"));

        model.add(linkTo(methodOn(ReviewController.class)
                .delete(review.getId(), null)).withRel("delete"));

        model.add(linkTo(methodOn(ReviewController.class)
                .vote(review.getId(), null)).withRel("vote"));

        return model;
    }

    public CollectionModel<EntityModel<ReviewResponseDTO>> toCollectionModel(
            java.util.List<EntityModel<ReviewResponseDTO>> reviews) {
        CollectionModel<EntityModel<ReviewResponseDTO>> collection = CollectionModel.of(reviews);
        if (!reviews.isEmpty()) {
            Long bookId = reviews.getFirst().getContent().getBookId();
            collection.add(linkTo(methodOn(ReviewController.class)
                    .getByBook(bookId)).withSelfRel());
        }
        return collection;
    }
}
