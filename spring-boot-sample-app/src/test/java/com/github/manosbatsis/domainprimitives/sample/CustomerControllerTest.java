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
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateUpdateAndRetrieveCustomers() {
        UUID customerId = UUID.randomUUID();
        String sCustomerRef = "CUS-001";

        webTestClient
                .post()
                .uri("api/customers")
                .bodyValue(
                        """
                        {
                          "name": "Travis Cornell",
                          "ref": "%s",
                          "id": "%s"
                        }
                        """
                                .formatted(sCustomerRef, customerId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("ref")
                .isEqualTo(sCustomerRef);

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/customers/{ref}").build(sCustomerRef))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("ref")
                .isEqualTo(sCustomerRef);
    }
}
