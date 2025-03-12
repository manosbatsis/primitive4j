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
    ResponseEntity<Customer> updateByRef(
            @Valid @RequestBody Customer entity, @PathVariable CustomerRef ref) {
        return ResponseEntity.ok(repository.save(entity));
    }
}
