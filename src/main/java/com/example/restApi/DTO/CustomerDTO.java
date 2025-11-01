package com.example.restApi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerDTO {

    @NotBlank
    @Size(max=100)
    private String name;

    @NotBlank
    @Size(max = 150)
    private String email;

    public String getName() { return name; }
    public void setName (String name) {this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
