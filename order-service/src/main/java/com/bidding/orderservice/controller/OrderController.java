package com.bidding.orderservice.controller;

import com.bidding.orderservice.dto.OrderDto;
import com.bidding.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") String id) {
        Long numericId = parseOrderId(id);
        return ResponseEntity.ok(orderService.getOrderById(numericId));
    }

    @GetMapping("/buyer/{buyerEmail}")
    public ResponseEntity<List<OrderDto>> getOrdersByBuyer(@PathVariable("buyerEmail") String buyerEmail) {
        return ResponseEntity.ok(orderService.getOrdersByBuyer(buyerEmail));
    }

    @GetMapping("/delivery/{deliveryPerson}")
    public ResponseEntity<List<OrderDto>> getOrdersByDeliveryPerson(@PathVariable("deliveryPerson") String deliveryPerson) {
        return ResponseEntity.ok(orderService.getOrdersByDeliveryPerson(deliveryPerson));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("id") String id, @RequestBody Map<String, String> payload) {
        Long numericId = parseOrderId(id);
        String status = payload.get("status");
        return ResponseEntity.ok(orderService.updateOrderStatus(numericId, status));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<OrderDto> assignDeliveryPerson(@PathVariable("id") String id, @RequestBody Map<String, String> payload) {
        Long numericId = parseOrderId(id);
        String deliveryPerson = payload.get("deliveryPerson");
        return ResponseEntity.ok(orderService.assignDeliveryPerson(numericId, deliveryPerson));
    }

    private Long parseOrderId(String id) {
        try {
            if (id.startsWith("ORD-")) {
                return Long.parseLong(id.substring(4)) - 1000;
            }
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid order ID format: " + id);
        }
    }
}
