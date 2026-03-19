package com.proyecto.fenixtech.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.proyecto.fenixtech.dto.PostsRequestDTO;
import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.service.PostsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Posts", description = "API para gestión de posts")
@RequestMapping("/posts")
@RestController
public class PostsController {
        @Autowired
        private PostsService postsService;

        @Operation(summary = "Obtener todos los posts", description = "Devuelve una lista paginada de posts")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Número de posts obtenido con éxito"),
                        @ApiResponse(responseCode = "404", description = "No se encontraron posts")
        })

        @GetMapping
        public ResponseEntity<Page<Posts>> findAllPosts(
                        @PageableDefault(page = 0, // Si no me dan página, dame la primera
                                        size = 10, // Si no me dan tamaño, dame 10
                                        sort = "createdAt", // Ordena por fecha
                                        direction = Sort.Direction.DESC // Los más nuevos primero
                        ) Pageable pageable) {

                return ResponseEntity.ok(postsService.findAllPosts(pageable));
        }

        @Operation(summary = "Obtener post por ID", description = "Devuelve un post por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Post obtenido con éxito"),
                        @ApiResponse(responseCode = "404", description = "Post no encontrado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Posts> findPostById(@PathVariable Integer id) {
                return ResponseEntity.ok(postsService.findById(id));
        }

        @Operation(summary = "Obtener posts por ID de usuario", description = "Devuelve una lista de posts asociada a un ID de usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Posts obtenidos con éxito"),
                        @ApiResponse(responseCode = "404", description = "No se encontraron posts para el usuario")
        })

        @GetMapping("/user/{userId}")
        public ResponseEntity<List<Posts>> findByUserId(@PathVariable Integer userId) {
                return ResponseEntity.ok(postsService.findByUserId(userId));
        }

        @Operation(summary = "Obtener posts públicos de una empresa", description = "Devuelve una lista de posts públicos de una empresa")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Posts obtenidos con éxito"),
                @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
        })
        @GetMapping("/company/{companyId}")
        public ResponseEntity<List<Posts>> getPublicPostsByCompany(@PathVariable Integer companyId) {
                List<Posts> posts = postsService.getPublicPostsByCompanyId(companyId);
                return ResponseEntity.ok(posts);
        }

        @Operation(summary = "Obtener el número total de posts", description = "Devuelve el número total de posts")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Número de posts obtenido con éxito")
        })

        @GetMapping("/count")
        public ResponseEntity<Map<String, Long>> count() {
                Long count = postsService.count();
                Map<String, Long> response = new HashMap<>();
                response.put("count", count);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Eliminar post por ID", description = "Elimina un post por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Post eliminado con éxito"),
                        @ApiResponse(responseCode = "404", description = "Post no encontrado")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
                postsService.deleteById(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Actualizar un post", description = "Actualiza un post existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Post actualizado con éxito"),
                        @ApiResponse(responseCode = "404", description = "Post no encontrado")
        })
        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Posts> updatePost(@PathVariable Integer id, @Valid @ModelAttribute PostsRequestDTO dto) {
                Posts updatedPost = postsService.update(id, dto);
                return ResponseEntity.ok(updatedPost);
        }

        @Operation(summary = "Crear un post", description = "Crea un nuevo post")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Post creado con éxito"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        @PostMapping
        public ResponseEntity<Posts> createPost(@Valid @RequestBody PostsRequestDTO dto) {
                Posts newPost = postsService.save(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        }

}
