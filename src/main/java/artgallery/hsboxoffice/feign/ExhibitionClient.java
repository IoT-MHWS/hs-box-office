package artgallery.hsboxoffice.feign;

import artgallery.hsboxoffice.dto.ExhibitionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
@Component
@ReactiveFeignClient(name="cms", path="/api/v1/exhibitions")
public interface ExhibitionClient {
    @GetMapping("/{id}")
    Mono<ExhibitionDTO> getExhibitionById(@PathVariable("id") Long id,
                                           @RequestHeader("X-User-Id") String userId,
                                           @RequestHeader("X-User-Name") String userName,
                                           @RequestHeader("X-User-Authorities") String userAuthorities);

}