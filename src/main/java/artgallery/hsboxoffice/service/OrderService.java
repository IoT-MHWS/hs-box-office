package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.OrderDTO;
import artgallery.hsboxoffice.exception.OrderDoesNotExistException;
import artgallery.hsboxoffice.model.OrderEntity;
import artgallery.hsboxoffice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Flux<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAllBy(pageable)
                .map(this::mapToOrderDto);
    }

    public Mono<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderDoesNotExistException(id)))
                .map(this::mapToOrderDto);
    }

    //TODO rewrite for login field + map
    public Mono<OrderDTO> createOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = this.mapToOrderEntity(orderDTO);
        return orderRepository.save(orderEntity)
                .map(this::mapToOrderDto);
    }

    public Mono<OrderDTO> updateOrder(long id, OrderDTO orderDTO) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderDoesNotExistException(id)))
                .flatMap(existingOrder -> {
                    existingOrder.setDate(orderDTO.getDate());
                    existingOrder.setLogin(orderDTO.getLogin());
                    return orderRepository.save(existingOrder);
                })
                .map(this::mapToOrderDto);
    }

    public Mono<Void> deleteOrder(long id) {
        return orderRepository.deleteById(id);
    }

    private OrderEntity mapToOrderEntity(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(orderDTO.getDate());
        orderEntity.setLogin(orderDTO.getLogin());
        return orderEntity;
    }

    private OrderDTO mapToOrderDto(OrderEntity orderEntity) {
        return new OrderDTO(orderEntity.getId(),
                orderEntity.getDate(), orderEntity.getLogin());
    }

}

