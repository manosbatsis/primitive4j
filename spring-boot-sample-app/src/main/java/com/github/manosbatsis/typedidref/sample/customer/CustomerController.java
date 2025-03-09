package com.github.manosbatsis.typedidref.sample.customer;

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

  @GetMapping("{id}")
  ResponseEntity<Customer> findById(@PathVariable Customer.CustomerRef id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @PostMapping
  ResponseEntity<Customer> findById(@Valid @RequestBody Customer entity) {
    return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
  }

  @PutMapping("{id}")
  ResponseEntity<Customer> findById(
      @Valid @RequestBody Customer entity, @PathVariable Customer.CustomerRef id) {
    return ResponseEntity.ok(repository.save(entity));
  }

  @PostMapping("{id}/echo")
  ResponseEntity<Customer> echo(
      @Valid @RequestBody Customer entity, @PathVariable Customer.CustomerRef id) {
    return ResponseEntity.ok(entity);
  }
}
