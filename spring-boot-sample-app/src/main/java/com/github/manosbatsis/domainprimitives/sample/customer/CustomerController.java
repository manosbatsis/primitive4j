/**
 * Copyright (C) 2024-2025 Manos Batsis
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
 * <a href="https://www.gnu.org/licenses/lgpl-3.0.html">https://www.gnu.org/licenses/lgpl-3.0.html</a>.
 */
package com.github.manosbatsis.domainprimitives.sample.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CustomerController.BASE_PATH)
public class CustomerController {

    public static final String BASE_PATH = "api/customers";

    private final CustomerRepository repository;

    @GetMapping("{ref}")
    ResponseEntity<CustomerResponse> getByRef(@PathVariable CustomerRef ref) {
        return repository
                .findOneByRef(ref)
                .map(customer -> ResponseEntity.ok(CustomerResponse.builder()
                        .ref(customer.getRef())
                        .name(customer.getName())
                        .build()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    ResponseEntity<CustomerResponse> save(@Valid @RequestBody CustomerCreateRequest request) {
        var customer = repository.save(
                Customer.builder().name(request.getName()).ref(request.getRef()).build());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomerResponse.builder()
                        .ref(customer.getRef())
                        .name(customer.getName())
                        .build());
    }

    @PutMapping("{ref}")
    ResponseEntity<CustomerResponse> updateByRef(
            @Valid @RequestBody CustomerUpdateRequest request, @PathVariable CustomerRef ref) {
        var maybeCustomer = repository.findOneByRef(ref);
        if (maybeCustomer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var customer = maybeCustomer.get();
        customer.setName(request.getName());
        customer = repository.save(customer);

        return ResponseEntity.ok(CustomerResponse.builder()
                .ref(customer.getRef())
                .name(customer.getName())
                .build());
    }
}
