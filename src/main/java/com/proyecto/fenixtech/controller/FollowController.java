package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    

    @Operation(summary = "Obtener el número de seguidores de un usuario", description = "Devuelve el número de seguidores de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de seguidores obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(@PathVariable Integer userId) {
        Long count = followService.countFollowers(userId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("followersCount", count);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener el número de usuarios que sigues de un usuario", description = "Devuelve el número de usuarios que sigues de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de seguidores obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(@PathVariable Integer userId) {
        Long count = followService.countFollowing(userId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("followingCount", count);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Seguir o dejar de seguir a un usuario", description = "Permite seguir o dejar de seguir a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<Map<String, Object>> toggleFollow(
            @PathVariable Integer followerId,
            @PathVariable Integer followingId) {
        
        Boolean isFollowing = followService.toggleUser(followerId, followingId);

        Map<String, Object> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        response.put("message", isFollowing ? "Ahora sigues a este usuario" : "Has dejado de seguir a este usuario");

        return ResponseEntity.ok(response);
    }
}