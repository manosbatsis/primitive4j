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
package com.github.manosbatsis.primitive4j.spring;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import com.github.manosbatsis.primitive4j.test.common.example.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        classes = {TestConfiguration.class},
        properties = {
            "logging.level.org.springframework.web=debug",
            "logging.level.root=info",
            "logging.level.com.github.manosbatsis.primitive4j=debug"
        })
class ConversionServiceTest {

    static Stream<Arguments> conversionData() throws MalformedURLException, URISyntaxException {
        return SampleData.convertibles().map(it -> {
            var valueClassName = it.value().getClass().getSimpleName();
            var domainPrimitiveClassName = it.domainPrimitiveClass().getSimpleName();
            return Arguments.arguments(
                    Named.named(valueClassName, it.value()),
                    Named.named(domainPrimitiveClassName, it.domainPrimitiveClass()));
        });
    }

    @Autowired
    private ConversionService conversionService;

    @ParameterizedTest(name = "Should convert {0} to {1}")
    @MethodSource("conversionData")
    @SneakyThrows
    <T extends Serializable> void whenConvertringTo_thenNoExceptions(
            T wrappedValue, Class<? extends DomainPrimitive<T>> domainPrimitiveClass) {
        var expectedValueClass = wrappedValue.getClass();
        // Assert our converters recognize the conversion pairs, include
        // string as input
        assertThat(conversionService.canConvert(expectedValueClass, domainPrimitiveClass))
                .isTrue();

        // Assert our primitives work as expected
        DomainPrimitive<T> expectedDomainPrimitive =
                domainPrimitiveClass.getConstructor(expectedValueClass).newInstance(wrappedValue);
        assertThat(expectedDomainPrimitive.value()).isEqualTo(wrappedValue);

        // Assert conversion is accurate
        var actualConversionResult = conversionService.convert(wrappedValue, domainPrimitiveClass);
        assertThat(actualConversionResult).isNotNull().isEqualTo(expectedDomainPrimitive);
        assertThat(actualConversionResult.value()).isEqualTo(wrappedValue);
    }

    @ParameterizedTest(name = "Should convert String to {1}")
    @MethodSource("conversionData")
    @SneakyThrows
    <T extends Serializable> void whenConvertringStringTo_thenNoExceptions(
            T wrappedValue, Class<? extends DomainPrimitive<T>> domainPrimitiveClass) {
        var expectedValueClass = wrappedValue.getClass();
        // Assert our converters recognize the conversion pairs, include
        // string as input
        assertThat(conversionService.canConvert(String.class, domainPrimitiveClass))
                .isTrue();

        // Assert our primitives work as expected
        DomainPrimitive<T> expectedDomainPrimitive =
                domainPrimitiveClass.getConstructor(expectedValueClass).newInstance(wrappedValue);
        assertThat(expectedDomainPrimitive.value()).isEqualTo(wrappedValue);

        // Assert conversion is accurate
        var serializedValue = conversionService.convert(wrappedValue, String.class);
        var actualConversionResult = conversionService.convert(serializedValue, domainPrimitiveClass);
        assertThat(actualConversionResult).isNotNull().isEqualTo(expectedDomainPrimitive);
        assertThat(actualConversionResult.value()).isEqualTo(wrappedValue);
    }

    @ParameterizedTest(name = "Should convert {1} to {0}")
    @MethodSource("conversionData")
    @SneakyThrows
    <T extends Serializable> void whenConvertringFrom_thenNoExceptions(
            T wrappedValue, Class<? extends DomainPrimitive<T>> domainPrimitiveClass) {
        var expectedValueClass = wrappedValue.getClass();
        // Assert our converters recognize the conversion pairs, include
        // string as output
        assertThat(conversionService.canConvert(domainPrimitiveClass, String.class))
                .isTrue();
        assertThat(conversionService.canConvert(domainPrimitiveClass, expectedValueClass))
                .isTrue();

        // Assert our primitives work as expected
        DomainPrimitive<T> domainPrimitive =
                domainPrimitiveClass.getConstructor(expectedValueClass).newInstance(wrappedValue);
        assertThat(domainPrimitive.value()).isEqualTo(wrappedValue);

        // Assert conversion is accurate
        var actualConversionResult = conversionService.convert(domainPrimitive, wrappedValue.getClass());
        assertThat(actualConversionResult).isNotNull().isEqualTo(wrappedValue);
    }
}
