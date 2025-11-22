package com.example.restApi.Controllers;

import com.example.restApi.DTO.CustomerDTO;
import com.example.restApi.DTO.UserDto;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Service.CustomerManagementService;
import com.example.restApi.Service.ExternalRestApiTemplateService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementService customerService;
    private final ExternalRestApiTemplateService restApiTemplateService;

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerDTO customerData) {
        CustomerEntity newCust = new CustomerEntity();
        try {
            newCust = customerService.createCustomer(customerData);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCust);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerEntity>> getAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "id") String sortBy) {
        Page<CustomerEntity> allCustomers = customerService.getAllCustomers(page, size, sortBy);
        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable String id) {
        CustomerEntity customer =  customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerEntity> updateCustomer(@PathVariable String id, CustomerDTO updateCustomerData){
        CustomerEntity custUpdated = customerService.updateCustomer(id, updateCustomerData);
        return ResponseEntity.ok(custUpdated);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/generate")
    public ResponseEntity<UserDto> generateNewCustomer() {
        UserDto user = restApiTemplateService.getUSerASObject(1L);
        return ResponseEntity.ok(user);
    }


}
