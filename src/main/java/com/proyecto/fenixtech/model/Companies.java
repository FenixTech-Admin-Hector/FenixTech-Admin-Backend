package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.proyecto.fenixtech.model.json.ImpactMetrics;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = { "user", "products", "companyBadges", "reviews", "followers" })
@EqualsAndHashCode(exclude = { "user", "products", "companyBadges", "reviews", "followers" })

@Schema(description = "Modelo de Empresa", name = "Companies")
@Entity
@Table(name = "companies")
public class Companies implements Serializable {
    @Schema(description = "Identificador único de la empresa", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false, unique = true)
    private Integer companyId;

    @Schema(description = "Nombre de la companía", example = "FenixTech")
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Schema(description = "CIF de la empresa", example = "B81948077")
    @NotBlank(message = "El CIF es obligatorio")
    @Pattern(regexp = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$", message = "El CIF está en un formato incorrecto")
    @Column(name = "cif", nullable = false, unique = true)
    private String cif;

    @Schema(description = "Puntuación de reputacion de la empresa", example = "4")
    @Column(name = "reputation_score")
    private Integer reputationScore = 0;

    @Schema(description = "Métricas de impacto de la empresa", example = "")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "impact_metrics", columnDefinition = "json")
    private ImpactMetrics impactMetrics;

    @Schema(description = "URL de la imagen de la empresa", example = "https://example.com/image.jpg")
    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|PNG|JPG|JPEG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpeg")
    @Column(name = "company_img", columnDefinition = "TEXT")
    private String companyImg;

    @Schema(description = "Descripción de la empresa")
    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Schema(description = "Visibilidad de la empresa", example = "true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments", "following" })
    private Users user;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "company", "category", "cartItems", "orderDetails"})
    private List<Products> products = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "company", "badge" })
    private List<CompanyBadges> companyBadges = new ArrayList<>();

    @OneToMany(mappedBy = "targetCompany", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({ "targetCompany", "user" })
    private List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("following")
    private List<Follow> followers;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    
}
