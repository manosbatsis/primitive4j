package com.github.manosbatsis.typedidref.spring;

import com.github.manosbatsis.typedidref.core.TypedProperty;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.GenericConverter;

@Slf4j
public class ToTypedPropertyConverter implements GenericConverter, ConditionalConverter {

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return null;
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    boolean matches = TypedProperty.class.isAssignableFrom(targetType.getType());
    log.info(
        "matches, sourceType: {}, targetType: {}, matches: {}",
        sourceType.getType().getName(),
        targetType.getType().getName(),
        matches);
    return matches;
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    try {
      return Objects.nonNull(source)
          ? targetType
              .getObjectType()
              .getConstructor(sourceType.getObjectType())
              .newInstance(source)
          : null;
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new TypedPropertyConversionException("Failed converting", e);
    }
  }
}
