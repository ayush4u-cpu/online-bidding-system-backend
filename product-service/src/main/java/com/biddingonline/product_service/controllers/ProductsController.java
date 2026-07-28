package com.biddingonline.product_service.controllers;

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

import com.biddingonline.product_service.dtos.ProductDto;
import com.biddingonline.product_service.service.ProductServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
	@Autowired
	private ProductServiceImpl productService;

	@GetMapping
	// Called by admin?
	public ResponseEntity<List<ProductDto>> getAllProducts() {

		List<ProductDto> productsList = productService.getAllProducts();

		return ResponseEntity.ok(productsList);

	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") Long id) {

		return ResponseEntity.ok(productService.getProductById(id));

	}

	@PostMapping()
	public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto newProduct) {

		return ResponseEntity.ok(productService.addProduct(newProduct));

	}

	@PatchMapping()
	public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto updatedProduct) {
		/*
		 * description imageUrl basePrice auctionEndTime
		 */
		ProductDto product = productService.updateProduct(updatedProduct);
		return ResponseEntity.ok(product);

	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<String> deleteProductById(@PathVariable("productId") int id) {
		productService.deleteProductById(id);
		return ResponseEntity.ok("Product has been deleted.");

	}
}
