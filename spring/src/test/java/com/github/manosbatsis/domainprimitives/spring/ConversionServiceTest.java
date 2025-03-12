package com.github.manosbatsis.domainprimitives.spring;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@Slf4j
@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        classes = {TestConfiguration.class},
        properties = {
            "logging.level.org.springframework.web=debug",
            "logging.level.root=info",
            "logging.level.com.github.manosbatsis.domainprimitives=debug"
        })
class ConversionServiceTest {

    @Autowired private FormattingConversionService conversionService;

    @Autowired private FromDomainPrimitiveConverter fromDomainPrimitiveConverter;

    @Autowired private ToDomainPrimitiveConverter toTypedPropertyConverter;

    @BeforeEach
    void setUp() {
        conversionService.addConverter(fromDomainPrimitiveConverter);
        conversionService.addConverter(toTypedPropertyConverter);
    }

    @Test
    void whenConvertringTo_thenNoExceptions() {
        String customerIdValue = UUID.randomUUID().toString();
        Customer.CustomerId expectedId = new Customer.CustomerId(customerIdValue);
        assertThat(conversionService.canConvert(String.class, Customer.CustomerId.class)).isTrue();
        Customer.CustomerId actual =
                conversionService.convert(customerIdValue, Customer.CustomerId.class);
        assertThat(actual).isEqualTo(expectedId);
    }

    @Test
    void whenConvertringFrom_thenNoExceptions() {
        String expectedCustomerIdValue = UUID.randomUUID().toString();
        Customer.CustomerId customerId = new Customer.CustomerId(expectedCustomerIdValue);
        assertThat(conversionService.canConvert(Customer.CustomerId.class, String.class)).isTrue();
        String actual = conversionService.convert(customerId, String.class);
        log.info("whenConvertringFrom_thenNoExceptions, actual: {}", actual);
        assertThat(actual).isEqualTo(expectedCustomerIdValue);
    }
}
