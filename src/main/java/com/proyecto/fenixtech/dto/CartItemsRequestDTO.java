package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemsRequestDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    private Integer quantity;

}
