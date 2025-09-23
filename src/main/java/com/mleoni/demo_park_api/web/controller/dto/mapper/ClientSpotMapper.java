package com.mleoni.demo_park_api.web.controller.dto.mapper;

import com.mleoni.demo_park_api.entities.ClientSpot;
import com.mleoni.demo_park_api.web.controller.dto.ParkingCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.ParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpotMapper {

    public static ClientSpot toClientSpot(ParkingCreateDTO dto) {
        return new ModelMapper().map(dto, ClientSpot.class);
    }

    public static ParkingResponseDTO toDto(ClientSpot clientSpot) {
        return new ModelMapper().map(clientSpot, ParkingResponseDTO.class);
    }
}
