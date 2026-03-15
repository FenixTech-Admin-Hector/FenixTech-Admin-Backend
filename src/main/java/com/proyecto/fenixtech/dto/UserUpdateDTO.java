package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", message = "El email está en un formato incorrecto")
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|PNG|JPG|JPEG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpe")
    private String userImg;
}