package com.folksdev.account.service;

import com.folksdev.account.dto.CustomerDto;
import com.folksdev.account.dto.CustomerDtoConverter;
import com.folksdev.account.exception.CustomerNotFoundException;
import com.folksdev.account.model.Customer;
import com.folksdev.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter converter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter converter) {
        this.customerRepository = customerRepository;
        this.converter = converter;
    }

    // Ben bu customer bilgisini dışarıya mı göndereceğim ? Hayır. Ben bunu Account servisinde kullanacağım. Already existing Customer'a yeni bir account açtığımda benim customer bilgilerine ihtiyacım var.
    // Servisler arası bu tarz veri transferlerinde Entity kullanabiliriz. Ve bunu yaparken protected kullanın.
    protected Customer findCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " +id));
    }

    public CustomerDto getCustomerById(String customerId) {
        return converter.convertToCustomerDto(findCustomerById(customerId));
    }
}
