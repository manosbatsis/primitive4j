package com.github.manosbatsis.typedidref.sample;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
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

  @Autowired private WebTestClient webTestClient;

  @Test
  void shouldEchoCustomers() {
    UUID customerId = UUID.randomUUID();
    String sCustomerRef = "CUS-001";

    webTestClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("api/customers/{id}/echo").build(customerId))
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
        .isOk()
        .expectBody(String.class)
        .consumeWith(
            response -> log.info("shouldEchoCustomers, response: {}", response.getResponseBody()));
    // .jsonPath("ref")
    // .isEqualTo(sCustomerRef);
  }

  @Test
  @Disabled
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
        .uri(uriBuilder -> uriBuilder.path("/api/customers/{id}").build(customerId))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("ref")
        .isEqualTo(sCustomerRef);
  }
}
