package com.proyecto.fenixtech.admin.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminSubcategoriesRequestDTO {
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    private String name;

    @NotBlank(message = "La descripción de la subcategoría es obligatoria")
    private String description;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Integer categoryId;
}