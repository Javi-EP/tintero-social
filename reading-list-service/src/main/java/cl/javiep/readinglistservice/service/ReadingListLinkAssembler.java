package cl.javiep.readinglistservice.service;

import cl.javiep.readinglistservice.controller.ReadingListController;
import cl.javiep.readinglistservice.dto.ListResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReadingListLinkAssembler {

    public EntityModel<ListResponseDTO> toModel(ListResponseDTO list) {
        EntityModel<ListResponseDTO> model = EntityModel.of(list);

        model.add(linkTo(methodOn(ReadingListController.class)
                .getById(list.getId())).withSelfRel());

        model.add(linkTo(methodOn(ReadingListController.class)
                .deleteList(list.getId())).withRel("delete"));

        model.add(linkTo(methodOn(ReadingListController.class)
                .addBook(list.getId(), null)).withRel("add-book"));

        if (list.getItems() != null && !list.getItems().isEmpty()) {
            list.getItems().forEach(item -> {
                if (item.getBookId() != null) {
                    model.add(linkTo(methodOn(ReadingListController.class)
                            .updateProgress(list.getId(), item.getBookId(), null))
                            .withRel("progress-" + item.getBookId()));
                }
            });
        }

        model.add(linkTo(methodOn(ReadingListController.class)
                .getByUser(list.getUserId())).withRel("user-lists"));

        return model;
    }

    public CollectionModel<EntityModel<ListResponseDTO>> toCollectionModel(
            java.util.List<EntityModel<ListResponseDTO>> lists) {
        CollectionModel<EntityModel<ListResponseDTO>> collection = CollectionModel.of(lists);
        if (!lists.isEmpty()) {
            Long userId = lists.getFirst().getContent().getUserId();
            collection.add(linkTo(methodOn(ReadingListController.class)
                    .getByUser(userId)).withSelfRel());
        }
        return collection;
    }
}
