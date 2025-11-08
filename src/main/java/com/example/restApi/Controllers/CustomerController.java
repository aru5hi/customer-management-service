package com.example.restApi.Controllers;

import com.example.restApi.DTO.CustomerDTO;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Service.CustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class CustomerController {

    @Autowired
    private CustomerManagementService customerService;

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerDTO customerData) {
        CustomerEntity newCust = new CustomerEntity();
        try {
            newCust = customerService.createCustomer(customerData);
            return ResponseEntity.ok(newCust);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customers")
    public Page<CustomerEntity> getAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return customerService.getAllCustomers(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public CustomerEntity getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerEntity updateCustomer(@PathVariable String id, CustomerDTO updateCustomerData){
        return customerService.updateCustomer(id, updateCustomerData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
