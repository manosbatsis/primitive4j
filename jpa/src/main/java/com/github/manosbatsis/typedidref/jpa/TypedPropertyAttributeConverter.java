package com.github.manosbatsis.typedidref.jpa;

import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import jakarta.persistence.AttributeConverter;

public abstract class TypedPropertyAttributeConverter<T extends AbstractTypedProperty<?, I>, I>
    implements AttributeConverter<T, I> {

  private final Class<T> typedPropertyClass;

  public TypedPropertyAttributeConverter(Class<T> typedPropertyClass) {
    this.typedPropertyClass = typedPropertyClass;
  }

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
      return typedPropertyClass.getConstructor(String.class).newInstance(dbData.toString());
    } catch (Exception e) {
      throw new TypedPropertyConversionException(
          "Error converting database column value to %s object"
              .formatted(typedPropertyClass.getCanonicalName()),
          e);
    }
  }
}
