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
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerRepository repository;

    @GetMapping("{ref}")
    ResponseEntity<Customer> getByRef(@PathVariable CustomerRef ref) {
        return ResponseEntity.of(repository.findOneByRef(ref));
    }

    @PostMapping
    ResponseEntity<Customer> save(@Valid @RequestBody Customer entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
    }

    @PutMapping("{ref}")
    ResponseEntity<Customer> updateByRef(@Valid @RequestBody Customer entity, @PathVariable CustomerRef ref) {
        return ResponseEntity.ok(repository.save(entity));
    }
}
