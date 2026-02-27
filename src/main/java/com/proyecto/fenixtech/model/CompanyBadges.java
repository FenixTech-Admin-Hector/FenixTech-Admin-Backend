package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = { "company", "badge" })
@EqualsAndHashCode(exclude = { "company", "badge" })


@Schema(description = "Modelo de Insignias de Empresas", name = "CompanyBadges")
@Entity
@Table(name = "CompanyBadges")
public class CompanyBadges implements Serializable {
    @Schema(description = "Identificador compuesto de la relación entre empresa e insignias")
    @EmbeddedId
    private CompanyBadgeId id;

    @Schema(description = "Fecha de asignación de la insignia a la empresa", example = "2026-01-01")
    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDate assignedAt;
    
    @ManyToOne
    @MapsId("companyId")
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("companyBadges")
    private Companies company;

    @ManyToOne
    @MapsId("badgeId")
    @JoinColumn(name = "badge_id")
    @JsonIgnoreProperties("companyBadge")
    private Badges badge;

}
