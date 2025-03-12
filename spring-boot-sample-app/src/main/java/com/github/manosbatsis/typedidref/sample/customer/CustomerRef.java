package com.github.manosbatsis.typedidref.sample.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import com.github.manosbatsis.typedidref.jpa.TypedPropertyAttributeConverter;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Converter;

/**
 * A business key type dedicated to Customer entities.
 */
@Schema(implementation = String.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public class CustomerRef extends AbstractTypedProperty<Customer, String> {

    /**
     * Annotated with @JsonCreator and used by Jackson when deserializing
     * @param value the internal, wrapped value
     * @return a new instance wrapping the given value
     */
    @JsonCreator
    public static CustomerRef of(String value) {
        return new CustomerRef(value);
    }

    /**
     * Subclasses of TypedProperty must have a single argument constructor to set internal wrapped
     * value
     *
     * @param value the internal, wrapped value
     */
    public CustomerRef(String value) {
        super(Customer.class, value);
    }

    /** A JPA converter for this dedicated TypedProperty */
    @Converter(autoApply = true)
    static class CustomerRefConverter extends TypedPropertyAttributeConverter<CustomerRef, String> {
        public CustomerRefConverter() {
            super(CustomerRef.class, String.class);
        }
    }
}
