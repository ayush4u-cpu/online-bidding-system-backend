package com.biddingonline.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.biddingonline.product_service.custom_exceptions.ResourceNotFoundException;
import com.biddingonline.product_service.dtos.ProductDto;
import com.biddingonline.product_service.entities.Product;
import com.biddingonline.product_service.mappers.ProductMapper;
import com.biddingonline.product_service.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Service

public class ProductServiceImpl {

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ProductMapper mapper;

	public List<ProductDto> getAllProducts() {
		// TODO Auto-generated method stub

		return productRepo.findAll().stream().map(prd -> mapper.toDto(prd)).toList();
	}

	public ProductDto getProductById(Long id) {
		// TODO Auto-generated method stub
		Product product = productRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));
		return mapper.toDto(product);
	}

	@Transactional
	// we are getting the product's id back here hence why we return the product
	public ProductDto addProduct(ProductDto newProduct) {
		// TODO Auto-generated method stub
		
		Product prd = productRepo.save(mapper.toEntity(newProduct));
		return mapper.toDto(prd);
	}

	@Transactional
	public ProductDto updateProduct(ProductDto updatedProductDto) {
		// TODO Auto-generated method stub
		Product prd = productRepo.findById(updatedProductDto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));
		mapper.updateProduct(updatedProductDto, prd); // here the object prd has been updated with the non null values
		return mapper.toDto(productRepo.save(prd));

	}

	@Transactional
	public void deleteProductById(int id) {
		// TODO Auto-generated method stub
		Product prd = productRepo.findById((long) id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));
		productRepo.delete(prd);
	}

}
