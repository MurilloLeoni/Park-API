package com.mleoni.demo_park_api.web.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDTO {

    @NotNull
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    @Size(min = 11, max = 11)
    @CPF
    private String cpf;

}
