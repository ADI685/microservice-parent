package com.adi685.orderservice.service;

import com.adi685.orderservice.dto.InventoryResponse;
import com.adi685.orderservice.dto.OrderLineItemsDto;
import com.adi685.orderservice.dto.OrderRequest;
import com.adi685.orderservice.model.Order;
import com.adi685.orderservice.model.OrderLineItems;
import com.adi685.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToOrderLineItems).toList();
    order.setOrderLineItemList(orderLineItemsList);

    List<String> skuCodeList = order.getOrderLineItemList().stream()
        .map(OrderLineItems::getSkuCode).toList();

    InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
        .uri("http://intventory-service/api/inventory",
            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

    log.info("array {}", (Object) inventoryResponseArray);

    assert inventoryResponseArray != null;
    boolean allInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);


    if (allInStock){
      orderRepository.save(order);
      log.info("Successfully saved order with id : {}", order.getId());
    }else{
      log.info(" order not saved : {}", orderRequest);
      throw new IllegalArgumentException("Order not in stock");
    }
  }

  private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setId(orderLineItemsDto.getId());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

    return orderLineItems;
  }

}
