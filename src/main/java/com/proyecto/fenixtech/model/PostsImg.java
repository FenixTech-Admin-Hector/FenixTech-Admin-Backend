package com.proyecto.fenixtech.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "post")
@EqualsAndHashCode(exclude = "post")

@Schema(description = "Modelo de Imagenes de Posts", name = "PostsImg")
@Entity
@Table(name = "posts_img")
public class PostsImg implements Serializable {
    @Schema(description = "Identificador único de la imagen", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false, unique = true)
    private Integer imageId;

    @Schema(description = "URL de la imagen", example = "https://example.com/image.jpg")
    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Pattern(regexp = "^.+\\.(png|jpg|jpeg|PNG|JPG|JPEG)$", message = "La imagen debe ser un archivo .png, .jpg o .jpeg")
    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties({"author", "comments"})
    private Posts post;

    @JsonProperty("postId")
    public void setPostId(Integer postId) {
        this.post = new Posts();
        this.post.setPostId(postId);
    }

    //)

}
