package com.mleoni.demo_park_api.web.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientResponseDTO {

    private Long id;
    private String name;
    private String cpf;
}
