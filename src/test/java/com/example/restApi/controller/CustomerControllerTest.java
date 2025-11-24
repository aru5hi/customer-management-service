package com.example.restApi.controller;

import com.example.restApi.Controllers.CustomerController;
import com.example.restApi.DTO.CustomerDTO;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Service.CustomerManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerManagementService service;

    @Test
    public void createCustomer_validPayload_returns200() throws Exception {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setName("Abcd");
        customer.setEmail("abnbcd@visa.com");

        when(service.createCustomer(any(CustomerDTO.class))).thenReturn(customer);

        String payload = """
                {
                    "name": "Abcd",
                    "email": "abnbcd@visa.com"
                }
                """;
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Abcd"))
                .andExpect(jsonPath("$.email").value("abnbcd@visa.com"));

        verify(service).createCustomer(any(CustomerDTO.class));
    }

    @Test
    public void createCustomer_invalidPayload_returns500() throws Exception {
        String payload = """
                {
                    "name": "",
                    "email": ""
                }
                """;
        
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"));

        verify(service, never()).createCustomer(any(CustomerDTO.class));
    }

    @Test
    public void createCustomer_duplicateEmail_returns400() throws Exception {
        when(service.createCustomer(any(CustomerDTO.class)))
                .thenThrow(new IllegalArgumentException("Customer with this email already exists"));

        String payload = """
                {
                    "name": "Abcd",
                    "email": "abnbcd@visa.com"
                }
                """;
        
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer with this email already exists"));

        verify(service).createCustomer(any(CustomerDTO.class));
    }

    @Test
    public void getAllCustomers_defaultParams_returnsPagedCustomers() throws Exception {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1L);
        customer1.setName("Customer 1");
        customer1.setEmail("customer1@test.com");

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(2L);
        customer2.setName("Customer 2");
        customer2.setEmail("customer2@test.com");

        List<CustomerEntity> customers = Arrays.asList(customer1, customer2);
        Page<CustomerEntity> page = new PageImpl<>(customers, PageRequest.of(0, 20), 2);

        when(service.getAllCustomers(0, 20, "id")).thenReturn(page);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Customer 1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(service).getAllCustomers(0, 20, "id");
    }

    @Test
    public void getAllCustomers_customParams_returnsPagedCustomers() throws Exception {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setName("Customer 1");
        customer.setEmail("customer1@test.com");

        List<CustomerEntity> customers = Arrays.asList(customer);
        Page<CustomerEntity> page = new PageImpl<>(customers, PageRequest.of(1, 10), 11);

        when(service.getAllCustomers(1, 10, "name")).thenReturn(page);

        mockMvc.perform(get("/api/customers")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(10));

        verify(service).getAllCustomers(1, 10, "name");
    }

    @Test
    public void getCustomerById_validId_returnsCustomer() throws Exception {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setName("Abcd");
        customer.setEmail("abnbcd@visa.com");

        when(service.getCustomerById("1")).thenReturn(customer);

        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Abcd"))
                .andExpect(jsonPath("$.email").value("abnbcd@visa.com"));

        verify(service).getCustomerById("1");
    }

    @Test
    public void getCustomerById_invalidId_throwsException() throws Exception {
        when(service.getCustomerById("999")).thenThrow(new RuntimeException("Customer not found"));

        mockMvc.perform(get("/api/999"))
                .andExpect(status().isInternalServerError());

        verify(service).getCustomerById("999");
    }

    @Test
    public void updateCustomer_validData_returnsUpdatedCustomer() throws Exception {
        CustomerEntity updatedCustomer = new CustomerEntity();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Updated Name");
        updatedCustomer.setEmail("updated@visa.com");

        when(service.updateCustomer(eq("1"), any(CustomerDTO.class))).thenReturn(updatedCustomer);

        String payload = """
                {
                    "name": "Updated Name",
                    "email": "updated@visa.com"
                }
                """;

        mockMvc.perform(put("/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@visa.com"));

        verify(service).updateCustomer(eq("1"), any(CustomerDTO.class));
    }

    @Test
    public void updateCustomer_invalidId_throwsException() throws Exception {
        when(service.updateCustomer(eq("999"), any(CustomerDTO.class)))
                .thenThrow(new RuntimeException("Customer not found"));

        String payload = """
                {
                    "name": "Updated Name",
                    "email": "updated@visa.com"
                }
                """;

        mockMvc.perform(put("/api/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError());

        verify(service).updateCustomer(eq("999"), any(CustomerDTO.class));
    }

    @Test
    public void deleteCustomer_validId_returns204() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/api/1"))
                .andExpect(status().isNoContent());

        verify(service).delete("1");
    }

    @Test
    public void deleteCustomer_invalidId_throwsException() throws Exception {
        doThrow(new RuntimeException("Customer not found")).when(service).delete("999");

        mockMvc.perform(delete("/api/999"))
                .andExpect(status().isInternalServerError());

        verify(service).delete("999");
    }
}
