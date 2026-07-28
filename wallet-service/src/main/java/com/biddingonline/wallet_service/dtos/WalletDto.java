package com.biddingonline.wallet_service.dtos;

import java.math.BigDecimal;

import com.biddingonline.wallet_service.entities.WalletStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
	private Long walletId;
	private Long userId;
	@NotNull
	private WalletStatus status;
	@NotNull @Positive
	private BigDecimal balance;
}
