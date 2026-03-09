package com.proyecto.fenixtech.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"order", "product"})
@EqualsAndHashCode(exclude = {"order", "product"})



@Schema(description = "Modelo de OrderDetails", name = "OrderDetails")
@Entity
@Table(name = "order_details")
public class OrderDetails implements Serializable {
    @Schema(description = "Identificador único del detalle del pedido", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id", nullable = false, unique = true)
    private Integer orderDetailId;

    @Schema(description = "Cantidad de productos en el pedido", example = "1")
    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Schema(description = "Precio unitario del producto en el momento de la compra", example = "250.00")
    @NotNull(message = "El precio unitario es obligatorio")
    @Column(name = "unit_price_at_purchase", nullable = false)
    private Double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties({"orderDetails", "shipment", "buyer"})
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"subcategory", "company", "cartItems", "orderDetails"})
    private Products product;

    @JsonProperty("productId")
    public void setProductId(Integer productId) {
        this.product = new Products();
        this.product.setProductId(productId);
    }

}
