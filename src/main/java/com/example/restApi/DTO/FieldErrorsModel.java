package com.example.restApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorsModel {
    private String field;
    private String rejectedValue;
    private String message;
}
