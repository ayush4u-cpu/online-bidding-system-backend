package com.bidding.orderservice.repository;

import com.bidding.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerEmail(String buyerEmail);
    List<Order> findByDeliveryPerson(String deliveryPerson);
}
