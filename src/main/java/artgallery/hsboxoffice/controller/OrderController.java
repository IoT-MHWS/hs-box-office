package artgallery.hsboxoffice.controller;

import artgallery.hsboxoffice.dto.OrderCreateDTO;
import artgallery.hsboxoffice.service.OrderService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @GetMapping("/")
    public Mono<ResponseEntity<?>> getAllOrders(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                                @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable)
                .collectList()
                .map(orderList -> ResponseEntity.ok().body(orderList));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getOrderById(@Min(0) @PathVariable("id") long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok().body(order));
    }

    // NOTE: because admin role is required, it can set different login from its own
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        return orderService.createOrder(orderCreateDTO)
                .map(createdOrder -> ResponseEntity.status(HttpStatus.CREATED).body(createdOrder));
    }


    // NOTE: because admin role is required, it can set different login from its own
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> updateOrder(@Min(0) @PathVariable("id") long id,
                                               @RequestBody OrderCreateDTO orderCreateDTO) {
        return orderService.updateOrder(id, orderCreateDTO)
                .map(updatedOrder-> ResponseEntity.ok().body(updatedOrder));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<?>> deleteOrder(@Min(0) @PathVariable("id") long id) {
        return orderService.deleteOrder(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }
}
