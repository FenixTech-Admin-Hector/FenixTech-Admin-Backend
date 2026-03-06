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
@ToString(exclude = "companyBadges")
@EqualsAndHashCode(exclude = "companyBadges")

@Schema(description = "Modelo de Badges", name = "Badges")
@Entity
@Table(name = "badges")
public class Badges implements Serializable {
    @Schema(description = "Identificador único de la insignia", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false, unique = true)
    private Integer badgeId;

    @Schema(description = "Nombre de la insignia", example = "Eco-Friendly")
    @NotBlank(message = "El nombre de la insignia es obligatorio")
    @Column(name = "name", nullable = false, unique = true)
    private String badgeName;

    @Schema(description = "URL del icono de la insignia", example = "https://example.com/badge.png")
    @NotBlank(message = "La URL del icono es obligatoria")
    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    @Schema(description = "Estado de la insignia (activo o inactivo)", example = "true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "badge",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({ "badge", "company" })
    private List<CompanyBadges> companyBadges = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

}
