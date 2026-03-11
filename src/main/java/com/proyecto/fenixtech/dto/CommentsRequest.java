package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentsRequest {
    @NotBlank(message = "El contenido del comentario es obligatorio")
    private String body;

    @NotNull(message = "El ID del post es obligatorio")
    private Integer postId;

    @NotNull(message = "El ID del autor es obligatorio")
    private Integer userId;

}
