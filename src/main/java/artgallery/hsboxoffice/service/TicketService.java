package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.ExhibitionDTO;
import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.exception.TicketDoesNotExistException;
import artgallery.hsboxoffice.feign.ExhibitionClient;
import artgallery.hsboxoffice.model.TicketEntity;
import artgallery.hsboxoffice.repository.TicketRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        long exhibitionId = ticketDto.getExhibition();
        try {
            ResponseEntity<ExhibitionDTO> response =
                    exhibitionClient.getExhibitionById(exhibitionId, "777", "from-feign-client", "ROLE_PUBLIC");
            HttpStatusCode status = response.getStatusCode();
            System.out.println("HTTP status code: " + status.value());
            if (status.value() == 200) {
                ticketEntity.setExhibition(exhibitionId);
            } else {
                ticketEntity.setExhibition(Long.valueOf(0));
            }
        } catch (FeignException.Unauthorized ex) {
            System.out.println("Unauthorized error: " + ex.getMessage());
            ticketEntity.setExhibition(Long.valueOf(0));
        }
        return ticketRepository.save(ticketEntity)
                .map(this::mapToTicketDto);
    }

    public Mono<TicketDTO> updateTicket(long id, TicketDTO ticketDto) {
        long exhibitionId = ticketDto.getExhibition();
        long prepareExhibitionId = Long.valueOf(0);
        try {
            ResponseEntity<ExhibitionDTO> response =
                    exhibitionClient.getExhibitionById(exhibitionId, "777", "from-feign-client", "ROLE_PUBLIC");
            HttpStatusCode status = response.getStatusCode();
            System.out.println("HTTP status code: " + status.value());
            if (status.value() == 200) {
                prepareExhibitionId = exhibitionId;
            }
        } catch (FeignException.Unauthorized ex) {
            System.out.println("Unauthorized error: " + ex.getMessage());

        }
        long finalPrepareExhibitionId = prepareExhibitionId;
        return ticketRepository.findById(id)
                .switchIfEmpty(Mono.error(new TicketDoesNotExistException(id)))
                .flatMap(existingTicket -> {
                    existingTicket.setDescription(ticketDto.getDescription());
                    existingTicket.setPrice(ticketDto.getPrice());
                    existingTicket.setExhibition(finalPrepareExhibitionId);
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
