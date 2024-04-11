package com.adi685.productservice.service;

import com.adi685.productservice.dto.ProductRequest;
import com.adi685.productservice.dto.ProductResponse;
import com.adi685.productservice.model.Product;
import com.adi685.productservice.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public void createProduct(ProductRequest productRequest){
    Product product = Product.builder()
        .price(productRequest.getPrice())
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .build();
    productRepository.insert(product);
    log.info("Successfully created product {}",product.getId());
  }

  public List<ProductResponse> getAllProduct() {
    List<Product> products = productRepository.findAll();
    return products.stream().map(this::mapToProductResponse).toList();
  }

  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .description(product.getDescription())
        .name(product.getName())
        .price(product.getPrice())
        .build();
  }
}
