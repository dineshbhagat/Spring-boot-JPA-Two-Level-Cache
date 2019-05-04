package com.example.service;

import com.example.entity.Customer;
import com.example.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Page<Customer> getAllCustomers() {
        Sort sort = Sort.by(Sort.Order.by("mobileNumber"));
        PageRequest pageRequest = PageRequest.of(1, 5, sort);

        return customerRepository.findAll(pageRequest);
    }

    public Customer getCustomer(Integer id) {
        return customerRepository.getOne(id);
    }
}
