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
package com.github.manosbatsis.primitive4j.sample.mvcjpa.sampleentity;

import com.github.manosbatsis.primitive4j.test.common.example.*;
import com.github.manosbatsis.primitive4j.test.common.example.network.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SampleEntityController.BASE_PATH)
public class SampleEntityController {

    public static final String BASE_PATH = "api/sample-entities";

    private final SampleEntityRepository repository;

    private final SampleEntityService service;

    @PostMapping
    ResponseEntity<SampleEntity> save(@Valid @RequestBody SampleEntity request) {
        var result = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Network
    @GetMapping("findAllByUriRecord/{param}")
    List<SampleEntity> findAllByUriRecord(@PathVariable UriRecord param) {
        return repository.findAllByUriRecord(param);
    }

    @GetMapping("findAllByUriBean/{param}")
    List<SampleEntity> findAllByUriBean(@PathVariable UriBean param) {
        return repository.findAllByUriBean(param);
    }

    @GetMapping("findAllByUrlRecord/{param}")
    List<SampleEntity> findAllByUrlRecord(@PathVariable UrlRecord param) {
        return repository.findAllByUrlRecord(param);
    }

    @GetMapping("findAllByUrlBean/{param}")
    List<SampleEntity> findAllByUrlBean(@PathVariable UrlBean param) {
        return repository.findAllByUrlBean(param);
    }

    // Numbers
    @GetMapping("findAllByBigDecimalBean/{param}")
    List<SampleEntity> findAllByBigDecimalBean(@PathVariable BigDecimalBean param) {
        return repository.findAllByBigDecimalBean(param);
    }

    @GetMapping("findAllByBigDecimalRecord/{param}")
    List<SampleEntity> findAllByBigDecimalRecord(@PathVariable BigDecimalRecord param) {
        return repository.findAllByBigDecimalRecord(param);
    }

    @GetMapping("findAllByBigIntegerBean/{param}")
    List<SampleEntity> findAllByBigIntegerBean(@PathVariable BigIntegerBean param) {
        return repository.findAllByBigIntegerBean(param);
    }

    @GetMapping("findAllByBigIntegerRecord/{param}")
    List<SampleEntity> findAllByBigIntegerRecord(@PathVariable BigIntegerRecord param) {
        return repository.findAllByBigIntegerRecord(param);
    }

    @GetMapping("findAllByDoubleBean/{param}")
    List<SampleEntity> findAllByDoubleBean(@PathVariable DoubleBean param) {
        return repository.findAllByDoubleBean(param);
    }

    @GetMapping("findAllByDoubleRecord/{param}")
    List<SampleEntity> findAllByDoubleRecord(@PathVariable DoubleRecord param) {
        return repository.findAllByDoubleRecord(param);
    }

    @GetMapping("findAllByFloatBean/{param}")
    List<SampleEntity> findAllByFloatBean(@PathVariable FloatBean param) {
        return repository.findAllByFloatBean(param);
    }

    @GetMapping("findAllByFloatRecord/{param}")
    List<SampleEntity> findAllByFloatRecord(@PathVariable FloatRecord param) {
        return repository.findAllByFloatRecord(param);
    }

    @GetMapping("findAllByIntegerBean/{param}")
    List<SampleEntity> findAllByIntegerBean(@PathVariable IntegerBean param) {
        return repository.findAllByIntegerBean(param);
    }

    @GetMapping("findAllByIntegerRecord/{param}")
    List<SampleEntity> findAllByIntegerRecord(@PathVariable IntegerRecord param) {
        return repository.findAllByIntegerRecord(param);
    }

    @GetMapping("findAllByLongBean/{param}")
    List<SampleEntity> findAllByLongBean(@PathVariable LongBean param) {
        return repository.findAllByLongBean(param);
    }

    @GetMapping("findAllByLongRecord/{param}")
    List<SampleEntity> findAllByLongRecord(@PathVariable LongRecord param) {
        return repository.findAllByLongRecord(param);
    }

    @GetMapping("findAllByShortBean/{param}")
    List<SampleEntity> findAllByShortBean(@PathVariable ShortBean param) {
        return repository.findAllByShortBean(param);
    }

    @GetMapping("findAllByShortRecord/{param}")
    List<SampleEntity> findAllByShortRecord(@PathVariable ShortRecord param) {
        return repository.findAllByShortRecord(param);
    }

    // Strings
    @GetMapping("findAllByStringBean/{param}")
    List<SampleEntity> findAllByStringBean(@PathVariable StringBean param) {
        return repository.findAllByStringBean(param);
    }

    @GetMapping("findAllByStringRecord/{param}")
    List<SampleEntity> findAllByStringRecord(@PathVariable StringRecord param) {
        return repository.findAllByStringRecord(param);
    }

    @GetMapping("findAllByUuidBean/{param}")
    List<SampleEntity> findAllByUuidBean(@PathVariable UuidBean param) {
        return repository.findAllByUuidBean(param);
    }

    @GetMapping("findAllByUuidRecord/{param}")
    List<SampleEntity> findAllByUuidRecord(@PathVariable UuidRecord param) {
        return repository.findAllByUuidRecord(param);
    }
}
