package com.proyecto.fenixtech.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")


@Schema(description = "Modelo de Envios", name = "Shipments")
@Entity
@Table(name = "shipments")
public class Shipments implements Serializable {
    @Schema(description = "Identificador único del envío", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false, unique = true)
    private Integer shipmentId;

    @Schema(description = "Dirección de envío", example = "Calle Falsa 123")
    @Column(name = "shipping_street", nullable = false)
    private String shippingStreet;

    @Schema(description = "Ciudad de envío", example = "Madrid")
    @Column(name = "shipping_city", nullable = false)
    private String shippingCity;

    @Schema(description = "Codigo postal del envío", example = "28001")
    @Column(name = "shipping_zip_code", nullable = false)
    private String shippingZipCode;

    @Schema(description = "País de envío", example = "España")
    @Column(name = "shipping_country", nullable = false)
    private String shippingCountry;

    @Schema(description = "Número de seguimiento del envío", example = "TRK123456789")
    @Column(name = "tracking_number")
    private String trackingNumber;

    @Schema(description = "Estado del envío", example = "shipped")
    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status")
    private ShipmentStatus status = ShipmentStatus.PREPARING;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    @JsonIgnoreProperties({"shipment", "orderDetails", "buyer"} )
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id", nullable = false)
    @JsonIgnoreProperties("shipments")
    private ShippingCarriers carrier;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = ShipmentStatus.PREPARING;
        }
    }

}
