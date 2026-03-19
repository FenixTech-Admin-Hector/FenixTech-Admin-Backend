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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@ToString(exclude = {"subcategory", "company", "cartItems", "orderDetails", "productsImg"})
@EqualsAndHashCode(exclude = {"subcategory", "company", "cartItems", "orderDetails", "productsImg"})


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
    @NotNull(message = "El precio no puede ser nulo")
    @Column(name = "price", nullable = false)
    private Double price;

    @Schema(description = "Stock disponible", example = "10")
    @NotNull(message = "El stock no puede ser nulo")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stock;

    @Schema(description = "Estatus del producto", example = "active")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus productStatus;

    @Schema(description = "Calle donde se encuentra el producto", example = "Calle Falsa 123")
    @NotBlank(message = "La calle es obligatoria")
    @Column(name = "street")
    private String street;

    @Schema(description = "Ciudad donde se encuentra el producto", example = "Madrid")
    @NotBlank(message = "La ciudad es obligatoria")
    @Column(name = "city")
    private String city;

    @Schema(description = "Región donde se encuentra el producto", example = "Madrid")
    @NotBlank(message = "La región es obligatoria")
    @Column(name = "region")
    private String region;

    @Schema(description = "Código postal donde se encuentra el producto", example = "28001")
    @NotBlank(message = "El código postal es obligatorio")
    @Size(min = 5, max = 5, message = "El código postal debe tener 5 dígitos")
    @Column(name = "zip_code")
    private String zipCode;

    @Schema(description = "País donde se encuentra el producto", example = "España")
    @NotBlank(message = "El país es obligatorio")
    @Column(name = "country")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    @JsonIgnoreProperties("products")
    private Subcategories subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnoreProperties({"products", "companyBadges", "reviews", "user", "followers"})
    private Companies company;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"product", "user"})
    private List<CartItems> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    @JsonIgnoreProperties({"product", "order"})
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<ProductsImg> productsImg = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (this.price == null) {
            this.price = 0.00;
        }
        if (this.stock == null) {
            this.stock = 1;
        }
        if (this.status == null) {
            this.status = ConditionStatus.NEW;
        }
        if (this.listingType == null) {
            this.listingType = ListingType.DONATION;
        }
        if (this.productStatus == null) {
            this.productStatus = ProductStatus.ACTIVE;
        }
    }

   
}
