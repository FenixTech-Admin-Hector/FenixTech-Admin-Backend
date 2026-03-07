package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.service.PostsImgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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

    @Operation(summary = "Contar total de imágenes", description = "Devuelve el número total de imágenes almacenadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de imágenes obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        Map<String, Long> response = new HashMap<>();
        response.put("cantidad", postsImgService.count());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear una nueva imagen", description = "Asocia una nueva URL de imagen a un post existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Imagen creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<PostsImg> save(@Valid @RequestBody PostsImg postsImg) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postsImgService.save(postsImg));
    }

    @Operation(summary = "Actualizar una imagen", description = "Actualiza la información de una imagen existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostsImg> update(@PathVariable Integer id, @Valid @RequestBody PostsImg postsImg) {
        return ResponseEntity.ok(postsImgService.update(id, postsImg));
    }

    @Operation(summary = "Eliminar una imagen", description = "Elimina una imagen de post por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Imagen eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        postsImgService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
