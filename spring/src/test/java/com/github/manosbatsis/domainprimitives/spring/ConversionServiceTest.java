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
package com.github.manosbatsis.domainprimitives.spring;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        classes = {TestConfiguration.class},
        properties = {
            "logging.level.org.springframework.web=debug",
            "logging.level.root=info",
            "logging.level.com.github.manosbatsis.domainprimitives=debug"
        })
class ConversionServiceTest {

    static Stream<SampleData.Convertible<? extends Serializable>> convertables()
            throws MalformedURLException, URISyntaxException {
        return SampleData.convertibles();
    }

    @Autowired
    private FormattingConversionService conversionService;

    @Autowired
    private FromDomainPrimitiveConverter fromDomainPrimitiveConverter;

    @Autowired
    private ToDomainPrimitiveConverter toTypedPropertyConverter;

    @BeforeEach
    void setUp() {
        conversionService.addConverter(fromDomainPrimitiveConverter);
        conversionService.addConverter(toTypedPropertyConverter);
    }

    @ParameterizedTest
    @MethodSource("convertables")
    @DisplayName("{0}")
    <T extends Serializable> void whenConvertringTo_thenNoExceptions(SampleData.Convertible<T> convertibleConfig) {
        T wrappedValue = convertibleConfig.value();

        var expectedValueClass = wrappedValue.getClass();
        try {
            // Assert our converters recognize the conversion pairs, include
            // string as input
            assertThat(conversionService.canConvert(String.class, convertibleConfig.domainPrimitiveClass()))
                    .isTrue();
            assertThat(conversionService.canConvert(expectedValueClass, convertibleConfig.domainPrimitiveClass()))
                    .isTrue();

            // Assert our primitives work as expected
            DomainPrimitive<T> expectedDomainPrimitive = (DomainPrimitive<T>) convertibleConfig
                    .domainPrimitiveClass()
                    .getConstructor(expectedValueClass)
                    .newInstance(wrappedValue);
            assertThat(expectedDomainPrimitive.value()).isEqualTo(wrappedValue);

            // Assert conversion is accurate
            var actualConversionResult =
                    conversionService.convert(wrappedValue, convertibleConfig.domainPrimitiveClass());
            assertThat(actualConversionResult).isNotNull();
            assertThat(actualConversionResult).isEqualTo(expectedDomainPrimitive);
            assertThat(actualConversionResult.value()).isEqualTo(wrappedValue);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("convertables")
    <T extends Serializable> void whenConvertringFrom_thenNoExceptions(SampleData.Convertible<T> convertibleConfig) {
        String expectedCustomerIdValue = UUID.randomUUID().toString();
        StringBeanPrimitive stringBeanPrimitive = new StringBeanPrimitive(expectedCustomerIdValue);
        assertThat(conversionService.canConvert(StringBeanPrimitive.class, String.class))
                .isTrue();
        String actual = conversionService.convert(stringBeanPrimitive, String.class);
        assertThat(actual).isEqualTo(expectedCustomerIdValue);
    }
}
