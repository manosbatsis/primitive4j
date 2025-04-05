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
package com.github.manosbatsis.domainprimitives.test.common.example.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.domainprimitives.core.AbstractMutableSdp4jType;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveConversionException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;
import java.net.URL;

public class UrlBean extends AbstractMutableSdp4jType<URL> {

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

    /**
     * A JPA converter for {@link UrlBean}
     */
    @Converter(autoApply = true)
    public static class UrlBeanAttributeConverter implements AttributeConverter<UrlBean, String> {

        @Override
        public String convertToDatabaseColumn(UrlBean attribute) {
            return attribute == null ? null : attribute.value().toString();
        }

        @Override
        public UrlBean convertToEntityAttribute(String dbData) {
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
