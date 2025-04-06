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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@RequiredArgsConstructor
public class StringToDomainPrimitiveConverter<T extends DomainPrimitive<?>> implements Converter<String, T> {

    private final Class<T> targetType;
    private final Class<?> innerValueType;
    private final ConversionService conversionService;

    @Override
    public T convert(String source) {
        log.info("convert CALLED for value: {}", source);

        if (!conversionService.canConvert(source.getClass(), innerValueType)) {
            throw new IllegalArgumentException("Provided conversion service unable to convert from %s to %s"
                    .formatted(source.getClass().getCanonicalName(), innerValueType.getCanonicalName()));
        }

        try {
            var sourceAsInnerValueType = conversionService.convert(source, innerValueType);
            return targetType.getConstructor(innerValueType).newInstance(sourceAsInnerValueType);
        } catch (Exception e) {
            throw new DomainPrimitiveConversionException("Failed converting", e);
        }
    }
}
