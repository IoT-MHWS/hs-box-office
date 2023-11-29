package artgallery.hsboxoffice.controller;

import artgallery.hsboxoffice.dto.OrderDTO;
import artgallery.hsboxoffice.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .map(orderList -> !orderList.isEmpty() ?
                        ResponseEntity.ok().body(orderList) :
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getOrderById(@Min(0) @PathVariable("id") long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok().body(order));
    }

    @PostMapping("/")
    public Mono<ResponseEntity<?>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO)
                .map(createdOrder -> ResponseEntity.status(HttpStatus.CREATED).body(createdOrder));

    }

    /** ---  with auth  ---
     *
     *
    @PostMapping("/")
    public Mono<ResponseEntity<?>> createOrder(@RequestBody OrderDTO orderDTO,
                                               @RequestHeader(name = "x-user-login") String login) {
        validateOrderDto(orderDTO);
        return orderService.createOrder(orderDTO, userLogin)
                .map(createdOrder -> ResponseEntity.status(HttpStatus.CREATED).body(createdOrder));
    }
     **/

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> updateOrder(@Min(0) @PathVariable("id") long id, @Valid @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO)
                .map(updatedOrder-> ResponseEntity.ok().body(updatedOrder));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<?>> deleteOrder(@Min(0) @PathVariable("id") long id) {
        return orderService.deleteOrder(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }
}
