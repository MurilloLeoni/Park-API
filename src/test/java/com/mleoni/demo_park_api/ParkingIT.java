package com.mleoni.demo_park_api;

import com.mleoni.demo_park_api.web.controller.dto.PageableDTO;
import com.mleoni.demo_park_api.web.controller.dto.ParkingCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkings/parkings-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkings/parkings-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarCheckin_ComDadosValidos_RetornarCreatedAndLocation() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .plate("WER-1111").brand("FIAT").model("PALIO 1.0")
                .color("AZUL").clientCpf("09191773016")
                .build();

        testClient.post().uri("/api/v1/parkings/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("plate").isEqualTo("WER-1111")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO 1.0")
                .jsonPath("color").isEqualTo("AZUL")
                .jsonPath("clientCpf").isEqualTo("09191773016")
                .jsonPath("receipt").exists()
                .jsonPath("entryDate").exists()
                .jsonPath("spotCode").exists();
    }

    @Test
    public void createCheckin_WithRoleClient_ReturnErrorStatus403() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .plate("WER-1111").brand("FIAT").model("PALIO 1.0")
                .color("AZUL").clientCpf("09191773016")
                .build();

        testClient.post().uri("/api/v1/parkings/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckin_WithInvalidData_ReturnErrorStatus422() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .plate("").brand("").model("")
                .color("").clientCpf("")
                .build();

        testClient.post().uri("/api/v1/parkings/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-in")
                .jsonPath("method").isEqualTo("POST");
    }


    @Test
    public void createCheckin_WithNonexistentCPF_ReturnErrorStatus404() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .plate("WER-1111").brand("FIAT").model("PALIO 1.0")
                .color("AZUL").clientCpf("18299320046")
                .build();

        testClient.post().uri("/api/v1/parkings/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/parkings/parking-insert-spot-occupied.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parkings/parking-delete-spot-occupied.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckin_WithOccupiedVacancies_ReturnErrorStatus404() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .plate("WER-1111").brand("FIAT").model("PALIO 1.0")
                .color("AZUL").clientCpf("09191773016")
                .build();

        testClient.post().uri("/api/v1/parkings/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void searchCheckin_ComPerfilAdmin_RetornarDadosStatus200() {

        testClient.get()
                .uri("/api/v1/parkings/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("entryDate").isEqualTo("2025-03-13 10:15:00")
                .jsonPath("spotCode").isEqualTo("A-01");
    }

    @Test
    public void searchCheckin_WithCustomerProfile_ReturnStatusData200() {

        testClient.get()
                .uri("/api/v1/parkings/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("entryDate").isEqualTo("2025-03-13 10:15:00")
                .jsonPath("spotCode").isEqualTo("A-01");
    }

    @Test
    public void searchCheckin_WithInexistentReceipt_ReturnErrorStatus404() {

        testClient.get()
                .uri("/api/v1/parkings/check-in/{receipt}", "20230313-999999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-in/20230313-999999")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void createCheckOut_WithExistingReceipt_ReturnSuccess() {

        testClient.put()
                .uri("/api/v1/parkings/check-out/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("entryDate").isEqualTo("2025-03-13 10:15:00")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("spotCode").isEqualTo("A-01")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("exitDate").exists()
                .jsonPath("value").exists()
                .jsonPath("discount").exists();
    }

    @Test
    public void createCheckOut_WithNonexistentReceipt_ReturnErrorStatus404() {

        testClient.put()
                .uri("/api/v1/parkings/check-out/{receipt}", "20230313-000000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-out/20230313-000000")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void createCheckOut_WithCustomerRole_ReturnErrorStatus403() {

        testClient.put()
                .uri("/api/v1/parkings/check-out/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/check-out/20230313-101300")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void searchParking_ByClientCPF_ReturnSuccess() {

        PageableDTO responseBody = testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}?size=1&page=1", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void searchParking_ByClientCPFWithClientProfile_ReturnErrorStatus403() {

        testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/cpf/98401203015")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void searchParking_OfLoggedInCustomer_ReturnSuccess() {

        PageableDTO responseBody = testClient.get()
                .uri("/api/v1/parkings?size=1&page=0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/parkings?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void searchParking_LoggedInClientProfileAdmin_ReturnErrorStatus403() {

        testClient.get()
                .uri("/api/v1/parkings")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings")
                .jsonPath("method").isEqualTo("GET");
    }
}
