package com.github.manosbatsis.domainprimitives.core;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Customer {

    static class CustomerId extends AbstractDomainPrimitive<String> {
        public CustomerId(String value) {
            super(value);
        }
    }

    private CustomerId id;
    private String name;
}
