package com.proyecto.fenixtech.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.proyecto.fenixtech.service.UsersService;
import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
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

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Users>> findAllUsers() {
        return ResponseEntity.ok(usersService.findAllUsers());
    }

    @Operation(summary = "Obtener solo los usuarios activos", description = "Devuelve una lista de todos los usuarios que no han sido borrados (is_active = true)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios activos obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Users>> findByIsActiveTrue() {
        return ResponseEntity.ok(usersService.findByIsActiveTrue());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/all/{id}")
    public ResponseEntity<Users> findByUsersId(@PathVariable Integer id) {
        return ResponseEntity.ok(usersService.findByUsersId(id));
    }

    @Operation(summary = "Obtener un usuario activo por ID", description = "Devuelve un usuario por su ID solo si está activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario activo obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o está inactivo")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Users> findByUserIdAndIsActiveTrue(@PathVariable Integer id) {
        return ResponseEntity.ok(usersService.findByUserIdAndIsActiveTrue(id));
    }

    @Operation(summary = "Obtener usuario por email", description = "Devuelve un usuario por su email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/all/email")
    public ResponseEntity<Users> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(usersService.findByEmail(email));
    }

    @Operation(summary = "Obtener un usuario activo por email", description = "Devuelve un usuario por su email solo si está activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario activo obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o está inactivo")
    })
    @GetMapping("/email")
    public ResponseEntity<Users> findByEmailAndIsActiveTrue(@RequestParam String email) {
        return ResponseEntity.ok(usersService.findByEmailAndIsActiveTrue(email));
    }

    @Operation(summary = "Obtener usuarios por rol", description = "Devuelve una lista de usuarios por su rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "El rol introducido no existe")
    })
    @GetMapping("/all/role")
    public ResponseEntity<List<Users>> findByRole(@RequestParam Rol rol) {
        return ResponseEntity.ok(usersService.findByRole(rol));
    }

    @Operation(summary = "Obtener usuarios activos por rol", description = "Devuelve una lista de usuarios activos por su rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios activos obtenidos con éxito")
    })
    @GetMapping("/role")
    public ResponseEntity<List<Users>> findByRoleAndIsActiveTrue(@RequestParam Rol rol) {
        return ResponseEntity.ok(usersService.findByRoleAndIsActiveTrue(rol));
    }

    @Operation(summary = "Obtener usuarios por fecha de creación descendente", description = "Devuelve una lista de usuarios por su fecha de creación ordenados de manera descendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/all/created_at/desc")
    public ResponseEntity<List<Users>> findByCreatedAtOrderByDesc() {
        return ResponseEntity.ok(usersService.findByCreatedAtOrderByDesc());
    }

    @Operation(summary = "Obtener usuarios activos por fecha de creción descendente", description = "Devuelve una lista de usuarios activos ordenados por fecha de creación descendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/desc")
    public ResponseEntity<List<Users>> findByCreatedAtAndIsActiveTrueOrderByDesc() {
        return ResponseEntity.ok(usersService.findByCreatedAtAndIsActiveTrueOrderByDesc());
    }

    @Operation(summary = "Obtener usuarios por fecha de creación ascendente", description = "Devuelve una lista de usuarios por su fecha de creación ordenados de manera ascendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/all/created_at/asc")
    public ResponseEntity<List<Users>> findByCreatedAtOrderByAsc() {
        return ResponseEntity.ok(usersService.findByCreatedAtOrderByAsc());
    }

    @Operation(summary = "Obtener usuarios activos por fecha ascendente", description = "Devuelve una lista de usuarios activos ordenados por fecha de creación ascendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/asc")
    public ResponseEntity<List<Users>> findByCreatedAtAndIsActiveTrueOrderByAsc() {
        return ResponseEntity.ok(usersService.findByCreatedAtAndIsActiveTrueOrderByAsc());
    }

    @Operation(summary = "Obtener usuarios registrados entre dos fechas", description = "Devuelve una lista de usuarios registrados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito"),
            @ApiResponse(responseCode = "400", description = "Fechas introducidas en formato incorrecto")
    })
    @GetMapping("/all/created_at/between")
    public ResponseEntity<List<Users>> findByCreatedAtBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(usersService.findByCreatedAtBetween(startDate, endDate));
    }

    @Operation(summary = "Obtener usuarios activos entre dos fechas", description = "Devuelve una lista de usuarios activos registrados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito"),
            @ApiResponse(responseCode = "400", description = "Fechas introducidas en formato incorrecto")
    })
    @GetMapping("/created_at/between")
    public ResponseEntity<List<Users>> findByCreatedAtBetweenAndIsActiveTrue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(usersService.findByCreatedAtBetweenAndIsActiveTrue(startDate, endDate));
    }

    @Operation(summary = "Obtener usuarios por año de creación", description = "Devuelve una lista de usuarios por su año de creación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/all/created_at/year")
    public ResponseEntity<List<Users>> findByCreatedAt(@RequestParam Integer year) {
        return ResponseEntity.ok(usersService.findByCreatedAt(year));
    }

    @Operation(summary = "Obtener usuarios activos por año de crecaión", description = "Devuelve una lista de usuarios activos registrados por su año de creación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    @GetMapping("/created_at/year")
    public ResponseEntity<List<Users>> findByCreatedAtAndIsActiveTrue(@RequestParam Integer year) {
        return ResponseEntity.ok(usersService.findByCreatedAtAndIsActiveTrue(year));
    }

    @Operation(summary = "Obtener el numero de usuarios", description = "Devuelve el numero de usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Numero de usuarios obtenido con éxito")
    })
    @GetMapping("/all/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = usersService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear un usuario particular", description = "Crea un nuevo particular")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. contraseña débil) o email ya registrado")
    })
    @PostMapping("register/particular")
    public ResponseEntity<Users> registerParticular(@Valid @RequestBody ParticularRequestDTO dto) {
        Users newUser = usersService.registerParticular(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Operation(summary = "Crear un administrador", description = "Crea un nuevo administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. contraseña débil) o email ya registrado")
    })
    @PostMapping("/admin")
    public ResponseEntity<Users> createAdmin(@Valid @RequestBody Users user) {
        Users savedAdmin = usersService.createAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    @Operation(summary = "Crear un usuario company", description = "Crea un nuevo usuario empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario empresa creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. contraseña débil) o email ya registrado")
    })
    @PostMapping("/register/company")
    public ResponseEntity<Users> registerCompany(@Valid @RequestBody CompanyRequestDTO dto) {
        Users newUser = usersService.registerCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
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

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @Valid @RequestBody Users user) {
        Users updatedUser = usersService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    

}
