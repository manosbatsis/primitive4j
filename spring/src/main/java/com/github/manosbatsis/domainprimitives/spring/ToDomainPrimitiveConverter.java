/**
 * Copyright (C) 2024-2025 Manos Batsis
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
 * <a href="https://www.gnu.org/licenses/lgpl-3.0.html">https://www.gnu.org/licenses/lgpl-3.0.html</a>.
 */
package com.github.manosbatsis.domainprimitives.spring;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;

@Slf4j
public class ToDomainPrimitiveConverter implements GenericConverter, ConditionalConverter {

    @Override
    @Nullable
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    @Nullable
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        log.info("convert CALLED");
        try {
            return Objects.nonNull(source)
                    ? targetType
                            .getObjectType()
                            .getConstructor(sourceType.getObjectType())
                            .newInstance(source)
                    : null;
        } catch (Exception e) {
            throw new DomainPrimitiveConversionException("Failed converting", e);
        }
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        var targetClass = targetType.getObjectType();
        // Target must implement DomainPrimitive...
        if (DomainPrimitive.class.isAssignableFrom(targetClass)) {
            // and have the same value type as sourceType or a compatible constructor
            var sourceClass = sourceType.getObjectType();
            Class<?> targetInnerValueClass =
                    GenericTypeResolver.resolveTypeArgument(targetClass, DomainPrimitive.class);
            return Objects.nonNull(targetInnerValueClass)
                    && (targetInnerValueClass.isAssignableFrom(sourceClass)
                    || hasRelevantConstructor(targetType, sourceClass));
        }
        return false;
    }

    private static boolean hasRelevantConstructor(TypeDescriptor targetType, Class<?> sourceClass) {
        return Arrays.stream(targetType.getObjectType().getConstructors()).anyMatch(constr -> {
            var paraTypes = constr.getParameterTypes();
            return paraTypes.length == 1 && Arrays.stream(paraTypes).anyMatch(it -> it.isAssignableFrom(sourceClass));
        });
    }
}
