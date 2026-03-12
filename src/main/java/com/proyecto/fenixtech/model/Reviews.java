package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = { "reviewer", "targetCompany" })
@EqualsAndHashCode(exclude = { "reviewer", "targetCompany" })


@Schema(description = "Modelo de Reviews", name = "Reviews")
@Entity
@Table(name = "reviews")
public class Reviews implements Serializable{
    @Schema(description = "Identificador único de la reseña", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, unique = true)
    private Integer reviewId;

    @Schema(description = "Puntuación de la reseña", example = "5")
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Schema(description = "Comentario de la reseña", example = "Excelente servicio y producto.")
    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String comment;

    @Schema(description = "Fecha de creación de la reseña")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_user_id", nullable = false)
    @JsonIgnoreProperties({"reviews", "company", "addresses", "proposals", "orders", "cartItems", "posts", "comments", "following"})
    private Users reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_company_id", nullable = false)
    @JsonIgnoreProperties({"reviews", "companyBadges", "products", "user", "followers"})
    private Companies targetCompany;



}
