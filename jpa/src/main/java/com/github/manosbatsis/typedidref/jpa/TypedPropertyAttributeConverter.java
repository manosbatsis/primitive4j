package com.github.manosbatsis.typedidref.jpa;

import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;

import jakarta.persistence.AttributeConverter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TypedPropertyAttributeConverter<T extends AbstractTypedProperty<?, I>, I>
        implements AttributeConverter<T, I> {

    private final Class<T> attributeType;
    private final Class<I> wrappedType;

    @Override
    public I convertToDatabaseColumn(T attribute) {
        try {
            return attribute.getValue();
        } catch (Exception e) {
            throw new TypedPropertyConversionException(
                    "Error converting object to database column value", e);
        }
    }

    @Override
    public T convertToEntityAttribute(I dbData) {
        try {
            return attributeType.getConstructor(wrappedType).newInstance(dbData);
        } catch (Exception e) {
            throw new TypedPropertyConversionException(
                    "Error converting database column value to %s object"
                            .formatted(attributeType.getCanonicalName()),
                    e);
        }
    }
}
