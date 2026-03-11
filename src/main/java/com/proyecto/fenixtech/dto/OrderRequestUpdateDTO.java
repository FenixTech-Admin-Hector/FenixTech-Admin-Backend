package com.proyecto.fenixtech.dto;

import com.proyecto.fenixtech.model.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestUpdateDTO {
    @NotNull(message = "El nuevo estado es obligatorio")
    private OrderStatus status;
}
