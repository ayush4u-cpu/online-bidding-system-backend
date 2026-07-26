package com.bidding.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;
    private String productName;
    private String specifications;
    private Double price;
    private String status;
    private String deliveryPerson;
    private String image;
    private String buyerEmail;
}
