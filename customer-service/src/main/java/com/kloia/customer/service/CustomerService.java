package com.kloia.customer.service;

import com.kloia.customer.dto.CustomerRequestDto;
import com.kloia.customer.exception.NotFoundException;
import com.kloia.customer.exception.SystemException;
import com.kloia.customer.model.Customer;
import com.kloia.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Customer findById(Integer id) throws NotFoundException {
        Optional<Customer> optional = customerRepository.findById(id);
        return optional.orElseThrow(() -> new NotFoundException("Customer with id: " + id + " not found"));
    }

    public Customer save(CustomerRequestDto customerRequestDto) throws InvocationTargetException, IllegalAccessException, SystemException {
        if (StringUtils.isBlank(customerRequestDto.getName())) {
            throw new SystemException("Name cannot be empty");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customer, customerRequestDto);
        return customerRepository.save(customer);
    }

}
