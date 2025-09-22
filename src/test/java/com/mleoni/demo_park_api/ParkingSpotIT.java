package com.mleoni.demo_park_api;

import com.mleoni.demo_park_api.web.controller.dto.ParkingSpotCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/spots/spots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/spots/spots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingSpotIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createSpot_WithValidData_ReturningLocationWithStatus201() {
        testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new ParkingSpotCreateDTO("A-05", "AVAILABLE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void createSpot_WithCodeAlreadyExisting_ReturningErrorMessageWithStatus409() {
        testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new ParkingSpotCreateDTO("A-01", "AVAILABLE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");
    }

    @Test
    public void createSpot_WithInvalidData_ReturningErrorMessageWithStatus422() {
        testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new ParkingSpotCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");

        testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new ParkingSpotCreateDTO("A-501", "UNAVAILABLE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");
    }

    @Test
    public void findSpot_WithValidCode_ReturningSpotWithStatus200() {
        testClient
                .get()
                .uri("/api/v1/spots/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("AVAILABLE");
    }

    @Test
    public void createSpot_WithInvalidCode_ReturningErrorMessageStatus404() {
        testClient
                .get()
                .uri("/api/v1/spots/{code}", "A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spots/A-10");
    }

    @Test
    public void createSpot_WithClientRole_ReturningErrorMessageWithStatus403() {
        testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456")) // CLIENT
                .bodyValue(new ParkingSpotCreateDTO("A-09", "AVAILABLE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");
    }

    @Test
    public void findSpot_WithClientRole_ReturningErrorMessageWithStatus403() {
        testClient
                .get()
                .uri("/api/v1/spots/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456")) // CLIENT
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spots/A-01");
    }
}
