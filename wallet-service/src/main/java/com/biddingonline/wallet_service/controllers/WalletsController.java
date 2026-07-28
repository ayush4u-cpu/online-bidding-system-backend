package com.biddingonline.wallet_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biddingonline.wallet_service.dtos.AmountRequest;
import com.biddingonline.wallet_service.dtos.CreateWalletRequest;
import com.biddingonline.wallet_service.dtos.WalletDto;
import com.biddingonline.wallet_service.service.WalletServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallets")
public class WalletsController {
	@Autowired
	private WalletServiceImpl walletService;

	@GetMapping
	// Called by admin?
	public ResponseEntity<List<WalletDto>> getAllWallets() {

		List<WalletDto> productsList = walletService.getAllWallets();

		return ResponseEntity.ok(productsList);

	}

	@GetMapping("/{walletId}")
	public ResponseEntity<WalletDto> getWalletById(@PathVariable("walletId") int id) {

		return ResponseEntity.ok(walletService.getWalletById(id));

	}

	@PostMapping("/create")
	public ResponseEntity<WalletDto> createWallet(@RequestBody @Valid CreateWalletRequest dto) {

		return ResponseEntity.ok(walletService.createWallet(dto));
	}

	@PatchMapping("/update")
	public ResponseEntity<WalletDto> updateWallet(@RequestBody @Valid WalletDto dto) {
		return ResponseEntity.ok(walletService.updateWallet(dto));
	}

	@DeleteMapping("/delete/{walletId}")
	public ResponseEntity<String> deleteWalletByID(@PathVariable int walletId) {
		walletService.deleteWalletById(walletId);
		return ResponseEntity.ok("Product has been deleted!");
	}

	@PostMapping("/{userId}/deposit")
	public ResponseEntity<WalletDto> deposit(@PathVariable Long userId, @Valid @RequestBody AmountRequest request) {
		return ResponseEntity.ok(walletService.deposit(userId, request.getAmount()));
	}

	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<WalletDto> withdraw(@PathVariable Long userId, @Valid @RequestBody AmountRequest request) {
		return ResponseEntity.ok(walletService.withdraw(userId, request.getAmount()));
	}

}
