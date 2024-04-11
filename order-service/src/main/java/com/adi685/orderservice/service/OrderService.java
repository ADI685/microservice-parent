package com.adi685.orderservice.service;

import com.adi685.orderservice.dto.OrderLineItemsDto;
import com.adi685.orderservice.dto.OrderRequest;
import com.adi685.orderservice.model.Order;
import com.adi685.orderservice.model.OrderLineItems;
import com.adi685.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;

  public void placeOrder(OrderRequest orderRequest){
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    List<OrderLineItems> orderLineItemsList =orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToOrderLineItems).toList();
    order.setOrderLineItemList(orderLineItemsList);

    orderRepository.save(order);
    log.info("Successfully saved order with id : {}", order.getId());
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
