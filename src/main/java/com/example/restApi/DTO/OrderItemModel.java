package com.example.restApi.DTO;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderItemModel {
    private String itemName;
    private Integer quantity;
}
