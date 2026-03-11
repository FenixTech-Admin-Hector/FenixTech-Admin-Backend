package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProposalRequestPostDTO {
    @NotBlank(message = "El título es obligatorio")
    String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;

    @NotNull(message = "El id de la categoria es obligatorio")
    private Integer categoryId;

}
