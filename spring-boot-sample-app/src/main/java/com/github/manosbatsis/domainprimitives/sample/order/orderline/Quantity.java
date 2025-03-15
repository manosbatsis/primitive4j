package com.github.manosbatsis.domainprimitives.sample.order.orderline;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Converter;
import jakarta.validation.constraints.Positive;

/**
 * The quantity of products or materials in an {@link OrderLine}.
 * Not to be confused with the units of measure as packaged or otherwise provided
 * by the product.
 */
@Schema(implementation = Integer.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record Quantity(@Positive Integer value) implements DomainPrimitive<Integer> {

    /** A JPA converter for {@link Quantity} */
    @Converter(autoApply = true)
    static class QuantityAttributeConverter
            extends DomainPrimitiveAttributeConverter<Quantity, Integer> {
        public QuantityAttributeConverter() {
            super(Quantity.class, Integer.class);
        }
    }
}
