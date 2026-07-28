package com.biddingonline.wallet_service.custom_exceptions;

public class InsufficientBalanceException extends RuntimeException {

	public InsufficientBalanceException(String message) {
		super(message);
	}
}
