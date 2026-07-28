package com.biddingonline.product_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.biddingonline.product_service.entities.ProductStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//@NoArgsConstructor
//@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ProductDto {
	/*
	 * description
	 * imageUrl
	 * basePrice
	 * auctionEndTime
	 */
	private Long productId;
	@NotBlank
	private String name;
	
	private String description;
	private String imageUrl;
	@NotNull 
	@Positive
	private BigDecimal basePrice;
	@NotNull 
	@Positive
	private BigDecimal currentHighestBid;
	@NotNull 
	@Future
	private LocalDateTime auctionEndTime;
	@NotNull
	private ProductStatus status;
}
