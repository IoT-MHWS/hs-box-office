package artgallery.hsboxoffice.repository;

import artgallery.hsboxoffice.model.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderRepository extends R2dbcRepository<OrderEntity, Long> {
    Flux<OrderEntity> findAllByIdNotNull();
    Mono<Void> deleteById(Long id);
    Flux<OrderEntity> findAllBy(Pageable pageable);
}
