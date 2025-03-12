package com.github.manosbatsis.domainprimitives.spring;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class ToDomainPrimitiveConverter implements GenericConverter, ConditionalConverter {

    @Override
    @Nullable
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return DomainPrimitive.class.isAssignableFrom(targetType.getType());
    }

    @Override
    @Nullable
    public Object convert(
            @Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
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
            throw new DomainPrimitiveConversionException("Failed converting", e);
        }
    }
}
