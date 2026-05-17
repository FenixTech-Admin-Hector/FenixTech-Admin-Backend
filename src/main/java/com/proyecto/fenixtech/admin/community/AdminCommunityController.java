package com.proyecto.fenixtech.admin.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.model.Comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Community", description = "API para la moderación de la comunidad (Posts y Comentarios)")
@RequestMapping("/admin/community")
@RestController
public class AdminCommunityController {

    @Autowired
    private AdminCommunityService adminCommunityService;

    @Operation(summary = "Ver todos los posts", description = "Lista todos los posts publicados para la tabla de moderación.")
    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> findAllPostsAdmin() {
        return ResponseEntity.ok(adminCommunityService.findAllPosts());
    }

    @Operation(summary = "Ver comentarios de un post", description = "Devuelve la lista de comentarios dentro de un post específico para revisarlos.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<Comments>> findCommentsByPostAdmin(
            @PathVariable Integer postId, 
            Pageable pageable) {
        return ResponseEntity.ok(adminCommunityService.findCommentsByPostId(postId, pageable));
    }

    @Operation(summary = "Eliminar post", description = "Borra un post entero, incluyendo sus imágenes y comentarios asociados.")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePostAdmin(@PathVariable Integer id) {
        adminCommunityService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar comentario", description = "Elimina un comentario individual ofensivo sin borrar el post.")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteCommentAdmin(@PathVariable Integer id) {
        adminCommunityService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}