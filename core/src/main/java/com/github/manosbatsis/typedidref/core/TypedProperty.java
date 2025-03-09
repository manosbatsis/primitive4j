package com.github.manosbatsis.typedidref.core;

public interface TypedProperty<T, ID> {

  Class<T> getContainerType();

  ID getValue();
}
