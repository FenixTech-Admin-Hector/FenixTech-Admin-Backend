package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.http.MediaType; 
import org.springframework.web.bind.annotation.ModelAttribute;

import com.proyecto.fenixtech.dto.BadgesRequestDTO;
import com.proyecto.fenixtech.model.Badges;
import com.proyecto.fenixtech.service.BadgesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Badges", description = "API para gestión de badges")
@RequestMapping("/badges")
@RestController
public class BadgesController {
    @Autowired
    private BadgesService badgesService;

    @Operation(summary = "Obtener todas las insignias", description = "Devuelve una lista de todas las insignias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Badges>> findAllBadges() {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findAllBadges());
    }

    @Operation(summary = "Obtener todas las insignias activas", description = "Devuelve una lista con todas las insignias activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias activas obtenidas con éxito")
    })
    @GetMapping("/active")
    public ResponseEntity<List<Badges>> findAllActiveBadges() {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findByIsActiveTrue());
    }

    @Operation(summary = "Obtener insignia por ID", description = "Devuelve una insignia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Badges> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findById(id));
    }

    @Operation(summary = "Obtener insignia activa por ID", description = "Devuelve una insignia activa por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia activa obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    @GetMapping("/active/{id}")
    public ResponseEntity<Badges> findActiveById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findByIdAndIsActiveTrue(id));
    }

    @Operation(summary = "Obtener insignias por nombre", description = "Devuelve una lista de insignias que contengan una cadena de texto en su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias encontradas con éxito")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Badges>> findByBadgeName(@RequestParam(required = true) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findByBadgeName(name));
    }

    @Operation(summary = "Obtener insignias activas por nombre", description = "Devuelve una lista de insignias activas que contengan una cadena de texto en su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias activas encontradas con éxito")
    })
    @GetMapping("/active/search")
    public ResponseEntity<List<Badges>> findByBadgeNameTrue(@RequestParam(required = true) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findByBadgeNameTrue(name));
    }

    @Operation(summary = "Obtener el número total de insignias", description = "Devuelve el número total de insignias registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de insignias obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = badgesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener el número total de insignias activas", description = "Devuelve el número total de insignias activas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de insignias activas obtenido con éxito")
    })
    @GetMapping("/active/count")
    public ResponseEntity<Map<String, Object>> countActive() {
        Long countActive = badgesService.countActive();
        Long count = badgesService.count();
        Long countInactive = badgesService.count() - badgesService.countActive();
        Map<String, Object> response = new HashMap<>();
        response.put("total", count);
        response.put("activas", countActive);
        response.put("inactivas", countInactive);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Eliminar una insignia por ID", description = "Elimina una insignia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Insignia eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        badgesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Crear una insignia", description = "Crea una nueva insignia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insignia creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Badges> create(@Valid @ModelAttribute BadgesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(badgesService.save(dto));
    }

    @Operation(summary = "Actualizar una insignia por ID", description = "Actualiza una insignia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Badges> update(@PathVariable Integer id, @Valid @ModelAttribute BadgesRequestDTO dto) {
        return ResponseEntity.ok(badgesService.update(id, dto));
    }

}
