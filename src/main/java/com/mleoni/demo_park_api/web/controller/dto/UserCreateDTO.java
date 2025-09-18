package com.mleoni.demo_park_api.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateDTO {

    @NotBlank
    @Email(message = "formato do e-mail está invalido", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
