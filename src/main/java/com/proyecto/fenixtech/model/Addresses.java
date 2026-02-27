package com.proyecto.fenixtech.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")

@Schema(description = "Model de direcciones", name = "Adresses")
@Entity
@Table(name = "adresses")
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
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Schema(description = "País de la dirección", example = "España")
    @NotBlank(message = "El país es obligatorio")
    @Column(name = "country", nullable = false)
    private String country;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true, nullable = false)
    @JsonIgnoreProperties({ "company", "address", "reviews", "proposals", "orders", "cartItems", "posts", "comments" })
    private Users user;

}
