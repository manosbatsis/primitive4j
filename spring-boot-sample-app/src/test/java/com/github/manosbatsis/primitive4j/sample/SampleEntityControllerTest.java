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
package com.github.manosbatsis.primitive4j.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import com.github.manosbatsis.primitive4j.sample.sampleentity.SampleEntity;
import com.github.manosbatsis.primitive4j.sample.sampleentity.SampleEntityController;
import com.github.manosbatsis.primitive4j.sample.sampleentity.SampleEntityService;
import com.github.manosbatsis.primitive4j.test.common.example.*;
import com.github.manosbatsis.primitive4j.test.common.example.network.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "PT10S")
class SampleEntityControllerTest {

    private static final Faker faker = new Faker();
    private static final SampleEntity sampleInstance = buildSampleEntity();

    private static Stream<Arguments> sampleInstanceUrlFragments() {
        return Stream.of(
                // Network
                // Arguments.of("findAllByUriRecord", sampleInstance.getUriRecord()),
                // Arguments.of("findAllByUriBean", sampleInstance.getUriBean()),
                // Arguments.of("findAllByUrlBean", sampleInstance.getUrlBean()),
                // Arguments.of("findAllByUrlRecord", sampleInstance.getUrlRecord()),
                // Numbers
                Arguments.of("findAllByBigDecimalBean", sampleInstance.getBigDecimalBean()),
                Arguments.of("findAllByBigDecimalRecord", sampleInstance.getBigDecimalRecord()),
                Arguments.of("findAllByBigIntegerBean", sampleInstance.getBigIntegerBean()),
                Arguments.of("findAllByBigIntegerRecord", sampleInstance.getBigIntegerRecord()),
                Arguments.of("findAllByDoubleBean", sampleInstance.getDoubleBean()),
                Arguments.of("findAllByDoubleRecord", sampleInstance.getDoubleRecord()),
                Arguments.of("findAllByFloatBean", sampleInstance.getFloatBean()),
                Arguments.of("findAllByFloatRecord", sampleInstance.getFloatRecord()),
                Arguments.of("findAllByIntegerBean", sampleInstance.getIntegerBean()),
                Arguments.of("findAllByIntegerRecord", sampleInstance.getIntegerRecord()),
                Arguments.of("findAllByLongBean", sampleInstance.getLongBean()),
                Arguments.of("findAllByLongRecord", sampleInstance.getLongRecord()),
                Arguments.of("findAllByShortBean", sampleInstance.getShortBean()),
                Arguments.of("findAllByShortRecord", sampleInstance.getShortRecord()),
                //                // Strings
                Arguments.of("findAllByStringBean", sampleInstance.getStringBean()),
                Arguments.of("findAllByStringRecord", sampleInstance.getStringRecord()),
                Arguments.of("findAllByUuidBean", sampleInstance.getUuidBean()),
                Arguments.of("findAllByUuidRecord", sampleInstance.getUuidRecord()));
    }

    @Autowired
    private SampleEntityService sampleEntityService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldBeAbleToPersistSearchByAnySimplePrimitiveProperty() {
        // Create an entity instance with random values
        var persisted = postSampleEntity(buildSampleEntity());
        assertThat(persisted).isNotNull();
    }

    @ParameterizedTest(name = "Search {0}")
    @MethodSource("sampleInstanceUrlFragments")
    void shouldBeAbleToSearchByAnySimplePrimitiveProperty(String pathFragment, DomainPrimitive<?> valueFragment) {
        // Create an entity instance with fixed random values
        sampleEntityService.save(sampleInstance);

        // Create a few more
        postSampleEntity(buildSampleEntity());
        postSampleEntity(buildSampleEntity());
        // Search by each property
        var searchResults = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SampleEntityController.BASE_PATH + "/" + pathFragment + "/{param}")
                        .build(valueFragment.value()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(SampleEntity.class)
                .returnResult()
                .getResponseBody();

        assertThat(searchResults).isNotNull().isNotEmpty().anySatisfy(it -> assertThat(it)
                .usingRecursiveComparison()
                .isEqualTo(sampleInstance));
    }

    @SneakyThrows
    private SampleEntity postSampleEntity(SampleEntity instance) {
        var persisted = webTestClient
                .post()
                .uri(SampleEntityController.BASE_PATH)
                .bodyValue(instance)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(SampleEntity.class)
                .returnResult()
                .getResponseBody();
        assert persisted != null;
        instance.setId(persisted.getId());

        return instance;
    }

    @SneakyThrows
    private static SampleEntity buildSampleEntity() {
        return SampleEntity.builder()
                .uriRecord(new UriRecord(new URI(faker.internet().url())))
                .uriBean(new UriBean(new URI(faker.internet().url())))
                .urlBean(new UrlBean(new URI(faker.internet().url()).toURL()))
                .urlRecord(new UrlRecord(new URI(faker.internet().url()).toURL()))
                .bigDecimalBean(
                        new BigDecimalBean(BigDecimal.valueOf(faker.number().randomDouble(1, 1, 999))))
                .bigDecimalRecord(
                        new BigDecimalRecord(BigDecimal.valueOf(faker.number().randomDouble(1, 1, 999))))
                .bigIntegerBean(
                        new BigIntegerBean(BigInteger.valueOf(faker.number().randomNumber())))
                .bigIntegerRecord(
                        new BigIntegerRecord(BigInteger.valueOf(faker.number().randomNumber())))
                .doubleBean(new DoubleBean(
                        truncateDecimal(faker.random().nextDouble()).doubleValue()))
                .doubleRecord(new DoubleRecord(
                        truncateDecimal(faker.random().nextDouble()).doubleValue()))
                .floatBean(new FloatBean(
                        truncateDecimal(faker.random().nextFloat()).floatValue()))
                .floatRecord(new FloatRecord(
                        truncateDecimal(faker.random().nextFloat()).floatValue()))
                .integerBean(new IntegerBean(faker.random().nextInt()))
                .integerRecord(new IntegerRecord(faker.random().nextInt()))
                .longBean(new LongBean(faker.random().nextLong()))
                .longRecord(new LongRecord(faker.random().nextLong()))
                .shortBean(new ShortBean((short) faker.random().nextInt(Short.MAX_VALUE)))
                .shortRecord(new ShortRecord((short) faker.random().nextInt(Short.MAX_VALUE)))
                .stringBean(new StringBean(faker.internet().domainName()))
                .stringRecord(new StringRecord(faker.internet().domainName()))
                .uuidBean(new UuidBean(UUID.randomUUID()))
                .uuidRecord(new UuidRecord(UUID.randomUUID()))
                .build();
    }

    private static BigDecimal truncateDecimal(final Number x) {
        return new BigDecimal(String.valueOf(x)).setScale(1, RoundingMode.FLOOR);
    }
}
