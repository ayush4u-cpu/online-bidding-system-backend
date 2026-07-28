package com.bidding.orderservice.dto;

import com.bidding.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;
    private Long productId;
    private Long buyerId;
    private Long sellerId;
    private Long deliveryPersonId;
    private BigDecimal finalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
