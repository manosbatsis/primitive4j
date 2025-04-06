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

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.primitive4j.sample.customer.Customer;
import com.github.manosbatsis.primitive4j.sample.customer.CustomerRef;
import com.github.manosbatsis.primitive4j.sample.customer.CustomerRepository;
import com.github.manosbatsis.primitive4j.sample.order.OrderController;
import com.github.manosbatsis.primitive4j.sample.order.PurchaseOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PurchaseOrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldCreateUpdateAndRetrieveOrders() {
        var customerRef = "CUS-003";
        customerRepository.saveAndFlush(Customer.builder()
                .name("Travis Cornell")
                .ref(new CustomerRef(customerRef))
                .build());
        var comments = "Bla bla";

        var savedPo = webTestClient
                .post()
                .uri(OrderController.BASE_PATH)
                .bodyValue(
                        """
                        {
                          "customerRef": "%s",
                          "comments": "%s"
                        }
                        """
                                .formatted(customerRef, comments))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(PurchaseOrder.class)
                .returnResult()
                .getResponseBody();

        assertThat(savedPo).isNotNull();

        var updatedComments = "Bla bla updated";
        webTestClient
                .put()
                .uri(uriBuilder ->
                        uriBuilder.path(OrderController.BASE_PATH + "/{id}").build(savedPo.getId()))
                .bodyValue(
                        """
                        {
                          "customerRef": "%s",
                          "comments": "%s",
                          "id": "%s"
                        }
                        """
                                .formatted(customerRef, updatedComments, savedPo.getId()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("id")
                .isEqualTo(savedPo.getId())
                .jsonPath("comments")
                .isEqualTo(updatedComments);

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(OrderController.BASE_PATH + "/{id}").build(savedPo.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("id")
                .isEqualTo(savedPo.getId())
                .jsonPath("comments")
                .isEqualTo(updatedComments);
    }
}
