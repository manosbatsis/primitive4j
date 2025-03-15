package com.github.manosbatsis.domainprimitives.sample.product;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

    private final ProductRepository repository;

    @GetMapping("{id}")
    ResponseEntity<Product> findById(@PathVariable ProductId id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping
    ResponseEntity<Product> save(@Valid @RequestBody Product entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
    }

    @PutMapping("{id}")
    ResponseEntity<Product> update(@Valid @RequestBody Product entity, @PathVariable ProductId id) {
        return ResponseEntity.ok(repository.save(entity));
    }
}
