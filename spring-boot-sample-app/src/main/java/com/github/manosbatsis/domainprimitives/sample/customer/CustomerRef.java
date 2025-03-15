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

    /**
     * A JPA converter for {@link CustomerRef}
     */
    @Converter(autoApply = true)
    static class CustomerRefAttributeConverter
            extends DomainPrimitiveAttributeConverter<CustomerRef, String> {
        public CustomerRefAttributeConverter() {
            super(CustomerRef.class, String.class);
        }
    }
}
