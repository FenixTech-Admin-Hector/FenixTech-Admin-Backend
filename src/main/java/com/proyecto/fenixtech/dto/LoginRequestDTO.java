package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", message = "El email está en un formato incorrecto")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}