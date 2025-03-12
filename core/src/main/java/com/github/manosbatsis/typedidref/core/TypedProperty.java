package com.github.manosbatsis.typedidref.core;

import java.io.Serializable;

/**
 *
 * @param <T> the subject of this dedicated type, e.g. Customer for CustomerRef respectively.
 * @param <ID> the wrapped value type
 */
public interface TypedProperty<T, ID> extends Serializable {

    Class<T> getContainerType();

    ID getValue();
}
