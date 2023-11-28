package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.ExhibitionFeignDTO;
import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.exception.TicketDoesNotExistException;
import artgallery.hsboxoffice.feign.ExhibitionClient;
import artgallery.hsboxoffice.model.TicketEntity;
import artgallery.hsboxoffice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ExhibitionClient exhibitionClient;

    public Flux<TicketDTO> getAllTickets(Pageable pageable) {
        return ticketRepository.findAllBy(pageable)
                .map(this::mapToTicketDto);
    }

    public Mono<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .switchIfEmpty(Mono.error(new TicketDoesNotExistException(id)))
                .map(this::mapToTicketDto);
    }

    public Mono<TicketDTO> createTicket(TicketDTO ticketDto) {
        TicketEntity ticketEntity = this.mapToTicketEntity(ticketDto);

        // use feign
        long exhibitionId = ticketDto.getExhibition();
        ExhibitionFeignDTO exhibitionFeignDTO = exhibitionClient.getExhibitionById(exhibitionId);
        ticketEntity.setExhibition(exhibitionFeignDTO.getExhibitionId());

        return ticketRepository.save(ticketEntity)
                .map(this::mapToTicketDto);
    }

    public Mono<TicketDTO> updateTicket(long id, TicketDTO ticketDto) {
        // use feign
        long exhibitionId = ticketDto.getExhibition();
        ExhibitionFeignDTO exhibitionFeignDTO = exhibitionClient.getExhibitionById(exhibitionId);

        return ticketRepository.findById(id)
                .switchIfEmpty(Mono.error(new TicketDoesNotExistException(id)))
                .flatMap(existingTicket -> {
                    existingTicket.setDescription(ticketDto.getDescription());
                    existingTicket.setPrice(ticketDto.getPrice());
//                    existingTicket.setExhibition(ticketDto.getExhibition());
                    existingTicket.setExhibition(exhibitionFeignDTO.getExhibitionId());

                    return ticketRepository.save(existingTicket);
                })
                .map(this::mapToTicketDto);
    }

    public Mono<Void> deleteTicket(long id) {
        return ticketRepository.deleteById(id);
    }

    private TicketEntity mapToTicketEntity(TicketDTO ticketDto) {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setDescription(ticketDto.getDescription());
        ticketEntity.setPrice(ticketDto.getPrice());
        ticketEntity.setExhibition(ticketDto.getExhibition());
        return ticketEntity;
    }

    private TicketDTO mapToTicketDto(TicketEntity ticketEntity) {
        return new TicketDTO(ticketEntity.getId(), ticketEntity.getDescription(),
                ticketEntity.getPrice(), ticketEntity.getExhibition());
    }
}
