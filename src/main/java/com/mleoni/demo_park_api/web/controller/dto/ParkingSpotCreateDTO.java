package com.mleoni.demo_park_api.web.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ParkingSpotCreateDTO {

    @NotBlank
    @Size(min = 4, max = 4)
    private String code;

    @NotBlank
    @Pattern(regexp = "AVAILABLE|OCCUPIED")
    private String status;
}
