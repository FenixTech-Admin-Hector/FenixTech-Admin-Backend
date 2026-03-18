package com.proyecto.fenixtech.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar empresas de transporte")
public class ShippingCarrierRequestDTO {

    @Schema(description = "Nombre comercial del transportista", example = "DHL Express")
    @NotBlank(message = "El nombre de la empresa de transporte es obligatorio")
    private String carrierName;

    @Schema(description = "URL del logo del transportista", example = "https://tu-storage.com/logos/dhl.png")
    @NotBlank(message = "La URL del logo es obligatoria")
    private MultipartFile carrierLogo;

    @Schema(description = "URL base de seguimiento con el marcador '{}'", example = "https://www.dhl.com/track?id={}")
    @NotBlank(message = "La URL de seguimiento es obligatoria")
    private String trackingUrl;

    @Schema(description = "Coste base del servicio de envío", example = "4.95")
    @NotNull(message = "El precio base es obligatorio")
    @Min(value = 0, message = "El precio base no puede ser negativo")
    private Double basePrice;

    @Schema(description = "Días estimados para la entrega", example = "3")
    @NotNull(message = "Los días estimados son obligatorios")
    @Min(value = 1, message = "El tiempo mínimo de entrega debe ser 1 día")
    private Integer estimatedDays;
}