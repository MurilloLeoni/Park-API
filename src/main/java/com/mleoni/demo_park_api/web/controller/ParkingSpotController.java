package com.mleoni.demo_park_api.web.controller;

import com.mleoni.demo_park_api.entities.ParkingSpot;
import com.mleoni.demo_park_api.services.ParkingSpotService;
import com.mleoni.demo_park_api.web.controller.dto.ClientResponseDTO;
import com.mleoni.demo_park_api.web.controller.dto.ParkingSpotCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.ParkingSpotResponseDTO;
import com.mleoni.demo_park_api.web.controller.dto.mapper.ParkingSpotMapper;
import com.mleoni.demo_park_api.web.controller.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/spots")
public class ParkingSpotController {

    private final ParkingSpotService spotService;

    @Operation(summary = "Create a new Parking Spot", description = "Resource for creating a new parking spot." +
            "The request requires a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully.",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL created")),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Parking spot already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ParkingSpotCreateDTO dto) {
        ParkingSpot spot = ParkingSpotMapper.toParkingSpot(dto);
        spotService.save(spot);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(spot.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find a spot", description = "Resource for find a spot by Code." +
            "The request requires a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingSpotResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Spot not found.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpotResponseDTO> getByCode(@PathVariable String code) {
        ParkingSpot spot = spotService.findByCode(code);
        return ResponseEntity.ok(ParkingSpotMapper.toDto(spot));
    }
}
