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
package com.github.manosbatsis.domainprimitives.sample.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.domainprimitives.core.AbstractDomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Converter;

/** A business key type dedicated to Customer entities. */
@Schema(implementation = String.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public class CustomerRef extends AbstractDomainPrimitive<String> {

    /**
     * Annotated with @JsonCreator and used by Jackson when deserializing
     *
     * @param value the internal, wrapped value
     * @return a new instance wrapping the given value
     */
    @JsonCreator
    public static CustomerRef of(String value) {
        return new CustomerRef(value);
    }

    /**
     * Subclasses of DomainPrimitive must have a single argument constructor to set internal wrapped
     * value
     *
     * @param value the internal, wrapped value
     */
    public CustomerRef(String value) {
        super(value);
    }

    /** A JPA converter for {@link CustomerRef} */
    @Converter(autoApply = true)
    static class CustomerRefAttributeConverter extends DomainPrimitiveAttributeConverter<CustomerRef, String> {
        public CustomerRefAttributeConverter() {
            super(CustomerRef.class, String.class);
        }
    }
}
