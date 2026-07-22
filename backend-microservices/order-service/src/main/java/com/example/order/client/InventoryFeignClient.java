package com.example.order.client;

import com.example.order.dto.InventoryResponse;
import com.example.order.dto.ReleaseStockRequest;
import com.example.order.dto.ReserveStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryFeignClient {

    @PostMapping("/v1/reserve")
    InventoryResponse reserve(@RequestBody ReserveStockRequest request);

    @PostMapping("/v1/release")
    InventoryResponse release(@RequestBody ReleaseStockRequest request);
}
