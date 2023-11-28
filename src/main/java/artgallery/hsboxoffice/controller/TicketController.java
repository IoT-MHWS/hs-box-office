package artgallery.hsboxoffice.controller;

import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public Mono<ResponseEntity<?>> getAllTickets(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        validateSizeOfPage(size);
        Pageable pageable = PageRequest.of(page, size);
        return ticketService.getAllTickets(pageable)
                .collectList()
                .map(ticketList -> !ticketList.isEmpty() ?
                        ResponseEntity.ok().body(ticketList) :
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getTicketById(@PathVariable("id") long id) {
        return ticketService.getTicketById(id)
                    .map(ticket -> ResponseEntity.ok().body(ticket));

    }

    @PostMapping("/")
    public Mono<ResponseEntity<?>> createTicket(@RequestBody TicketDTO ticketDto) {
      validateTicketDto(ticketDto);
        return ticketService.createTicket(ticketDto)
                    .map(createdTicket -> ResponseEntity.status(HttpStatus.CREATED).body(createdTicket));

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> updateTicket(@PathVariable("id") long id, @RequestBody TicketDTO ticketDto) {
        validateTicketDto(ticketDto);
        return ticketService.updateTicket(id, ticketDto)
                    .map(updatedTicket -> ResponseEntity.ok().body(updatedTicket));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<?>> deleteTicket(@PathVariable("id") long id) {
        return ticketService.deleteTicket(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }

    private void validateSizeOfPage(int size) {
        if (size > 50) {
            throw new IllegalArgumentException("Size of page must be <= 50");
        }
    }

    private void validateTicketDto(TicketDTO ticketDto) {
        if (ticketDto == null) {
            throw new IllegalArgumentException("Ticket is not set");
        }
        if (ticketDto.getPrice() == null || ticketDto.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid price value");
        }
        if (ticketDto.getExhibition() == null || ticketDto.getExhibition() < 0) {
            throw new IllegalArgumentException("Invalid exhibition value");
        }
    }
}
