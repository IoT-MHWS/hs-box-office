package artgallery.hsboxoffice.repository;

import artgallery.hsboxoffice.model.TicketEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketRepository extends R2dbcRepository<TicketEntity, Long> {
    Flux<TicketEntity> findAllByIdNotNull();
    Mono<Void> deleteById(Long id);
    Flux<TicketEntity> findAllBy(Pageable pageable);
    Mono<Void> deleteAllByExhibition(Long id);
}
