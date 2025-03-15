package com.github.manosbatsis.domainprimitives.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @param <I> the primitive wrapped value
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class AbstractDomainPrimitive<I extends Serializable>
        implements DomainPrimitive<I> {
    private final I value;

    @Override
    public I value() {
        return getValue();
    }
}
