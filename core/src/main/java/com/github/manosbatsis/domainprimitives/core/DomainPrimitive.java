package com.github.manosbatsis.domainprimitives.core;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 *
 * @param <T> the subject type of this dedicated type, e.g. Customer for CustomerRef respectively.
 * @param <I> the wrapped/inner value type
 */
public interface DomainPrimitive<T, I extends Serializable> extends Serializable {

    @JsonValue
    I value();
}
