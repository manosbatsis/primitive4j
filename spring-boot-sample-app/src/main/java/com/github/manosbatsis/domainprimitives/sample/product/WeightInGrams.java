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
    static class WeightInGramsAttributeConverter
            extends DomainPrimitiveAttributeConverter<WeightInGrams, Integer> {
        public WeightInGramsAttributeConverter() {
            super(WeightInGrams.class, Integer.class);
        }
    }
}
