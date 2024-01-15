package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.OrderCreateDTO;
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
public class OrderServiceImpl implements OrderService {
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

    public Mono<OrderDTO> createOrder(OrderCreateDTO orderCreateDTO) {
        OrderEntity orderEntity = this.mapToOrderEntity(orderCreateDTO);
        return orderRepository.save(orderEntity)
                .map(this::mapToOrderDto);
    }

    public Mono<OrderDTO> updateOrder(long id, OrderCreateDTO orderCreateDTO) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderDoesNotExistException(id)))
                .flatMap(existingOrder -> {
                    existingOrder.setDate(orderCreateDTO.getDate());
                    existingOrder.setLogin(orderCreateDTO.getLogin());
                    return orderRepository.save(existingOrder);
                })
                .map(this::mapToOrderDto);
    }

    public Mono<Void> deleteOrder(long id) {
        return orderRepository.deleteById(id);
    }

    private OrderEntity mapToOrderEntity(OrderCreateDTO orderCreateDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(orderCreateDTO.getDate());
        orderEntity.setLogin(orderCreateDTO.getLogin());
        return orderEntity;
    }

    private OrderDTO mapToOrderDto(OrderEntity orderEntity) {
        return new OrderDTO(orderEntity.getId(),
                orderEntity.getDate(), orderEntity.getLogin());
    }
}

