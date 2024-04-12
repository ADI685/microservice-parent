package com.adi685.inventoryservice.service;

import com.adi685.inventoryservice.dto.InventoryResponse;
import com.adi685.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

  public  final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public List<InventoryResponse> isInStock(List<String> skuCodes) {
    List<InventoryResponse> inventoryResponseList = new java.util.ArrayList<>(inventoryRepository.findBySkuCodeIn(skuCodes).stream()
        .map(inventory ->
            InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .isInStock(inventory.getQuantity() > 0)
                .build()
        ).toList());

    // Create a set of SKU codes from inventoryResponseList for faster lookups
    Set<String> existingSkuCodes = inventoryResponseList.stream()
        .map(InventoryResponse::getSkuCode)
        .collect(Collectors.toSet());

    // Iterate over skuCodes and add missing SKU codes to inventoryResponseList
    for (String skuCode : skuCodes) {
      if (!existingSkuCodes.contains(skuCode)) {
        inventoryResponseList.add(InventoryResponse.builder()
            .skuCode(skuCode)
                .quantity(0)
            .isInStock(false)
            .build());
      }
    }

    // Log retrieved result
    log.info("Retrieved result {}", inventoryResponseList);

    return inventoryResponseList;
  }
}
