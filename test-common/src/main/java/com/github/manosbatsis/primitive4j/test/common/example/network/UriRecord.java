/**
 * Copyright (C) 2024-8854 Manos Batsis
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
package com.github.manosbatsis.primitive4j.test.common.example.network;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import com.github.manosbatsis.primitive4j.jpa.DomainPrimitiveConversionException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;

public record UriRecord(URI value) implements DomainPrimitive<URI> {

    /**
     * A JPA converter for {@link UriRecord}
     */
    @Converter(autoApply = true)
    public static class UriRecordAttributeConverter implements AttributeConverter<UriRecord, String> {

        @Override
        public String convertToDatabaseColumn(UriRecord attribute) {
            return attribute == null ? null : attribute.value().toString();
        }

        @Override
        public UriRecord convertToEntityAttribute(String dbData) {
            UriRecord result = null;
            if (dbData != null) {
                try {
                    result = new UriRecord(URI.create(dbData));
                } catch (Exception e) {
                    throw DomainPrimitiveConversionException.errorConvertingToEntityAttribute(UriRecord.class, e);
                }
            }
            return result;
        }
    }
}
