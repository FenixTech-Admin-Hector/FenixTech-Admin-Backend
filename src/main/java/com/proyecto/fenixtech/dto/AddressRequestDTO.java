package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequestDTO {
    @NotNull
    private Integer userId;

    @NotBlank(message = "La calle es obligatoria")
    private String street;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "La region es obligatoria")
    private String region;

    @NotBlank(message = "El código postal es obligatorio")
    @Size(min = 5, max = 5, message = "El código postal debe tener 5 dígitos")
    private String zipCode;

    @NotBlank(message = "El país es obligatorio")
    private String country;

}