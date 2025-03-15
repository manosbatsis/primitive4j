package com.github.manosbatsis.domainprimitives.sample.order.orderline;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orderlines")
public class OrderLineController {

    private final OrderLineRepository repository;

    @GetMapping("{id}")
    ResponseEntity<OrderLine> findById(@PathVariable OrderLineId id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping
    ResponseEntity<OrderLine> save(@Valid @RequestBody OrderLine entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
    }

    @PutMapping("{id}")
    ResponseEntity<OrderLine> update(
            @Valid @RequestBody OrderLine entity, @PathVariable OrderLineId id) {
        return ResponseEntity.ok(repository.save(entity));
    }
}
