package com.biddingonline.wallet_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biddingonline.wallet_service.custom_exceptions.InsufficientBalanceException;
import com.biddingonline.wallet_service.custom_exceptions.ResourceNotFoundException;
import com.biddingonline.wallet_service.dtos.CreateWalletRequest;
import com.biddingonline.wallet_service.dtos.WalletDto;
import com.biddingonline.wallet_service.entities.Wallet;
import com.biddingonline.wallet_service.entities.WalletStatus;
import com.biddingonline.wallet_service.mappers.WalletMapper;
import com.biddingonline.wallet_service.repositories.WalletRepository;

@Service

public class WalletServiceImpl {

	@Autowired
	private WalletRepository walletRepo;
	@Autowired
	private WalletMapper mapper;
	
	@Transactional
	public WalletDto createWallet(CreateWalletRequest dto) {
		// TODO Auto-generated method stub
		Wallet newWallet = new Wallet();
		newWallet.setUserId(dto.getUserId());
		newWallet.setBalance(dto.getInitialBalance());
		newWallet.setStatus(WalletStatus.ACTIVE);
		Wallet savedWallet = walletRepo.save(newWallet);
		return mapper.toDto(savedWallet);
	}
	@Transactional
	public WalletDto updateWallet(WalletDto updatedto) {
		// TODO Auto-generated method stub
		Wallet updatedWallet = walletRepo.findById(updatedto.getWalletId())
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found."));
		mapper.updateWallet(updatedto, updatedWallet);
		return mapper.toDto(updatedWallet);
	}
	@Transactional
	public void deleteWalletById(int walletId) {
		// TODO Auto-generated method stub
		Wallet wallet = walletRepo.findById((long)walletId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found."));
		walletRepo.delete(wallet);
	}
	 @Transactional(readOnly = true)
	public List<WalletDto> getAllWallets() {
		// TODO Auto-generated method stub
		List<Wallet> list = walletRepo.findAll();
		List<WalletDto> dtoList = list.stream().map(wallet -> mapper.toDto(wallet)).toList();
		return dtoList;
	}
	 @Transactional(readOnly = true)
	public WalletDto getWalletById(int id) {
		// TODO Auto-generated method stub
		Wallet wallet = walletRepo.findById((long) id)
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found."));
		return mapper.toDto(wallet);
	}
	@Transactional
	public WalletDto deposit(Long userId, BigDecimal amount) {
		Wallet wallet = walletRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
		wallet.setBalance(wallet.getBalance().add(amount));
		return mapper.toDto(walletRepo.save(wallet));
	}
	@Transactional
	public WalletDto withdraw(Long userId, BigDecimal amount) {
		Wallet wallet = walletRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
		if (wallet.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient wallet balance");
		}
		wallet.setBalance(wallet.getBalance().subtract(amount));
		return mapper.toDto(walletRepo.save(wallet));
	}

}
