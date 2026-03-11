package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubcategoriesRequestDTO {
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    private String name;

    @NotBlank(message = "La descripción de la subcategoría es obligatoria")
    private String description;

    @NotBlank(message = "El ID de la categoría es obligatorio")
    private Integer categoryId;
}
