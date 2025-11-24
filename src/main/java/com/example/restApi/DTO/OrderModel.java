package com.example.restApi.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderModel {
    @NotEmpty(message="Order must have at least one item")
    private List<OrderItemModel> items;
}
