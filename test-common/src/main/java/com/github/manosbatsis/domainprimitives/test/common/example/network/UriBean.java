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
package com.github.manosbatsis.domainprimitives.test.common.example.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.domainprimitives.core.AbstractMutableDomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveConversionException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;

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

    /**
     * A JPA converter for {@link UriBean}
     */
    @Converter(autoApply = true)
    public static class UriBeanAttributeConverter implements AttributeConverter<UriBean, String> {

        @Override
        public String convertToDatabaseColumn(UriBean attribute) {
            return attribute == null ? null : attribute.value().toString();
        }

        @Override
        public UriBean convertToEntityAttribute(String dbData) {
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
