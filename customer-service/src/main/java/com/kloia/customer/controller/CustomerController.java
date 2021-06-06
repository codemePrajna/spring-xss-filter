package com.kloia.customer.controller;

import com.kloia.customer.dto.CustomerRequestDto;
import com.kloia.customer.model.Customer;
import com.kloia.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> all = customerService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) throws Exception {
        Customer customer = customerService.findById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createCustomer(
            HttpServletRequest httpServletRequest,
            @RequestBody @Validated CustomerRequestDto customerRequestDto,
            @RequestHeader(name = "X-Request-Id", required = false) String requestId
    ) throws Exception {
        String requestIdFromHttpServletRequest = httpServletRequest.getHeader("X-Request-Id");
        Customer account = customerService.save(customerRequestDto);
        return ResponseEntity.ok(account);
    }

}
