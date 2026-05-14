package cl.javiep.statsservice.Client;

import cl.javiep.statsservice.dto.ReadingListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "reading-list-service",
        url = "http://localhost:8084"
)
public interface ReadingListClient {

    @GetMapping("/reading-list/user/{userId}")
    List<ReadingListDTO> getUserReadingList(
            @PathVariable Long userId);
}
