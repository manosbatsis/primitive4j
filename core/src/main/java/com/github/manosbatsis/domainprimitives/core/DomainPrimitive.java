package com.github.manosbatsis.domainprimitives.core;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @param <I> the wrapped, inner value type
 */
public interface DomainPrimitive<I extends Serializable> extends Serializable {

    @JsonValue
    I value();
}
