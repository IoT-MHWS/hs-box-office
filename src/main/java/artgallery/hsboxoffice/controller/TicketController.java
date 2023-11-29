package artgallery.hsboxoffice.controller;

import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.service.TicketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private final TicketService ticketService;

//    @Autowired
//    public TicketController(TicketService ticketService){
//        this.ticketService = ticketService;
//    }

    @GetMapping("/")
    public Mono<ResponseEntity<?>> getAllTickets(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketService.getAllTickets(pageable)
                .collectList()
                .map(ticketList -> !ticketList.isEmpty() ?
                        ResponseEntity.ok().body(ticketList) :
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getTicketById(@Min(0) @PathVariable("id") long id) {
        return ticketService.getTicketById(id)
                    .map(ticket -> ResponseEntity.ok().body(ticket));

    }

    @PostMapping("/")
    public Mono<ResponseEntity<?>> createTicket(@Valid  @RequestBody TicketDTO ticketDto) {
        return ticketService.createTicket(ticketDto)
                    .map(createdTicket -> ResponseEntity.status(HttpStatus.CREATED).body(createdTicket));

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> updateTicket(@Min(0) @PathVariable("id") long id, @Valid @RequestBody TicketDTO ticketDto) {
        return ticketService.updateTicket(id, ticketDto)
                    .map(updatedTicket -> ResponseEntity.ok().body(updatedTicket));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<?>> deleteTicket(@Min(0) @PathVariable("id") long id) {
        return ticketService.deleteTicket(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }

}
