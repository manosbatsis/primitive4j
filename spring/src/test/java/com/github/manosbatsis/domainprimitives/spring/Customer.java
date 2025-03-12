package com.github.manosbatsis.domainprimitives.spring;

import com.github.manosbatsis.domainprimitives.core.AbstractDomainPrimitive;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Customer {

    static class CustomerId extends AbstractDomainPrimitive<Customer, String> {
        public CustomerId(String value) {
            super(value);
        }
    }

    private CustomerId id;
    private String name;
}
