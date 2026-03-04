package com.proyecto.fenixtech.controller;

import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.service.PostsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(summary = "Obtener posts recientes", description = "Devuelve una lista de los posts más recientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts recientes obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron posts recientes")
    })
    @GetMapping("/recent")
    public ResponseEntity<List<Posts>> findRecentPosts() {
        return ResponseEntity.ok(postsService.findRecentPosts());
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

}
