package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proyecto.fenixtech.model.enums.ProposalStatus;

import io.swagger.v3.oas.annotations.media.Schema;
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
@ToString(exclude = "requester")
@EqualsAndHashCode(exclude = "requester")

@Schema(description = "Modelo de Propuesta", name = "Proposals")
@Entity
@Table(name = "proposals")
public class Proposals implements Serializable {
    @Schema(description = "Identificador único de la propuesta", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id", nullable = false, unique = true)
    private Integer proposalId;

    @Schema(description = "Título de la propuesta", example = "Recogida de monitores antiguos")
    @NotBlank(message = "El título es obligatorio")
    @Column(name = "title", nullable = false)
    private String title;

    @Schema(description = "Descripción detallada de la propuesta", example = "Buscamos empresa para reciclar 50 monitores CRT")
    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Schema(description = "Estado de la propuesta", example = "open")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProposalStatus status = ProposalStatus.OPEN;

    @Schema(description = "Fecha de creación de la propuesta")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_user_id", nullable = false)
    @JsonIgnoreProperties({ "proposals", "company", "addresses", "reviews", "orders", "cartItems", "posts", "comments", "following" })
    private Users requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({ "subcategories", "products" })
    private Categories category;

}
