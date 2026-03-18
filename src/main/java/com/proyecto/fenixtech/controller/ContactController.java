package com.proyecto.fenixtech.controller;

import com.proyecto.fenixtech.dto.ContactRequestDTO;
import com.proyecto.fenixtech.model.Contact;
import com.proyecto.fenixtech.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Contact", description = "API para la gestión del formulario de contacto y solicitudes de artículos")
@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Operation(summary = "Obtener todas las solicitudes de contacto", description = "Devuelve una lista con todos los mensajes de contacto registrados en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitudes obtenidas con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public ResponseEntity<List<Contact>> findAll() {
        return ResponseEntity.ok(contactService.findAll());
    }

    @Operation(summary = "Obtener solicitud por ID", description = "Busca y devuelve una solicitud de contacto específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "La solicitud con ese ID no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Contact> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(contactService.findById(id));
    }

    @Operation(summary = "Enviar nueva solicitud de contacto", description = "Registra una nueva solicitud en el sistema vinculada al usuario actual y notifica automáticamente al administrador por email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada y correo enviado al administrador con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del formulario inválidos "),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "La categoría enviada no existe en la base de datos")
    })
    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody ContactRequestDTO dto) {
        Contact newContact = contactService.createContactAndNotify(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
    }
}