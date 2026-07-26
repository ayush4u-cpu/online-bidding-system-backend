package com.bidding.orderservice.service;

import com.bidding.orderservice.dto.OrderDto;
import com.bidding.orderservice.entity.Order;
import com.bidding.orderservice.exception.ResourceNotFoundException;
import com.bidding.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        order.setProductName(orderDto.getProductName());
        order.setSpecifications(orderDto.getSpecifications());
        order.setPrice(orderDto.getPrice());
        order.setStatus(orderDto.getStatus() != null ? orderDto.getStatus() : "ASSIGNED");
        order.setDeliveryPerson(orderDto.getDeliveryPerson() != null ? orderDto.getDeliveryPerson() : "Not Assigned");
        order.setImage(orderDto.getImage());
        order.setBuyerEmail(orderDto.getBuyerEmail());

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByBuyer(String buyerEmail) {
        return orderRepository.findByBuyerEmail(buyerEmail).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByDeliveryPerson(String deliveryPerson) {
        return orderRepository.findByDeliveryPerson(deliveryPerson).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assignDeliveryPerson(Long id, String deliveryPerson) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setDeliveryPerson(deliveryPerson);
        order.setStatus("ASSIGNED");
        return mapToDto(orderRepository.save(order));
    }

    private OrderDto mapToDto(Order order) {
        return new OrderDto(
                "ORD-" + (1000 + order.getId()),
                order.getProductName(),
                order.getSpecifications(),
                order.getPrice(),
                order.getStatus(),
                order.getDeliveryPerson(),
                order.getImage(),
                order.getBuyerEmail()
        );
    }
}
