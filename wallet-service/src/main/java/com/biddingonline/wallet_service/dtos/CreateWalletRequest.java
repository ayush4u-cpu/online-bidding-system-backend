package com.biddingonline.wallet_service.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWalletRequest {
	@NotNull
	private Long userId;
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal initialBalance;
}