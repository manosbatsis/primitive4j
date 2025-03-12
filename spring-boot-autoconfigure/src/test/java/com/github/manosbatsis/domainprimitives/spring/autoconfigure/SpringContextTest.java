package com.github.manosbatsis.domainprimitives.spring.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = DomainPrimitivesAutoConfiguration.class)
class SpringContextTest {

    @Autowired
    private DomainPrimitivesProperties properties;

    @Test
    void whenSpringContextIsBootstrapped_thenNoExceptions() {
        assertThat(properties).isNotNull();
    }
}
