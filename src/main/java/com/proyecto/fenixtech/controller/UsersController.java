package com.proyecto.fenixtech.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType; 
import org.springframework.web.bind.annotation.ModelAttribute;

import com.proyecto.fenixtech.service.UsersService;
import com.proyecto.fenixtech.dto.PasswordUpdateDTO;
import com.proyecto.fenixtech.dto.UserResponseDTO;
import com.proyecto.fenixtech.dto.UserUpdateDTO;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Users", description = "API para gestión de usuarios")
@RequestMapping("/users")
@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Operation(summary = "Filtrar usuarios", description = "Buscador avanzado que permite filtrar por rol, estado, y rango de fechas de creación con ordenamiento dinámico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios filtrada obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Parámetros de consulta inválidos")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Users>> findUsers(
            @RequestParam(required = false) Rol role,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(usersService.findUsers(role, active, start, end, direction));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca cualquier usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere rol ADMIN")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Users> findById(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        return ResponseEntity.ok(usersService.findById(id, onlyActive));
    }

    @Operation(summary = "Obtener usuario por Email", description = "Busca cualquier usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> findByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        return ResponseEntity.ok(usersService.findByEmail(email, onlyActive));
    }

    @Operation(summary = "Obtener mis datos de perfil", description = "Devuelve la información del usuario autenticado actual utilizando el Token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del perfil obtenidos con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token no válido")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        return ResponseEntity.ok(usersService.getMyProfile());
    }

    @Operation(summary = "Borrar un usuario", description = "Borra un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario borrado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid Integer id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar mi perfil", description = "Permite al usuario autenticado actualizar su nombre, apellido, email e imagen. Se valida que el email no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya en uso"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o ausente")
    })
    @PutMapping(value ="/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updateMyProfile(@Valid @ModelAttribute UserUpdateDTO dto) {
        return ResponseEntity.ok(usersService.updateMyProfile(dto));
    }

    @Operation(summary = "Cambiar mi contraseña", description = "Endpoint específico para cambio de clave. Requiere la contraseña actual para verificar la identidad del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada correctamente"),
            @ApiResponse(responseCode = "400", description = "La contraseña actual no coincide o la nueva no cumple los requisitos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        usersService.updatePassword(dto);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

}
