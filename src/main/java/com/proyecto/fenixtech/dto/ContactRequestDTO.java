package com.proyecto.fenixtech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Objeto de transferencia de datos para crear una nueva solicitud de contacto")
@Data
public class ContactRequestDTO {

    @Schema(description = "ID de la categoría seleccionada", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Debes seleccionar una categoría")
    private Integer categoryId;

    @Schema(description = "Título o resumen de la solicitud", example = "Busco monitores antiguos para donar a un colegio", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El título de la solicitud es obligatorio")
    @Size(max = 250, message = "El título no puede superar los 250 caracteres")
    private String title;

    @Schema(description = "Descripción detallada de la necesidad", example = "Necesito al menos 5 monitores que funcionen para el aula de informática del colegio San José. Puedo ir a recogerlos en coche.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción detallada es obligatoria")
    private String description;

}