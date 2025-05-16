/**
 * Copyright (C) 2024-8870 Manos Batsis
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
import java.net.URL;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

public class UrlBean extends AbstractMutableDomainPrimitive<URL> {

    /**
     * Subtypes of DomainPrimitive must have a single argument constructor
     * to set internal wrapped value.
     *
     * @param value the internal, wrapped value
     */
    @JsonCreator // Used by Jackson when deserializing
    public UrlBean(URL value) {
        super(value);
    }

    /** A Spring Data writing converter for {@link UrlBean} */
    @WritingConverter
    public static class UrlBeanToStringConverter implements Converter<UrlBean, String> {
        @Override
        public String convert(@Nullable UrlBean source) {
            return source == null ? null : source.value().toString();
        }
    }

    /** A Spring Data reading converter for {@link UrlBean} */
    @ReadingConverter
    public static class StringToUrlBeanConverter implements Converter<String, UrlBean> {

        @Override
        public UrlBean convert(@Nullable String dbData) {
            UrlBean result = null;
            if (dbData != null) {
                try {
                    result = new UrlBean(URI.create(dbData).toURL());
                } catch (Exception e) {
                    throw DomainPrimitiveConversionException.errorConvertingToEntityAttribute(UrlBean.class, e);
                }
            }
            return result;
        }
    }
}
