package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@ToString(exclude = {"author", "comments"})
@EqualsAndHashCode(exclude = {"author", "comments"})

@Schema(description = "Modelo de Posts", name = "Posts")
@Entity
@Table(name = "posts")
public class Posts implements Serializable {
    @Schema(description = "Identificador único del post", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, unique = true)
    private Integer postId;

    @Schema(description = "Título del post", example = "Cómo reciclar componentes electrónicos")
    @NotBlank(message = "El título es obligatorio")
    @Column(name = "title", nullable = false)
    private String title;

    @Schema(description = "Contenido del post", example = "En este post explicaremos...")
    @NotBlank(message = "El contenido es obligatorio")
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Schema(description = "Fecha de creación del post")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    @JsonIgnoreProperties({"posts", "company", "addresses", "reviews", "proposals", "orders", "cartItems", "comments", "following"})
    private Users author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "post", "author" })
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("post")
    private List<PostsImg> postImages = new ArrayList<>();

    


}
