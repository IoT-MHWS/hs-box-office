package artgallery.hsboxoffice.controller;

import artgallery.hsboxoffice.dto.OrderDTO;
import artgallery.hsboxoffice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/")
    public Mono<ResponseEntity<?>> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        validateSizeOfPage(size);
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable)
                .collectList()
                .map(orderList -> !orderList.isEmpty() ?
                        ResponseEntity.ok().body(orderList) :
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getOrderById(@PathVariable("id") long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok().body(order));
    }

    @PostMapping("/")
    public Mono<ResponseEntity<?>> createOrder(@RequestBody OrderDTO orderDTO) {
        validateOrderDto(orderDTO);
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
    public Mono<ResponseEntity<?>> updateOrder(@PathVariable("id") long id, @RequestBody OrderDTO orderDTO) {
        validateOrderDto(orderDTO);
        return orderService.updateOrder(id, orderDTO)
                .map(updatedOrder-> ResponseEntity.ok().body(updatedOrder));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<?>> deleteOrder(@PathVariable("id") long id) {
        return orderService.deleteOrder(id)
                .map(deleted -> ResponseEntity.noContent().build());
    }

    private void validateSizeOfPage(int size) {
        if (size > 50) {
            throw new IllegalArgumentException("Size of page must be <= 50");
        }
    }

    private void validateOrderDto(OrderDTO orderDTO) {
        if (orderDTO == null) {
            throw new IllegalArgumentException("Order is not set");
        }
        if (orderDTO.getDate() == null) {
            throw new IllegalArgumentException("Invalid date value");
        }
        if (orderDTO.getLogin() == null) {
            throw new IllegalArgumentException("Invalid user login value");
        }
    }
}
