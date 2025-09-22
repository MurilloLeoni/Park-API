package com.mleoni.demo_park_api.web.controller.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ParkingSpotResponseDTO {

    private Long id;
    private String code;
    private String status;
}
