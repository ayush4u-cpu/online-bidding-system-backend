package com.biddingonline.wallet_service.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.biddingonline.wallet_service.dtos.WalletDto;
import com.biddingonline.wallet_service.entities.Wallet;
@Mapper(componentModel = "spring")
public interface WalletMapper {
	WalletDto toDto(Wallet wallet);
	Wallet toEntity(WalletDto dto);
	@BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateWallet(WalletDto dto,
                       @MappingTarget Wallet wallet);
}
