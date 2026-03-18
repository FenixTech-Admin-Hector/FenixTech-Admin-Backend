package com.proyecto.fenixtech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Schema(description = "Entidad que representa una solicitud de contacto o petición de artículos (Contacto/Solicitud)")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "user", "category" })
@EqualsAndHashCode(exclude = { "user", "category" })
@Data
@Entity
@Table(name = "contact")
public class Contact {

    @Schema(description = "Identificador único de la solicitud de contacto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    @Schema(description = "Categoría relacionada con la solicitud")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({ "subcategories", "proposals", "contacts" })
    private Categories category;

    @Schema(description = "Usuario que ha creado y enviado la solicitud")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "posts", "company", "addresses", "reviews", "proposals", "orders", "cartItems", "comments",
            "following" })
    private Users user;

    @Schema(description = "Título principal o resumen de lo que se busca", example = "Busco monitores antiguos para donar a un colegio")
    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false, length = 250)
    private String title;

    @Schema(description = "Descripción detallada de la necesidad, cantidades, o condiciones de recogida", example = "Describe exactamente qué necesitas, cantidad aproximada, si puedes desplazarte a recogerlo, etc.")
    @NotBlank(message = "La descripción es obligatoria")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Schema(description = "Fecha y hora en la que se creó la solicitud", example = "2024-03-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}