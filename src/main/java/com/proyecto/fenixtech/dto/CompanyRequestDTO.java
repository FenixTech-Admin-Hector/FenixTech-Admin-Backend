package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompanyRequestDTO {

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

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String companyName;

    @NotBlank(message = "El CIF es obligatorio")
    @Pattern(regexp = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$", message = "El CIF está en un formato incorrecto")
    private String cif;

    @NotBlank(message = "La dirección es obligatoria")
    private String street;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El código postal es obligatorio")
    private String zipCode; 

    @NotBlank(message = "La provincia es obligatoria")
    private String region;
     
    @NotBlank(message = "El país es obligatorio")
    private String country;
}