package com.github.manosbatsis.domainprimitives.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @param <T> the entity type this typed property is used for, e.g. <code>Customer</code> for <code>
 *     CustomerId</code>
 * @param <I> the primitive wrapped value
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class AbstractDomainPrimitive<T, I extends Serializable>
        implements DomainPrimitive<T, I> {
    private final I value;

    @Override
    public I value() {
        return getValue();
    }
}
