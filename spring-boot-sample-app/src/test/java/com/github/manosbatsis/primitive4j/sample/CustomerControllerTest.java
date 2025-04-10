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
package com.github.manosbatsis.primitive4j.sample;

import com.github.manosbatsis.primitive4j.sample.customer.CustomerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateUpdateAndRetrieveCustomers() {
        String customerRef = "CUS-001";
        String customerName = "Travis Cornell";
        webTestClient
                .post()
                .uri(CustomerController.BASE_PATH)
                .bodyValue(
                        """
                        {
                          "name": "%s",
                          "ref": "%s"
                        }
                        """
                                .formatted(customerName, customerRef))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("name")
                .isEqualTo(customerName)
                .jsonPath("ref")
                .isEqualTo(customerRef);

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(CustomerController.BASE_PATH + "/{ref}").build(customerRef))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("name")
                .isEqualTo(customerName)
                .jsonPath("ref")
                .isEqualTo(customerRef);
    }
}
