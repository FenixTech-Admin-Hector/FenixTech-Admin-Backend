package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.dto.FollowDTO;
import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Follows", description = "API para la gestión de seguidores y comunidad")
@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Operation(summary = "Obtener lista de seguidores activos", description = "Devuelve los seguidores de un usuario cuya cuenta no esté borrada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de seguidores obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o inactivo")
    })
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<Follow>> getActiveFollowers(@PathVariable Integer userId) {
        return ResponseEntity.ok(followService.getActiveFollowers(userId));
    }

    @Operation(summary = "Obtener lista de seguidos activos", description = "Devuelve la lista de usuarios a los que sigue el usuario y que están activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de seguidos obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o inactivo")
    })
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Follow>> getActiveFollowing(@PathVariable Integer userId) {
        return ResponseEntity.ok(followService.getActiveFollowing(userId));
    }

    @Operation(summary = "Obtener el número de seguidores activos", description = "Devuelve el conteo de seguidores excluyendo cuentas borradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de seguidores obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(@PathVariable Integer userId) {
        Long count = followService.countActiveFollowers(userId);

        Map<String, Long> response = new HashMap<>();
        response.put("followersCount", count);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener el número de usuarios seguidos activos", description = "Devuelve el conteo de seguidos excluyendo cuentas borradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de seguidos obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(@PathVariable Integer userId) {
        Long count = followService.countActiveFollowing(userId);

        Map<String, Long> response = new HashMap<>();
        response.put("followingCount", count);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Seguir o dejar de seguir a un usuario", description = "Permite establecer o eliminar una relación de seguimiento entre dos usuarios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. seguirse a sí mismo o cuenta inactiva)")
    })
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFollow(
            @Valid @RequestBody FollowDTO dto) {

        Boolean isFollowing = followService.toggleUser(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        response.put("message", isFollowing ? "Ahora sigues a este usuario" : "Has dejado de seguir a este usuario");

        return ResponseEntity.ok(response);
    }
}