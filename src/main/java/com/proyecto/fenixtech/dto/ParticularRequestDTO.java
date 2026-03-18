package com.proyecto.fenixtech.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ParticularRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", message = "El email está en un formato incorrecto")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.*]).{8,}$", 
        message = "La contraseña debe contener al menos un número, una letra minúscula, una letra mayúscula y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private MultipartFile userImg;
}
