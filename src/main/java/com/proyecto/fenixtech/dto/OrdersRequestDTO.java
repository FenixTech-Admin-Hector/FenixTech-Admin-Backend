package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersRequestDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;
    
    @NotNull(message = "La dirección de envío es obligatoria")
    private Integer addressId;

    @NotNull(message = "Debes especificar si requiere envío")
    private Boolean requiresShipping;

    private Integer carrierId;
    private String shippingStreet;
    private String shippingCity;
    private String shippingZipCode;
    private String shippingCountry;
}
