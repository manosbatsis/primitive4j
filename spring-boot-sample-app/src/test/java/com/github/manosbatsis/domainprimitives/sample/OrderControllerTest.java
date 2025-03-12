package com.github.manosbatsis.domainprimitives.sample;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class OrderControllerTest {

    @Autowired private WebTestClient webTestClient;

    @Test
    void shouldCreateUpdateAndRetrieveOrders() {
        UUID orderId = UUID.randomUUID();
        var comments = "Bla bla";

        webTestClient
                .post()
                .uri("api/orders")
                .bodyValue(
                        """
                        {
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
