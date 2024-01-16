package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.TicketCreateDTO;
import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.exception.TicketDoesNotExistException;
import artgallery.hsboxoffice.feign.ExhibitionClient;
import artgallery.hsboxoffice.model.TicketEntity;
import artgallery.hsboxoffice.repository.TicketRepository;
import artgallery.hsboxoffice.configuration.ServerUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

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

  public Mono<TicketDTO> createTicket(TicketCreateDTO ticketDto, ServerUserDetails userDetails) {
      return exhibitionClient.getExhibitionById(ticketDto.getExhibition(),
              userDetails.getIdAsString(), userDetails.getUsername(), userDetails.getAuthoritiesAsString())
          .flatMap(exhibitionDTO -> {
            TicketEntity ticketEntity = mapToTicketEntity(ticketDto);
            ticketEntity.setExhibition(exhibitionDTO.getId());
            return ticketRepository.save(ticketEntity);
          })
          .map(this::mapToTicketDto);
    }

  public Mono<TicketDTO> updateTicket(long id, TicketCreateDTO ticketDto, ServerUserDetails userDetails) {
    return exhibitionClient.getExhibitionById(ticketDto.getExhibition(),
        userDetails.getIdAsString(), userDetails.getUsername(), userDetails.getAuthoritiesAsString())
        .flatMap(exhibitionDTO -> ticketRepository.findById(id)
                  .switchIfEmpty(Mono.error(new TicketDoesNotExistException(id)))
                  .flatMap(ticketEntity -> {
                    ticketEntity.setDescription(ticketDto.getDescription());
                    ticketEntity.setExhibition(exhibitionDTO.getId());
                    ticketEntity.setPrice(ticketDto.getPrice());
                    return ticketRepository.save(ticketEntity);
                  })
        ).map(this::mapToTicketDto);
  }

  public Mono<Void> deleteTicket(long id) {
    return ticketRepository.deleteById(id);
  }

  public Mono<Void> deleteTicketsByExhibitionId(long id) {
      return ticketRepository.deleteAllByExhibition(id);
  }

  private TicketEntity mapToTicketEntity(TicketCreateDTO ticketDto) {
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
