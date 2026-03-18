package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")

@Schema(description = "Modelo de Subcategorías", name = "Subcategories")
@Entity
@Table(name = "subcategories")
public class Subcategories implements Serializable {
    @Schema(description = "Identificador único de la subcategoría", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id", nullable = false, unique = true)
    private Integer subcategoryId;

    @Schema(description = "Nombre de la subcategoría", example = "Laptops")
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    @Column(name = "name", nullable = false)
    private String name;

    @Schema(description = "Descripción de la subcategoría", example = "Ordenadores portátiles de diversas marcas")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Schema(description = "Visibilidad de la subcategoría", example = "true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"subcategories", "proposals", "contacts"})
    private Categories category;

    @OneToMany(mappedBy = "subcategory",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnoreProperties({"subcategory", "company", "cartItems", "orderDetails", "productsImg"}) 
    private List<Products> products = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }



}
