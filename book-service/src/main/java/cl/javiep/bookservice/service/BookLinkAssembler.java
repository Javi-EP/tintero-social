package cl.javiep.bookservice.service;

import cl.javiep.bookservice.controller.BookController;
import cl.javiep.bookservice.dto.BookResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookLinkAssembler {

    public EntityModel<BookResponseDTO> toModel(BookResponseDTO book) {
        EntityModel<BookResponseDTO> model = EntityModel.of(book);

        model.add(linkTo(methodOn(BookController.class)
                .findById(book.getId())).withSelfRel());

        model.add(linkTo(methodOn(BookController.class)
                .list(null)).withRel("all"));

        model.add(linkTo(methodOn(BookController.class)
                .update(book.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(BookController.class)
                .delete(book.getId())).withRel("delete"));

        return model;
    }

    public CollectionModel<EntityModel<BookResponseDTO>> toCollectionModel(
            java.util.List<EntityModel<BookResponseDTO>> books) {
        CollectionModel<EntityModel<BookResponseDTO>> collection = CollectionModel.of(books);
        collection.add(linkTo(methodOn(BookController.class)
                .list(null)).withSelfRel());
        return collection;
    }
}
