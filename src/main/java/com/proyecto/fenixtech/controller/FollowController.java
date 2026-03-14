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

import com.proyecto.fenixtech.dto.FollowRequestDTO;
import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Follows", description = "API para la gestión de seguidores (Particulares siguen a Empresas)")
@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followsService;

    @Operation(summary = "Obtener lista de seguidores de una empresa", description = "Devuelve los usuarios particulares activos que siguen a una empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de seguidores obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada o inactiva")
    })
    @GetMapping("/company/{companyId}/followers")
    public ResponseEntity<List<Follow>> getCompanyFollowers(@PathVariable Integer companyId) {
        return ResponseEntity.ok(followsService.getFollowersByCompany(companyId));
    }

    @Operation(summary = "Obtener el número de seguidores de una empresa", description = "Devuelve el conteo de usuarios particulares que siguen a la empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de seguidores obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/company/{companyId}/followers/count")
    public ResponseEntity<Map<String, Long>> getCompanyFollowersCount(@PathVariable Integer companyId) {
        Long count = followsService.countFollowersByCompany(companyId);

        Map<String, Long> response = new HashMap<>();
        response.put("followersCount", count);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Obtener lista de empresas seguidas", description = "Devuelve las empresas a las que sigue un usuario particular activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empresas seguidas obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o inactivo")
    })
    @GetMapping("/user/{userId}/following")
    public ResponseEntity<List<Follow>> getUserFollowing(@PathVariable Integer userId) {
        return ResponseEntity.ok(followsService.getFollowingByUser(userId));
    }

    @Operation(summary = "Obtener el número de empresas seguidas", description = "Devuelve el conteo de empresas a las que sigue un usuario particular")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de empresas seguidas obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getUserFollowingCount(@PathVariable Integer userId) {
        Long count = followsService.countFollowingByUser(userId);

        Map<String, Long> response = new HashMap<>();
        response.put("followingCount", count);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Seguir o dejar de seguir a una empresa", description = "Permite a un particular establecer o eliminar una relación de seguimiento con una empresa activa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. rol incorrecto o empresa inactiva)")
    })
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFollow(@Valid @RequestBody FollowRequestDTO dto) {
        
        Boolean isFollowing = followsService.toggleFollow(dto); 
        
        Map<String, Object> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        response.put("message", isFollowing ? "Ahora sigues a esta empresa" : "Has dejado de seguir a esta empresa");

        return ResponseEntity.ok(response);
    }
}