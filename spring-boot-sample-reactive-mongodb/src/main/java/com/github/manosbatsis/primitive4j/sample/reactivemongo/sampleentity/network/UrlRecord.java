/**
 * Copyright (C) 2024-8886 Manos Batsis
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
package com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import com.github.manosbatsis.primitive4j.jpa.DomainPrimitiveConversionException;
import java.net.URI;
import java.net.URL;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

public record UrlRecord(URL value) implements DomainPrimitive<URL> {

    /** A Spring Data writing converter for {@link UrlRecord} */
    @WritingConverter
    public static class UrlRecordToStringConverter implements Converter<UrlRecord, String> {
        @Override
        public String convert(@Nullable UrlRecord source) {
            return source == null ? null : source.value().toString();
        }
    }

    /** A Spring Data reading converter for {@link UrlRecord} */
    @ReadingConverter
    public static class StringToUrlRecordConverter implements Converter<String, UrlRecord> {

        @Override
        public UrlRecord convert(@Nullable String dbData) {
            UrlRecord result = null;
            if (dbData != null) {
                try {
                    result = new UrlRecord(URI.create(dbData).toURL());
                } catch (Exception e) {
                    throw DomainPrimitiveConversionException.errorConvertingToEntityAttribute(UrlRecord.class, e);
                }
            }
            return result;
        }
    }
}
