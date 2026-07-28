package com.biddingonline.product_service.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.biddingonline.product_service.dtos.ProductDto;
import com.biddingonline.product_service.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductDto toDto(Product product);
	Product toEntity(ProductDto dto);
	@BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(ProductDto dto,
                       @MappingTarget Product product);
}
