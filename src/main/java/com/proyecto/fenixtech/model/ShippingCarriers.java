package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "shipments")
@EqualsAndHashCode(exclude = "shipments")

@Schema(description = "Modelo de Empresas de Envios", name = "ShippingCarriers")
@Entity
@Table(name = "shipping_carriers")
public class ShippingCarriers implements Serializable {
    @Schema(description = "Identificador único de la empresa de transporte", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_id", nullable = false, unique = true)
    private Integer carrierId;

    @Schema(description = "Nombre de la empresa de transporte", example = "SEUR")
    @NotBlank(message = "El nombre de la empresa de transporte es obligatorio")
    @Column(name = "carrier_name", nullable = false)
    private String carrierName;

    @Schema(description = "URL del logo de la empresa", example = "https://example.com/logo.png")
    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|svg|PNG|JPG|JPEG|SVG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpeg")
    @Column(name = "carrier_logo", columnDefinition = "TEXT")
    private String carrierLogo;

    @Schema(description = "URL de seguimiento de la empresa", example = "https://seur.com/track?id=")
    @Column(name = "tracking_url", columnDefinition = "TEXT")
    private String trackingUrl;

    @Schema(description = "Precio base de la empresa de envíos", example = "10.00")
    @NotNull(message = "El precio base es obligatorio")
    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Schema(description = "Días estimados de envío", example = "3")
    @NotNull(message = "Los días estimados son obligatorios")
    @Min(value = 1, message = "Los días estimados deben ser al menos 1")
    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays;

    @Schema(description = "Visibilidad del transportista", example = "true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;


    @OneToMany(mappedBy = "carrier")
    @JsonIgnoreProperties({"carrier", "order"})
    private List<Shipments> shipments;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }


}
