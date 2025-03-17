/**
 * Copyright (C) 2024-2025 Manos Batsis
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
 * <a href="https://www.gnu.org/licenses/lgpl-3.0.html">https://www.gnu.org/licenses/lgpl-3.0.html</a>.
 */
package com.github.manosbatsis.domainprimitives.sample;

import com.github.manosbatsis.domainprimitives.sample.customer.Customer;
import com.github.manosbatsis.domainprimitives.sample.customer.CustomerRef;
import com.github.manosbatsis.domainprimitives.sample.customer.CustomerRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldCreateUpdateAndRetrieveOrders() {
        customerRepository.saveAndFlush(Customer.builder()
                .id(UUID.randomUUID())
                .name("Travis Cornell")
                .ref(new CustomerRef("CUS-001"))
                .build());
        UUID orderId = UUID.randomUUID();
        var comments = "Bla bla";

        webTestClient
                .post()
                .uri("api/orders")
                .bodyValue(
                        """
                        {
                          "customerRef": "CUS-001",
                          "comments": "%s",
                          "id": "%s"
                        }
                        """
                                .formatted(comments, orderId.toString()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("id")
                .isEqualTo(orderId.toString())
                .jsonPath("comments")
                .isEqualTo(comments);

        var updatedComments = "Bla bla updated";
        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/orders/{id}").build(orderId.toString()))
                .bodyValue(
                        """
                        {
                          "comments": "%s",
                          "id": "%s"
                        }
                        """
                                .formatted(updatedComments, orderId.toString()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("id")
                .isEqualTo(orderId.toString())
                .jsonPath("comments")
                .isEqualTo(updatedComments);

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/orders/{id}").build(orderId.toString()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("id")
                .isEqualTo(orderId)
                .jsonPath("comments")
                .isEqualTo(updatedComments);
    }
}
