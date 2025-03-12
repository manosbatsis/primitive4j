package com.github.manosbatsis.typedidref.core;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Customer {

    static class CustomerId extends AbstractTypedProperty<Customer, String> {
        public CustomerId(String value) {
            super(Customer.class, value);
        }
    }

    private CustomerId id;
    private String name;
}
