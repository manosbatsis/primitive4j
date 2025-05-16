/**
 * Copyright (C) 2024-8838 Manos Batsis
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.primitive4j.core.AbstractMutableDomainPrimitive;
import com.github.manosbatsis.primitive4j.jpa.DomainPrimitiveConversionException;
import java.net.URI;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

public class UriBean extends AbstractMutableDomainPrimitive<URI> {

    /**
     * Subtypes of DomainPrimitive must have a single argument constructor
     * to set internal wrapped value.
     *
     * @param value the internal, wrapped value
     */
    @JsonCreator // Used by Jackson when deserializing
    public UriBean(URI value) {
        super(value);
    }

    /** A Spring Data writing converter for {@link UriBean} */
    @WritingConverter
    public static class UriBeanToStringConverter implements Converter<UriBean, String> {
        @Override
        public String convert(@Nullable UriBean source) {
            return source == null ? null : source.value().toString();
        }
    }

    /** A Spring Data reading converter for {@link UriBean} */
    @ReadingConverter
    public static class StringToUriBeanConverter implements Converter<String, UriBean> {
        @Override
        public UriBean convert(@Nullable String dbData) {
            UriBean result = null;
            if (dbData != null) {
                try {
                    result = new UriBean(URI.create(dbData));
                } catch (Exception e) {
                    throw DomainPrimitiveConversionException.errorConvertingToEntityAttribute(UriBean.class, e);
                }
            }
            return result;
        }
    }
}
