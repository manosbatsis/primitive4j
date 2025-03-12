package com.github.manosbatsis.domainprimitives.spring;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class FromDomainPrimitiveConverter implements GenericConverter, ConditionalConverter {

    @Override
    @Nullable
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return DomainPrimitive.class.isAssignableFrom(sourceType.getType());
    }

    @Override
    @Nullable
    public Object convert(
            @Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return Objects.nonNull(source) ? ((DomainPrimitive<?, ?>) source).value() : null;
    }
}
