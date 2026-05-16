package com.proyecto.fenixtech.admin.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminCategoriesRequestDTO {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    private String description;
}