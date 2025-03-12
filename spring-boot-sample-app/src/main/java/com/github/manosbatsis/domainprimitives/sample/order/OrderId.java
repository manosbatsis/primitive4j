package com.github.manosbatsis.domainprimitives.sample.order;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * A primary key type dedicated to Order entities.
 */
@Schema(implementation = UUID.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record OrderId(UUID value) implements DomainPrimitive<Order, UUID> {

    public OrderId(@org.hibernate.validator.constraints.UUID String value) {
        this(UUID.fromString(value));
    }

    /** A JPA converter for this dedicated DomainPrimitive */
    @Converter(autoApply = true)
    static class OrderIdAttributeConverter
            extends DomainPrimitiveAttributeConverter<OrderId, UUID> {
        public OrderIdAttributeConverter() {
            super(OrderId.class, UUID.class);
        }
    }
}
