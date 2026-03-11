package com.proyecto.fenixtech.dto;

import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Respuesta detallada del envío con URL de seguimiento calculada")
public class ShipmentResponseDTO {

    @Schema(description = "ID único del envío", example = "45")
    private Integer shipmentId;

    @Schema(description = "ID del pedido relacionado", example = "101")
    private Integer orderId;

    @Schema(description = "Nombre de la empresa de transporte", example = "SEUR")
    private String carrierName;

    @Schema(description = "URL del logo del transportista", example = "https://example.com/seur-logo.png")
    private String carrierLogo;

    @Schema(description = "Número de seguimiento generado", example = "SEU-A1B2C3D4")
    private String trackingNumber;

    @Schema(description = "URL final para el cliente (Calculada dinámicamente)", 
            example = "https://www.seur.com/seguimiento?id=SEU-A1B2C3D4")
    private String trackingUrl;

    @Schema(description = "Estado actual del paquete")
    private ShipmentStatus status;

    @Schema(description = "Dirección de entrega", example = "Calle Falsa 123")
    private String shippingStreet;

    @Schema(description = "Ciudad de entrega", example = "Madrid")
    private String shippingCity;

}
