package com.proyecto.fenixtech.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")

@Schema(description = "Model de direcciones", name = "Addresses")
@Entity
@Table(name = "addresses")
public class Addresses implements Serializable {
    @Schema(description = "Identificador único de la dirección", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false, unique = true)
    private Integer addressId;

    @Schema(description = "Calle de la dirección", example = "Calle Falsa 123")
    @NotBlank(message = "La calle es obligatoria")
    @Column(name = "street", nullable = false)
    private String street;

    @Schema(description = "Ciudad de la dirección", example = "Madrid")
    @NotBlank(message = "La ciudad es obligatoria")
    @Column(name = "city", nullable = false)
    private String city;

    @Schema(description = "Región de la dirección", example = "Madrid")
    @NotBlank(message = "La región es obligatoria")
    @Column(name = "region", nullable = false)
    private String region;

    @Schema(description = "Código postal de la dirección", example = "28001")
    @NotBlank(message = "El código postal es obligatorio")
    @Size(min = 5, max = 5, message = "El código postal debe tener 5 dígitos")
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Schema(description = "País de la dirección", example = "España")
    @NotBlank(message = "El país es obligatorio")
    @Column(name = "country", nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments", "following"})
    private Users user;



}
