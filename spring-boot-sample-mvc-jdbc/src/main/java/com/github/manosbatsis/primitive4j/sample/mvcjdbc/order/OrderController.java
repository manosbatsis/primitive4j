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
package com.github.manosbatsis.primitive4j.sample.mvcjdbc.order;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(OrderController.BASE_PATH)
public class OrderController {

    public static final String BASE_PATH = "api/purchase-orders";

    private final PurchaseOrderRepository repository;

    @GetMapping("{id}")
    ResponseEntity<PurchaseOrder> findById(@PathVariable UUID id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping
    ResponseEntity<PurchaseOrder> save(@Valid @RequestBody PurchaseOrder entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(entity));
    }

    @PutMapping("{id}")
    ResponseEntity<PurchaseOrder> update(@Valid @RequestBody PurchaseOrder entity, @PathVariable UUID id) {
        return ResponseEntity.ok(repository.save(entity));
    }
}
