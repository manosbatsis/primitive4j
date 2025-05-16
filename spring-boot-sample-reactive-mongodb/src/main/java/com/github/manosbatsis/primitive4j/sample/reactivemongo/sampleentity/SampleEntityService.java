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
package com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class SampleEntityService {

    private final SampleEntityRepository sampleEntityRepository;

    public Flux<SampleEntity> findAll() {
        return sampleEntityRepository.findAll();
    }

    public Mono<SampleEntity> findById(UUID id) {
        return sampleEntityRepository.findById(id);
    }

    public Mono<SampleEntity> save(SampleEntity sampleEntity) {
        return sampleEntityRepository.save(sampleEntity);
    }

    public Mono<SampleEntity> update(UUID id, SampleEntity sampleEntity) {
        return sampleEntityRepository
                .findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalSampleEntity -> {
                    if (optionalSampleEntity.isPresent()) {
                        sampleEntity.setId(id);
                        return sampleEntityRepository.save(sampleEntity);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(UUID id) {
        return sampleEntityRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return sampleEntityRepository.deleteAll();
    }
}
