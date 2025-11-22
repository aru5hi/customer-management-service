package com.example.restApi.Controllers;

import com.example.restApi.DTO.OrderModel;
import com.example.restApi.Entity.OrderEntity;
import com.example.restApi.Service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrdersController {
    @Autowired
    private OrderManagementService orderService;

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<Page<OrderEntity>> getAllOrderForCustomerId(@PathVariable String customerId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size ) {
        Page<OrderEntity> orderLists = orderService.getOrdersByCustomerId(customerId, PageRequest.of(page, size));
        return ResponseEntity.ok(orderLists);
    }

    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<OrderEntity> createOrderForCustomer(@PathVariable String customerId, @RequestBody OrderModel orderData) {
        OrderEntity newOrderCreated = orderService.createOrderForCustomerId(customerId, orderData);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrderCreated);
    }

}
