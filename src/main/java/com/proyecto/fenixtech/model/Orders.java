package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proyecto.fenixtech.model.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"buyer", "orderDetails", "shipment"})
@EqualsAndHashCode(exclude = {"buyer", "orderDetails", "shipment"})

@Schema(description = "Modelo de Ordenes", name = "Orders")
@Entity
@Table(name = "orders")
public class Orders implements Serializable {
    @Schema(description = "Identificador único del pedido", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, unique = true)
    private Integer orderId;

    @Schema(description = "Fecha del pedido", example = "2023-10-27T10:00:00")
    @CreationTimestamp
    @Column(name = "order_date", updatable = false)
    private LocalDateTime orderDate;

    @Schema(description = "Monto total del pedido", example = "500.00")
    @NotNull(message = "El monto total es obligatorio")
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Schema(description = "Estado del pedido", example = "paid")
    @NotNull(message = "El estado del pedido no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Schema(description = "Necesita envío el pedido", example = "true")
    @Column(name = "requires_shipping")
    private Boolean requiresShipping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_user_id", nullable = false)
    @JsonIgnoreProperties({"orders", "company", "addresses", "reviews", "proposals", "cartItems", "posts", "comments", "following"})
    private Users buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"order"}) //Se quita products para que desde el front se puedan ver los productos asociados 
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order")
    private Shipments shipment;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = OrderStatus.PENDING_PAYMENT;
        }
        if (this.totalAmount == null ) {
            this.totalAmount = 0.00;
        }
        if (this.requiresShipping == null) {
            this.requiresShipping = false;
        }
    }


}
