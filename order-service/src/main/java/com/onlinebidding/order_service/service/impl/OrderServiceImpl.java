package com.onlinebidding.order_service.service.impl;

import com.onlinebidding.order_service.dto.OrderDto;
import com.onlinebidding.order_service.entity.Order;
import com.onlinebidding.order_service.entity.OrderStatus;
import com.onlinebidding.order_service.exception.ResourceNotFoundException;
import com.onlinebidding.order_service.repository.OrderRepository;
import com.onlinebidding.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setProductId(orderDto.getProductId());
        order.setBuyerId(orderDto.getBuyerId());
        order.setSellerId(orderDto.getSellerId());
        order.setDeliveryPersonId(orderDto.getDeliveryPersonId());
        order.setFinalPrice(orderDto.getFinalPrice());
        order.setStatus(orderDto.getStatus() != null ? orderDto.getStatus() : OrderStatus.PENDING);
        order.setCreatedAt(orderDto.getCreatedAt() != null ? orderDto.getCreatedAt() : LocalDateTime.now());

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByDeliveryPerson(Long deliveryPersonId) {
        return orderRepository.findByDeliveryPersonId(deliveryPersonId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assignDeliveryPerson(Long id, Long deliveryPersonId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setDeliveryPersonId(deliveryPersonId);
        order.setStatus(OrderStatus.ASSIGNED);
        return mapToDto(orderRepository.save(order));
    }

    private OrderDto mapToDto(Order order) {
        return new OrderDto(
                "ORD-" + (1000 + order.getId()),
                order.getProductId(),
                order.getBuyerId(),
                order.getSellerId(),
                order.getDeliveryPersonId(),
                order.getFinalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
