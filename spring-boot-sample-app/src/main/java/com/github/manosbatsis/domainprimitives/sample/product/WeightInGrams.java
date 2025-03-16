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
package com.github.manosbatsis.domainprimitives.sample.product;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Converter;
import jakarta.validation.constraints.PositiveOrZero;

/** Weight in grams, a metric unit of mass equal to one thousandth of a kilogram.  */
@Schema(implementation = Integer.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record WeightInGrams(@PositiveOrZero Integer value) implements DomainPrimitive<Integer> {

    /** A JPA converter for {@link WeightInGrams} */
    @Converter(autoApply = true)
    static class WeightInGramsAttributeConverter extends DomainPrimitiveAttributeConverter<WeightInGrams, Integer> {
        public WeightInGramsAttributeConverter() {
            super(WeightInGrams.class, Integer.class);
        }
    }
}
