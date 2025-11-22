package com.example.restApi.Entity;

import com.example.restApi.DTO.OrderItemModel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="customer_orders")
@Data
public class OrderEntity {

    @Id
    @Column(name="order_id")
    @GeneratedValue
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private CustomerEntity customer;

    @Column(name="status", nullable=false)
    private String status;

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

    @ElementCollection
    @CollectionTable(name="order_items", joinColumns = @JoinColumn(name="order_id"))
    private List<OrderItemModel> itemList;
}
