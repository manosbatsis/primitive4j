package com.github.manosbatsis.domainprimitives.sample.product;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Converter;

import java.util.UUID;

/** A primary key type dedicated to Product entities. */
@Schema(implementation = UUID.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record ProductId(UUID value) implements DomainPrimitive<UUID> {

    public ProductId(@org.hibernate.validator.constraints.UUID String value) {
        this(UUID.fromString(value));
    }

    /** A JPA converter for {@link ProductId} */
    @Converter(autoApply = true)
    static class ProductIdAttributeConverter
            extends DomainPrimitiveAttributeConverter<ProductId, UUID> {
        public ProductIdAttributeConverter() {
            super(ProductId.class, UUID.class);
        }
    }
}
