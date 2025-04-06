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

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Creates converters for cases where the source type is a String VS the actual value type of the
 * target {@link DomainPrimitive} implementation.
 */
@Slf4j
@RequiredArgsConstructor
public class StringToDomainPrimitiveConverterFactory<T extends DomainPrimitive<?>>
        implements ConverterFactory<String, T> {

    private final ConversionService conversionService;

    @Override
    public <T1 extends T> Converter<String, T1> getConverter(Class<T1> targetType) {
        log.info("getConverter CALLED");
        Class<?> targetInnerValueClass = GenericTypeResolver.resolveTypeArgument(targetType, DomainPrimitive.class);
        if (Objects.isNull(targetInnerValueClass)) {
            throw new IllegalArgumentException("Could not resolve DomainPrimitive's type argument for class: %s"
                    .formatted(targetType.getCanonicalName()));
        }
        return new StringToDomainPrimitiveConverter<>(targetType, targetInnerValueClass, conversionService);
    }
}
