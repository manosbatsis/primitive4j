package com.github.manosbatsis.domainprimitives.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerTest {

    @Test
    void shouldBeAble() {
        String idValue = UUID.randomUUID().toString();
        Customer.CustomerId id = new Customer.CustomerId(idValue);
        assertThat(id).extracting("value").isEqualTo(idValue);
    }
}
