package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"category", "company", "cartItems", "orderDetails"})
@EqualsAndHashCode(exclude = {"category", "company", "cartItems", "orderDetails"})


@Schema(description = "Modelo de Productos", name = "Products")
@Entity
@Table(name = "products")
public class Products implements Serializable {
    @Schema(description = "Identificador único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private Integer productId;

    @Schema(description = "Nombre del producto", example = "Laptop Dell Latitude")
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(name = "title", nullable = false)
    private String productTitle;

    @Schema(description = "Descripción del producto", example = "Laptop reacondicionada con 16GB RAM")
    @Size(max=200, message= "La descripcion no puede superar los 200 caracteres")
    @Column(name = "description")
    private String description;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/image.jpg")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Schema(description = "Estado del producto", example = "new")
    @NotNull(message = "El estado del producto es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status", nullable = false)
    private ConditionStatus status;

    @Schema(description = "Tipo de venta de venta del producto", example = "donation")
    @NotNull(message = "El tipo de venta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "listing_type", nullable = false)
    private ListingType listingType;

    @Schema(description = "Precio del producto", example = "250.00")
    @Column(name = "price", nullable = false)
    private Double price;

    @Schema(description = "Stock disponible", example = "10")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stock;

    @Schema(description = "Estatus del producto", example = "active")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus productStatus = ProductStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("products")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnoreProperties({"products", "companyBadges", "reviews", "user"})
    private Companies company;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"product", "user"})
    private List<CartItems> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    @JsonIgnoreProperties({"product", "order"})
    private List<OrderDetails> orderDetails = new ArrayList<>();

    




}
