package com.example.restApi.service;

import com.example.restApi.DTO.CustomerDTO;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Repository.CustomerRepository;
import com.example.restApi.Service.CustomerManagementService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static  org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repo;

    @InjectMocks
    private CustomerManagementService svc;

    @Test
    public void create_whenEmailExists_throException() {
        CustomerDTO custData = new CustomerDTO();
        custData.setName("Alice");
        custData.setEmail("alice@gmail.com");
        when(repo.existsByEmail("alice@gmail.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> svc.createCustomer(custData));
        verify(repo, never()).save(any());
    }

    @Test
    public void create_wenEmailUnique() {
        CustomerDTO custData = new CustomerDTO();
        custData.setName("Arushi");
        custData.setEmail("arushi@gmail.com");

        when(repo.existsByEmail("arushi@gmail.com")).thenReturn(true);
        when(repo.save(any(CustomerEntity.class))).thenAnswer(inv -> {
            CustomerEntity custEntity = inv.getArgument(0);
            custEntity.setId(1L);
            return custEntity;
        });

        CustomerEntity result = svc.createCustomer(custData);
        assertNotNull(result);
        assertEquals("Arushi", result.getName());
        assertEquals("arushi@gmail.com", result.getEmail());
        verify(repo).save(any(CustomerEntity.class));
    }
}
