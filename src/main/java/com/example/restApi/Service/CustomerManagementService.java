package com.example.restApi.Service;

import com.example.restApi.DTO.CustomerDTO;
import com.example.restApi.Entity.CustomerEntity;
import com.example.restApi.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManagementService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public CustomerEntity createCustomer(CustomerDTO customerData) {
        if(customerRepository.existsByEmail(customerData.getEmail())){
            throw new IllegalArgumentException("Email Already in use");
        }
        if(customerData.getEmail().equals(null) || customerData.getName().equals(null)){
            throw new IllegalArgumentException("Customer name & email are required");
        }
        CustomerEntity customerInsertionData = new CustomerEntity();
        customerInsertionData.setName(customerData.getName());
        //customerInsertionData.setName(customerData.getName());
        customerInsertionData.setEmail(customerData.getEmail());
        try {
            return customerRepository.save(customerInsertionData);
        } catch(Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly=true)
    public Page<CustomerEntity> getAllCustomers(int page, int size, String sortBy){
        String sortCriteria = sortBy.equals(null) ? "id" : sortBy;
        return customerRepository.findAll(PageRequest.of(page, size, Sort.by(sortCriteria).descending()));
    }

    @Transactional(readOnly = true)
    public CustomerEntity getCustomerById(String id) {
        Long customerId = Long.parseLong(id);
        return customerRepository.findById(customerId).orElseThrow(() ->new IllegalArgumentException("Customer not found"));
    }

    @Transactional
    public CustomerEntity updateCustomer(String id, CustomerDTO updatedCustData) {
            CustomerEntity cust = getCustomerById(id);
            cust.setName(updatedCustData.getName());
            cust.setEmail(updatedCustData.getEmail());
            return customerRepository.save(cust);
    }

    @Transactional
    public void delete(String id){
        Long custId = Long.parseLong(id);
        customerRepository.deleteById(custId);
    }
}
