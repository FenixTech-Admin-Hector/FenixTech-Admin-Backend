package com.proyecto.fenixtech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para la creación y gestión de envíos de pedidos")
public class ShipmentRequestDTO {

    @Schema(description = "ID del pedido asociado", example = "101")
    @NotNull(message = "El ID del pedido es obligatorio")
    private Integer orderId;

    @Schema(description = "ID de la empresa de transporte seleccionada", example = "1")
    @NotNull(message = "Debes seleccionar un transportista (carrierId)")
    private Integer carrierId;

    @Schema(description = "Calle de destino", example = "Avenida de la Constitución 45")
    @NotBlank(message = "La calle de envío es obligatoria")
    private String shippingStreet;

    @Schema(description = "Ciudad de destino", example = "Sevilla")
    @NotBlank(message = "La ciudad es obligatoria")
    private String shippingCity;

    @Schema(description = "Código postal de destino", example = "41001")
    @NotBlank(message = "El código postal es obligatorio")
    private String shippingZipCode;

    @Schema(description = "País de destino", example = "España")
    @NotBlank(message = "El país es obligatorio")
    private String shippingCountry;
}