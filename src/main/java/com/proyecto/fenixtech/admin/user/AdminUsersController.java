package com.proyecto.fenixtech.admin.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType; 

import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Admin Users", description = "API exclusiva para la gestión de usuarios por parte del Administrador")
@RequestMapping("/api/admin/users") // Añadido /api por convención
@RestController
public class AdminUsersController {

    @Autowired
    private AdminUsersService adminUsersService;

    @Operation(summary = "Mostrar y Filtrar usuarios", description = "Lista todos los usuarios para la tabla de Figma, permitiendo filtrar por rol y estado.")
    @GetMapping("/search")
    public ResponseEntity<List<Users>> findUsers(
            @RequestParam(required = false) Rol role,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(adminUsersService.findUsers(role, active, start, end, direction));
    }

    @Operation(summary = "Banear usuario", description = "Realiza un borrado lógico (baneo) del usuario, ocultando también su empresa y productos asociados.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> banUser(@PathVariable @Valid Integer id) {
        adminUsersService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desbanear usuario", description = "Reactiva a un usuario previamente baneado.")
    @PutMapping("/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Integer id) {
        adminUsersService.unbanUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar usuario", description = "Permite al administrador modificar los datos básicos de cualquier usuario.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminUserResponseDTO> updateUserAsAdmin(
            @PathVariable Integer id, 
            @Valid @ModelAttribute AdminUserUpdateDTO dto) {
        return ResponseEntity.ok(adminUsersService.updateUserAsAdmin(id, dto));
    }
}