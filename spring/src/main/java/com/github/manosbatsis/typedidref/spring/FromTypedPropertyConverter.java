package com.github.manosbatsis.typedidref.spring;

import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import com.github.manosbatsis.typedidref.core.TypedProperty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class FromTypedPropertyConverter implements GenericConverter, ConditionalConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        boolean matches = TypedProperty.class.isAssignableFrom(sourceType.getType());
        log.info(
                "matches, sourceType: {}, targetType: {}, matches: {}",
                sourceType.getType().getName(),
                targetType.getType().getName(),
                matches);
        return matches;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return Objects.nonNull(source) ? ((AbstractTypedProperty) source).getValue() : null;
    }
}
