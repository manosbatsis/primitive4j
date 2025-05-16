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
package com.github.manosbatsis.primitive4j.sample.reactivemongo.order;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(OrderController.BASE_PATH)
public class OrderController {

    public static final String BASE_PATH = "api/purchase-orders";

    private final PurchaseOrderRepository repository;

    @GetMapping("{id}")
    Mono<PurchaseOrder> findById(@PathVariable UUID id) {
        return repository.findById(id);
    }

    @PostMapping
    Mono<PurchaseOrder> save(@Valid @RequestBody PurchaseOrder entity) {
        return repository.save(entity);
    }

    @PutMapping("{id}")
    Mono<PurchaseOrder> update(@Valid @RequestBody PurchaseOrder entity, @PathVariable UUID id) {
        return repository.save(entity);
    }
}
