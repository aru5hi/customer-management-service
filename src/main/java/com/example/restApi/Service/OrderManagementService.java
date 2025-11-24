package com.example.restApi.Service;

import com.example.restApi.DTO.OrderModel;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Entity.OrderEntity;
import com.example.restApi.Repository.CustomerRepository;
import com.example.restApi.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Transactional
    public Page<OrderEntity> getOrdersByCustomerId(String customerId, Pageable pageable) {
        return orderRepo.findByCustomerId(Long.parseLong(customerId), pageable);
    }

    @Transactional
    public OrderEntity createOrderForCustomerId(String customerId, OrderModel orderData) {
        CustomerEntity customerData = customerRepo.findById(Long.parseLong(customerId))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        OrderEntity order = new OrderEntity();
        order.setCustomer(customerData);
        order.setItemList(orderData.getItems());
        order.setStatus("PENDING");
        return orderRepo.save(order);
    }
}
