package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompaniesRequestUpdateDTO {
   @NotBlank(message = "El nombre es obligatorio")
    private String companyName;

    @NotBlank(message = "El CIF es obligatorio")
    @Pattern(regexp = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$", message = "El CIF está en un formato incorrecto")
    private String cif;

    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|PNG|JPG|JPEG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpeg")
    private String companyImg; 
}
