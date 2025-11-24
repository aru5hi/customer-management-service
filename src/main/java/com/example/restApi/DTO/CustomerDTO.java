package com.example.restApi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    @NotBlank
    @Size(max=100)
    private String name;

    @NotBlank
    @Size(max = 150)
    private String email;

}
