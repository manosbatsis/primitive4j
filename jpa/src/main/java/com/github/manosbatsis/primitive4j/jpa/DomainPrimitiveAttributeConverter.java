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
package com.github.manosbatsis.primitive4j.jpa;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import jakarta.persistence.AttributeConverter;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

/**
 * Convenient base class when implementing JPA converters
 * for specific {@link DomainPrimitive} types.
 */
@RequiredArgsConstructor
public abstract class DomainPrimitiveAttributeConverter<T extends DomainPrimitive<I>, I extends Serializable>
        implements AttributeConverter<T, I> {

    private final Class<T> attributeType;
    private final Class<I> wrappedType;

    @Override
    public I convertToDatabaseColumn(T attribute) {
        try {
            return attribute.value();
        } catch (Exception e) {
            throw DomainPrimitiveConversionException.errorConvertingToDatabaseColumn(e);
        }
    }

    @Override
    public T convertToEntityAttribute(I dbData) {
        try {
            return attributeType.getConstructor(wrappedType).newInstance(dbData);
        } catch (Exception e) {
            throw DomainPrimitiveConversionException.errorConvertingToEntityAttribute(attributeType, e);
        }
    }
}
