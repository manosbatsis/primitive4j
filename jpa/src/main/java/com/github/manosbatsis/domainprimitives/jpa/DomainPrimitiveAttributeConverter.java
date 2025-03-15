package com.github.manosbatsis.domainprimitives.jpa;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;

import jakarta.persistence.AttributeConverter;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public abstract class DomainPrimitiveAttributeConverter<
                T extends DomainPrimitive<I>, I extends Serializable>
        implements AttributeConverter<T, I> {

    private final Class<T> attributeType;
    private final Class<I> wrappedType;

    @Override
    public I convertToDatabaseColumn(T attribute) {
        try {
            return attribute.value();
        } catch (Exception e) {
            throw new DomainPrimitiveConversionException(
                    "Error converting object to database column value", e);
        }
    }

    @Override
    public T convertToEntityAttribute(I dbData) {
        try {
            return attributeType.getConstructor(wrappedType).newInstance(dbData);
        } catch (Exception e) {
            throw new DomainPrimitiveConversionException(
                    "Error converting database column value to %s object"
                            .formatted(attributeType.getCanonicalName()),
                    e);
        }
    }
}
