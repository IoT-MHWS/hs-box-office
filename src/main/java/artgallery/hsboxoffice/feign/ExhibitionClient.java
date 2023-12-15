package artgallery.hsboxoffice.feign;

import artgallery.hsboxoffice.dto.ExhibitionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("hs-cms")
public interface ExhibitionClient {

    @GetMapping("/api/v1/exhibitions/{id}")
    ResponseEntity<ExhibitionDTO> getFeidn(@PathVariable("id") Long id,
                            @RequestHeader("X-User-Id") String value1,
                            @RequestHeader("X-User-Name") String value2,
                            @RequestHeader("X-User-Authorities") String value3);

}