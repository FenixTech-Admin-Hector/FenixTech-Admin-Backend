package com.proyecto.fenixtech.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.service.UsersService;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuers", description = "API para gestión de usuarios")
@RequestMapping("/api/users")
@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Users>> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findAllUsers());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Users> findByUsersId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByUsersId(id));
    }

    @Operation(summary = "Obtener usuario por email", description = "Devuelve un usuario por su email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/email")
    public ResponseEntity<Users> findByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByEmail(email));
    }

    @Operation(summary = "Obtener usuarios por rol", description = "Devuelve una lista de usuarios por su rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "El rol introducido no existe")
    })
    @GetMapping("/role")
    public ResponseEntity<List<Users>> findByRole(@RequestParam Rol rol) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByRole(rol));
    }

    @Operation(summary = "Obtener usuarios por fecha de creación descendente", description = "Devuelve una lista de usuarios por su fecha de creación ordenados de manera descendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/desc")
    public ResponseEntity<List<Users>> findByCreatedAtOrderByDesc() {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByCreatedAtOrderByDesc());
    }

    @Operation(summary = "Obtener usuarios por fecha de creación ascendente", description = "Devuelve una lista de usuarios por su fecha de creación ordenados de manera ascendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/asc")
    public ResponseEntity<List<Users>> findByCreatedAtOrderByAsc() {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByCreatedAtOrderByAsc());
    }

    @Operation(summary = "Obtener usuarios registrados entre dos fechas", description = "Devuelve una lista de usuarios registrados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito"),
            @ApiResponse(responseCode = "400", description = "Fechas introducidas en formato incorrecto")
    })
    @GetMapping("/created_at/between")
    public ResponseEntity<List<Users>> findByCreatedAtBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByCreatedAtBetween(startDate, endDate));
    }

    @Operation(summary = "Obtener usuarios por año de creación", description = "Devuelve una lista de usuarios por su año de creación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/year")
    public ResponseEntity<List<Users>> findByCreatedAt(@RequestParam Integer year) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findByCreatedAt(year));
    }

    @Operation(summary = "Obtener el numero de usuarios", description = "Devuelve el numero de usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Numero de usuarios obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = usersService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
