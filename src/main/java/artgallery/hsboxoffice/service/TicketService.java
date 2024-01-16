package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.configuration.ServerUserDetails;
import artgallery.hsboxoffice.dto.TicketCreateDTO;
import artgallery.hsboxoffice.dto.TicketDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketService {
  Flux<TicketDTO> getAllTickets(Pageable pageable);

  Mono<TicketDTO> getTicketById(Long id);

  Mono<TicketDTO> createTicket(TicketCreateDTO ticketDto, ServerUserDetails userDetails);

  Mono<TicketDTO> updateTicket(long id, TicketCreateDTO ticketDto, ServerUserDetails userDetails);

  Mono<Void> deleteTicket(long id);

  Mono<Void> deleteTicketsByExhibitionId(long id);
}
