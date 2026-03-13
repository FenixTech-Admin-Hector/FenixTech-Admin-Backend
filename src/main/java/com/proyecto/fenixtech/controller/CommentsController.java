package com.proyecto.fenixtech.controller;

import com.proyecto.fenixtech.dto.CommentsRequest;
import com.proyecto.fenixtech.model.Comments;
import com.proyecto.fenixtech.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Comments", description = "API para gestión de comentarios")
@RequestMapping("/comments")
@RestController
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @Operation(summary = "Obtener comentarios de un post", description = "Devuelve una lista paginada de comentarios para un post específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentarios obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Page<Comments>> findCommentsByPostId(
            @PathVariable Integer postId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(commentsService.findCommentsByPostId(postId, pageable));
    }

    @Operation(summary = "Obtener comentarios por ID de usuario", description = "Devuelve una lista de comentarios asociada a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentarios obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron comentarios para el usuario")
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Comments>> findCommentsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(commentsService.findCommentsByUserId(userId));
    }

    @Operation(summary = "Obtener el número total de comentarios", description = "Devuelve el número total de comentarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de comentarios obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countAllComments() {
        Long count = commentsService.countAllComments();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear un nuevo comentario", description = "Crea un nuevo comentario y lo devuelve")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Comments> save(@Valid @RequestBody CommentsRequest comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentsService.save(comment));
    }

    @Operation(summary = "Eliminar un comentario", description = "El autor elimina su propio comentario por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comentario eliminado con éxito"),
            @ApiResponse(responseCode = "400", description = "No puedes borrar un comentario que no te pertenece."),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
    })
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id, @PathVariable Integer userId) {
        commentsService.deleteByUser(id, userId);
        return ResponseEntity.noContent().build();
    }

}
