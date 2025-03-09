package com.github.manosbatsis.typedidref.sample.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {

  private final OrderRepository repository;

  @GetMapping("{id}")
  ResponseEntity<Order> findById(@PathVariable Order.OrderRef id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @PostMapping
  ResponseEntity<Order> findById(@Valid @RequestBody Order entity) {
    return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
  }

  @PutMapping("{id}")
  ResponseEntity<Order> findById(
      @Valid @RequestBody Order entity, @PathVariable Order.OrderRef id) {
    return ResponseEntity.ok(repository.save(entity));
  }
}
