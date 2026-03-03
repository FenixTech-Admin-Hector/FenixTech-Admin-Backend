package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = { "subcategories"})
@EqualsAndHashCode(exclude = { "subcategories"})

@Schema(description = "Modelo de Categorías", name = "Categories")
@Entity
@Table(name = "categories")
public class Categories implements Serializable{
    @Schema(description = "Identificador único de la categoría", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, unique = true)
    private Integer categoryId;

    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Schema(description = "Descripción de la categoría", example = "Dispositivos electrónicos usados")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"category"})
    private List<Subcategories> subcategories = new ArrayList<>();




}
