package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.OrderCreateDTO;
import artgallery.hsboxoffice.dto.OrderDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
  Flux<OrderDTO> getAllOrders(Pageable pageable);

  Mono<OrderDTO> getOrderById(Long id);

  Mono<OrderDTO> createOrder(OrderCreateDTO orderCreateDTO);

  Mono<OrderDTO> updateOrder(long id, OrderCreateDTO orderCreateDTO);

  Mono<Void> deleteOrder(long id);
}
