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
package com.github.manosbatsis.domainprimitives.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.domainprimitives.core.Sdp4jType;
import com.github.manosbatsis.domainprimitives.sample.sampleentity.SampleEntity;
import com.github.manosbatsis.domainprimitives.sample.sampleentity.SampleEntityController;
import com.github.manosbatsis.domainprimitives.sample.sampleentity.SampleEntityService;
import com.github.manosbatsis.domainprimitives.test.common.example.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Disabled;
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
@AutoConfigureWebTestClient
class SampleEntityControllerTest {

    private static Faker faker = new Faker();
    private static SampleEntity sampleInstance = buildSampleEntity();

    private static Stream<Arguments> sampleInstanceUrlFragments() {
        return Stream.of(
                // Network
                Arguments.of("findAllByUriRecord", sampleInstance.getUriRecord()),
                Arguments.of("findAllByUriBean", sampleInstance.getUriBean()),
                Arguments.of("findAllByUrlBean", sampleInstance.getUrlBean()),
                Arguments.of("findAllByUrlRecord", sampleInstance.getUrlRecord()),
                // TODO
                // Numbers
                //                Arguments.of("findAllByBigDecimalBean", sampleInstance.getBigDecimalBean()),
                //                Arguments.of("findAllByBigDecimalRecord", sampleInstance.getBigDecimalRecord()),
                //                Arguments.of("findAllByBigIntegerBean", sampleInstance.getBigIntegerBean()),
                //                Arguments.of("findAllByBigIntegerRecord", sampleInstance.getBigIntegerRecord()),
                //                Arguments.of("findAllByDoubleBean", sampleInstance.getDoubleBean()),
                //                Arguments.of("findAllByDoubleRecord", sampleInstance.getDoubleRecord()),
                //                Arguments.of("findAllByFloatBean", sampleInstance.getFloatBean()),
                //                Arguments.of("findAllByFloatRecord", sampleInstance.getFloatRecord()),
                //                Arguments.of("findAllByIntegerBean", sampleInstance.getIntegerBean()),
                //                Arguments.of("findAllByIntegerRecord", sampleInstance.getIntegerRecord()),
                Arguments.of("findAllByLongBean", sampleInstance.getLongBean()),
                Arguments.of("findAllByLongRecord", sampleInstance.getLongRecord()),
                Arguments.of("findAllByShortBean", sampleInstance.getShortBean()),
                Arguments.of("findAllByShortRecord", sampleInstance.getShortRecord()),
                // Strings
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
    @Disabled
    void shouldBeAbleToSearchByAnySimplePrimitiveProperty(String pathFragment, Sdp4jType<?> valueFragment) {
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

        assertThat(searchResults).isNotNull().isNotEmpty().hasSize(1);
        assertThat(searchResults.getFirst()).isEqualTo(sampleInstance);
    }

    @SneakyThrows
    private SampleEntity postSampleEntity(SampleEntity instance) {
        var pestisted = webTestClient
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
        instance.setId(pestisted.getId());

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
                        new BigDecimalBean(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 999))))
                .bigDecimalRecord(
                        new BigDecimalRecord(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 999))))
                .bigIntegerBean(
                        new BigIntegerBean(BigInteger.valueOf(faker.number().randomNumber())))
                .bigIntegerRecord(
                        new BigIntegerRecord(BigInteger.valueOf(faker.number().randomNumber())))
                .doubleBean(new DoubleBean(faker.number().randomDouble(2, 1, 999)))
                .doubleRecord(new DoubleRecord(faker.number().randomDouble(2, 1, 999)))
                .floatBean(new FloatBean((float) faker.number().randomDouble(2, 1, 999)))
                .floatRecord(new FloatRecord((float) faker.number().randomDouble(2, 1, 999)))
                .integerBean(new IntegerBean(faker.number().randomDigit()))
                .integerRecord(new IntegerRecord(faker.number().randomDigit()))
                .longBean(new LongBean(faker.number().randomNumber()))
                .longRecord(new LongRecord(faker.number().randomNumber()))
                .shortBean(new ShortBean((short) faker.number().randomDigit()))
                .shortRecord(new ShortRecord((short) faker.number().randomDigit()))
                .stringBean(new StringBean(faker.internet().domainName()))
                .stringRecord(new StringRecord(faker.internet().domainName()))
                .uuidBean(new UuidBean(UUID.randomUUID()))
                .uuidRecord(new UuidRecord(UUID.randomUUID()))
                .build();
    }
}
