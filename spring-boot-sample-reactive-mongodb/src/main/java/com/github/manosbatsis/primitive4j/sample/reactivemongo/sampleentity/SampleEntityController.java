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

import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriRecord;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(SampleEntityController.BASE_PATH)
public class SampleEntityController {

    public static final String BASE_PATH = "api/sample-entities";

    private final SampleEntityRepository repository;

    private final SampleEntityService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<SampleEntity> save(@Valid @RequestBody SampleEntity request) {
        return service.save(request);
    }

    // Network
    @GetMapping("findAllByUriRecord/{param}")
    Flux<SampleEntity> findAllByUriRecord(@PathVariable UriRecord param) {
        return repository.findAllByUriRecord(param);
    }

    @GetMapping("findAllByUriBean/{param}")
    Flux<SampleEntity> findAllByUriBean(@PathVariable UriBean param) {
        return repository.findAllByUriBean(param);
    }

    @GetMapping("findAllByUrlRecord/{param}")
    Flux<SampleEntity> findAllByUrlRecord(@PathVariable UrlRecord param) {
        return repository.findAllByUrlRecord(param);
    }

    @GetMapping("findAllByUrlBean/{param}")
    Flux<SampleEntity> findAllByUrlBean(@PathVariable UrlBean param) {
        return repository.findAllByUrlBean(param);
    }

    // Numbers
    @GetMapping("findAllByBigDecimalBean/{param}")
    Flux<SampleEntity> findAllByBigDecimalBean(@PathVariable BigDecimalBean param) {
        return repository.findAllByBigDecimalBean(param);
    }

    @GetMapping("findAllByBigDecimalRecord/{param}")
    Flux<SampleEntity> findAllByBigDecimalRecord(@PathVariable BigDecimalRecord param) {
        return repository.findAllByBigDecimalRecord(param);
    }

    @GetMapping("findAllByBigIntegerBean/{param}")
    Flux<SampleEntity> findAllByBigIntegerBean(@PathVariable BigIntegerBean param) {
        return repository.findAllByBigIntegerBean(param);
    }

    @GetMapping("findAllByBigIntegerRecord/{param}")
    Flux<SampleEntity> findAllByBigIntegerRecord(@PathVariable BigIntegerRecord param) {
        return repository.findAllByBigIntegerRecord(param);
    }

    @GetMapping("findAllByDoubleBean/{param}")
    Flux<SampleEntity> findAllByDoubleBean(@PathVariable DoubleBean param) {
        return repository.findAllByDoubleBean(param);
    }

    @GetMapping("findAllByDoubleRecord/{param}")
    Flux<SampleEntity> findAllByDoubleRecord(@PathVariable DoubleRecord param) {
        return repository.findAllByDoubleRecord(param);
    }

    @GetMapping("findAllByFloatBean/{param}")
    Flux<SampleEntity> findAllByFloatBean(@PathVariable FloatBean param) {
        return repository.findAllByFloatBean(param);
    }

    @GetMapping("findAllByFloatRecord/{param}")
    Flux<SampleEntity> findAllByFloatRecord(@PathVariable FloatRecord param) {
        return repository.findAllByFloatRecord(param);
    }

    @GetMapping("findAllByIntegerBean/{param}")
    Flux<SampleEntity> findAllByIntegerBean(@PathVariable IntegerBean param) {
        return repository.findAllByIntegerBean(param);
    }

    @GetMapping("findAllByIntegerRecord/{param}")
    Flux<SampleEntity> findAllByIntegerRecord(@PathVariable IntegerRecord param) {
        return repository.findAllByIntegerRecord(param);
    }

    @GetMapping("findAllByLongBean/{param}")
    Flux<SampleEntity> findAllByLongBean(@PathVariable LongBean param) {
        return repository.findAllByLongBean(param);
    }

    @GetMapping("findAllByLongRecord/{param}")
    Flux<SampleEntity> findAllByLongRecord(@PathVariable LongRecord param) {
        return repository.findAllByLongRecord(param);
    }

    @GetMapping("findAllByShortBean/{param}")
    Flux<SampleEntity> findAllByShortBean(@PathVariable ShortBean param) {
        return repository.findAllByShortBean(param);
    }

    @GetMapping("findAllByShortRecord/{param}")
    Flux<SampleEntity> findAllByShortRecord(@PathVariable ShortRecord param) {
        return repository.findAllByShortRecord(param);
    }

    // Strings
    @GetMapping("findAllByStringBean/{param}")
    Flux<SampleEntity> findAllByStringBean(@PathVariable StringBean param) {
        return repository.findAllByStringBean(param);
    }

    @GetMapping("findAllByStringRecord/{param}")
    Flux<SampleEntity> findAllByStringRecord(@PathVariable StringRecord param) {
        return repository.findAllByStringRecord(param);
    }

    @GetMapping("findAllByUuidBean/{param}")
    Flux<SampleEntity> findAllByUuidBean(@PathVariable UuidBean param) {
        return repository.findAllByUuidBean(param);
    }

    @GetMapping("findAllByUuidRecord/{param}")
    Flux<SampleEntity> findAllByUuidRecord(@PathVariable UuidRecord param) {
        return repository.findAllByUuidRecord(param);
    }
}
