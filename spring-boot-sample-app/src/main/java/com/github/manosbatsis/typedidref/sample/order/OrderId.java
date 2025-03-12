package com.github.manosbatsis.typedidref.sample.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import com.github.manosbatsis.typedidref.jpa.TypedPropertyAttributeConverter;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * A primary key type dedicated to Order entities.
 */
@Schema(implementation = UUID.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public class OrderId extends AbstractTypedProperty<Order, UUID> {

    /**
     * Annotated with @JsonCreator and used by Jackson when deserializing
     * @param value the internal, wrapped value
     * @return a new instance wrapping the given value
     */
    @JsonCreator
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    /**
     * Subclasses of TypedProperty must have a single argument constructor to set internal wrapped
     * value
     *
     * @param value the internal, wrapped value
     */
    public OrderId(UUID value) {
        super(Order.class, value);
    }

    /** A JPA converter for this dedicated TypedProperty */
    @Converter(autoApply = true)
    static class OrderIdConverter extends TypedPropertyAttributeConverter<OrderId, UUID> {
        public OrderIdConverter() {
            super(OrderId.class, UUID.class);
        }
    }
}
