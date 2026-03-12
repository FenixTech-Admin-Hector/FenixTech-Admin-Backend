package com.proyecto.fenixtech.dto;

import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar únicamente el estado de un envío")
public class ShipmentUpdateStatusDTO {

    @Schema(description = "Nuevo estado del envío", example = "IN_TRANSIT")
    @NotNull(message = "El estado del envío es obligatorio")
    private ShipmentStatus status;
}