package artgallery.hsboxoffice.feign;

import artgallery.hsboxoffice.dto.ExhibitionFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("cms-service")
public interface ExhibitionClient {

    // test url
    @GetMapping("/server/api/v1/exhibitions/check/{id}")
    ExhibitionFeignDTO getExhibitionById(@PathVariable("id") Long id);
}
