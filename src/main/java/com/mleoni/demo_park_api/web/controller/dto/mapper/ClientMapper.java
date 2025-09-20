package com.mleoni.demo_park_api.web.controller.dto.mapper;

import com.mleoni.demo_park_api.entities.Client;
import com.mleoni.demo_park_api.web.controller.dto.ClientCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.ClientResponseDTO;
import lombok.*;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreateDTO createDto) {
        return new ModelMapper().map(createDto, Client.class);
    }

    public static ClientResponseDTO toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDTO.class);
    }

}
