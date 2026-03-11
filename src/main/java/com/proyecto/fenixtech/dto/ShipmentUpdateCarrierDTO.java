package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentUpdateCarrierDTO {
    @NotNull(message = "El ID del transportista es obligatorio")
    private Integer carrierId;
}