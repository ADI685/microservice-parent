package com.adi685.productservice.controller;

import com.adi685.productservice.dto.ProductRequest;
import com.adi685.productservice.dto.ProductResponse;
import com.adi685.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

  private final ProductService productService;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String addProduct(@RequestBody ProductRequest productRequest ){
    log.info("Add product received request : {}", productRequest);
    productService.createProduct(productRequest);
    return String.format("Successfully created new product : %s",productRequest.getName());
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductResponse> getAllProduct(){
    log.info("Received get all product list");
    return productService.getAllProduct();
  }



}
