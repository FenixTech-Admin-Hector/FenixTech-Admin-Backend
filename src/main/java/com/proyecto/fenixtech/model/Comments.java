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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = { "post", "author" })
@EqualsAndHashCode(exclude = { "post", "author" })

@Schema(description = "Modelo de Comentarios", name = "Comments")
@Entity
@Table(name = "comments")
public class Comments implements Serializable {
    @Schema(description = "Identificador único del comentario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, unique = true)
    private Integer commentId;

    @Schema(description = "Contenido del comentario", example = "¡Excelente post, muy informativo!")
    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Schema(description = "Fecha de creación del comentario")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties({"author", "comments"})
    private Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    @JsonIgnoreProperties({"comments", "posts", "company", "addresses", "reviews", "proposals", "orders", "cartItems", "following"})
    private Users author;



}
