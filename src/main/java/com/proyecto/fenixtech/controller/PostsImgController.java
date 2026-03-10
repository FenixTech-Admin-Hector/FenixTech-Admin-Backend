package com.proyecto.fenixtech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.service.PostsImgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "PostsImg", description = "API para gestión de imágenes de posts")
@RequestMapping("/postsImg")
@RestController
public class PostsImgController {
    @Autowired
    private PostsImgService postsImgService;

    @Operation(summary = "Obtener todas las imágenes de posts", description = "Devuelve una lista de todas las imágenes de posts registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imágenes de posts obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<PostsImg>> findAll() {
        return ResponseEntity.ok(postsImgService.findAllPostsImg());
    }

    @Operation(summary = "Obtener imagen por ID", description = "Devuelve una imagen de post por su ID único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen de post encontrada con éxito"),
            @ApiResponse(responseCode = "404", description = "Imagen de post no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostsImg> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(postsImgService.findById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imágenes de post encontradas con éxito"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @Operation(summary = "Obtener imágenes por ID de post", description = "Devuelve una lista de imágenes asociadas a un post específico")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostsImg>> findByPostId(@PathVariable Integer postId) {
        return ResponseEntity.ok(postsImgService.findByPostId(postId));
    }

    @Operation(summary = "Borrar imagen de un post específico", description = "Elimina una imagen validando que pertenece al post indicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagen eliminada con éxito"),
            @ApiResponse(responseCode = "400", description = "La imagen no pertenece a ese post"),
            @ApiResponse(responseCode = "404", description = "Post o Imagen no encontrados")
    })
    @DeleteMapping("/post/{postId}/image/{imageId}")
    public ResponseEntity<Void> deleteFromPost(
            @PathVariable Integer postId,
            @PathVariable Integer imageId) {

        postsImgService.deleteFromPost(postId, imageId);
        return ResponseEntity.noContent().build();
    }

}
