package com.onlinebidding.bid_service.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidDto {
    private Long id;
    private Long auctionId;
    private Long bidderId;
    private String bidderName;
    private BigDecimal amount;
    private LocalDateTime bidTime;
}
