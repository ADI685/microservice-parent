package com.adi685.orderservice.controller;

import com.adi685.orderservice.dto.OrderRequest;
import com.adi685.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private String placeOrder(@RequestBody OrderRequest orderRequest){
    log.info("Received placeOrder request : {}",orderRequest);
    orderService.placeOrder(orderRequest);
    return "Order placed successfully";
  }
}
