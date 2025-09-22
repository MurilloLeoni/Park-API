package com.mleoni.demo_park_api.web.controller.dto.mapper;

import com.mleoni.demo_park_api.entities.ParkingSpot;
import com.mleoni.demo_park_api.web.controller.dto.ParkingSpotCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.ParkingSpotResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingSpotMapper {

    public static ParkingSpot toParkingSpot(ParkingSpotCreateDTO dto) {
        return new ModelMapper().map(dto, ParkingSpot.class);
    }

    public static ParkingSpotResponseDTO toDto(ParkingSpot spot) {
        return new ModelMapper().map(spot, ParkingSpotResponseDTO.class);
    }
}
