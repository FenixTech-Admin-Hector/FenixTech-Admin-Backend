package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Modelo de Seguimiento (Particular sigue a Empresa)", name = "Follow")
@Entity
@Table(name = "follows")
public class Follow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del seguimiento", example = "1")
    private Integer followId;

    @Schema(description = "Fecha en la que se empezó a seguir a la empresa", example = "2023-10-27T10:00:00")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Usuario con rol PARTICULAR que realiza el seguimiento")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments", "following"})
    private Users follower;

    @Schema(description = "Empresa que está siendo seguida")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    @JsonIgnoreProperties({ "user", "products", "companyBadges", "reviews", "followers" })
    private Companies following; 
}