package com.proyecto.fenixtech.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyecto.fenixtech.model.enums.Rol;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = { "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments",
        "following" })
@EqualsAndHashCode(exclude = { "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts",
        "comments", "following" })

@Schema(description = "Modelo de Usuario", name = "Users")
@Entity
@Table(name = "users")
public class Users implements UserDetails {
    @Schema(description = "Identificador unico del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Schema(description = "Email del usuario", example = "user@example.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", message = "El email está en un formato incorrecto")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "1234Abcd$")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.*]).{8,}$", message = "La contraseña debe contener al menos un número, una letra minúscula, una letra mayúscula y un carácter especial")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Schema(description = "Nombre del usuario", example = "Pepe")
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apelido es obligatorio")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Schema(description = "Rol del usuario", example = "admin")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Rol role;

    @Schema(description = "Imagen de perfil del usuario", example = "https://example.com/image.jpg")
    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|PNG|JPG|JPEG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpeg")
    @Column(name = "user_img", columnDefinition = "TEXT")
    private String userImg;

    @Schema(description = "Fecha de creación del usuario", example = "")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Estado del usuario", example = "true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Schema(description = "Fecha de borrado del usuario", example = "2023-10-27T10:00:00")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Schema(description = "Descripcion del usuario", example = "Soy un usuario ...........")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "user", "companyBadges", "products", "reviews", "followers" })
    private Companies company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "user" })
    private List<Addresses> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "user", "product" })
    private List<CartItems> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    @JsonIgnoreProperties({ "user", "orderDetails", "shipment" })
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "reviewer", "company" })
    private List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "author", "comments" })
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "author", "post" })
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("requester")
    private List<Proposals> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("follower")
    private List<Follow> following;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }

    }

    // =========================================================================
    // IMPLEMENTACIÓN DE USERDETAILS (SPRING SECURITY)
    // =========================================================================

    /**
     * Convierte tu Enum 'Rol' en el formato que entiende Spring Security.
     * Importante: Spring Security suele requerir el prefijo "ROLE_" (ej.
     * ROLE_ADMIN).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    /**
     * Le decimos a Spring que el nombre de usuario (username) para el login es tu
     * columna 'email'.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Le decimos a Spring dónde está la contraseña hasheada.
     */
    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    /**
     * Indica si la cuenta ha caducado. Siempre true (no implementamos cuentas
     * caducadas).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está bloqueada temporalmente.
     * Podemos enlazarlo con tu columna 'isActive' para que si un admin lo
     * desactiva, no pueda entrar.
     */
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Indica si las credenciales (contraseña) han caducado. Siempre true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado (activo).
     * También lo enlazamos con tu columna 'isActive' y que no esté borrado
     * lógicamente.
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isActive) && this.deletedAt == null;
    }

}
