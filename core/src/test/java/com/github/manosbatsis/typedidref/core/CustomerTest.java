package com.github.manosbatsis.typedidref.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerTest {

    @Test
    void shouldBeAble() {
        String idValue = UUID.randomUUID().toString();
        Customer.CustomerId id = new Customer.CustomerId(idValue);
        Customer customer = new Customer(id, "name");
        assertThat(id)
                .extracting("containerType", "value")
                .containsExactly(customer.getClass(), idValue);
    }
}
