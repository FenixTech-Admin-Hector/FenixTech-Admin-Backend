package com.proyecto.fenixtech.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@ToString(exclude = { "product", "user" })
@EqualsAndHashCode(exclude = { "product", "user" })

@Schema(description = "Modelo de CartItems", name = "CartItems")
@Entity
@Table(name = "cart_items")
public class CartItems implements Serializable {
    @Schema(description = "Identificador único del item del carrito", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false, unique = true)
    private Integer cartItemId;

    @Schema(description = "Cantidad de productos", example = "2")
    @NotNull(message = "La cantidad de producoa no puede ser nula")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({ "subcategory", "company", "cartItems", "orderDetails" })
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts",
            "comments", "following" })
    private Users user;

    @PrePersist
    public void prePersist() {
        if (this.quantity == null) {
            this.quantity = 1;
        }
    }


}
