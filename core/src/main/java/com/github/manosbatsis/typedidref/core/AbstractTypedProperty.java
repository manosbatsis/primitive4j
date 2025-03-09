package com.github.manosbatsis.typedidref.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @param <T> the entity type this typed property is used for, e.g. <code>Customer</code> for <code>
 *     CustomerId</code>
 * @param <ID> the primitive wrapped value
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class AbstractTypedProperty<T, ID> implements TypedProperty<T, ID> {
  private final Class<T> containerType;
  private final ID value;
}
