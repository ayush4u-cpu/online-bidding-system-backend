package com.bidding.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String specifications;
    private Double price;
    private String status; // ASSIGNED, OUT_FOR_DELIVERY, DELIVERED
    private String deliveryPerson;
    private String image;
    private String buyerEmail;
}
