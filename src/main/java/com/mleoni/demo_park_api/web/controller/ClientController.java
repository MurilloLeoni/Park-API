package com.mleoni.demo_park_api.web.controller;

import com.mleoni.demo_park_api.entities.Client;
import com.mleoni.demo_park_api.jwt.JwtUserDetails;
import com.mleoni.demo_park_api.services.ClientService;
import com.mleoni.demo_park_api.services.UserService;
import com.mleoni.demo_park_api.web.controller.dto.ClientCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.ClientResponseDTO;
import com.mleoni.demo_park_api.web.controller.dto.mapper.ClientMapper;
import com.mleoni.demo_park_api.web.controller.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clients", description = "Contains all operations related to a user's resources.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    private final UserService userService;

    @Operation(summary = "Create a new client", description = "Resource for creating a new client linked to a registered user." +
            "The request requires a bearer token. Access restricted to Role='CUSTOMER'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Feature not allowed for ADMIN profile.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Client CPF already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientCreateDTO createDto,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(createDto);
        client.setUser(userService.searchById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(ClientMapper.toDto(client));
    }
}
