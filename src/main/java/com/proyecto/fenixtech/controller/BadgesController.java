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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.Badges;
import com.proyecto.fenixtech.service.BadgesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Obtener insignia por ID", description = "Devuelve una insignia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Badges> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findById(id));
    }

    @Operation(summary = "Obtener insignias por nombre", description = "Devuelve una lista de insignias que contengan una cadena de texto en su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias encontradas con éxito")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Badges>> findByBadgeName(@RequestParam(required = true) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(badgesService.findByBadgeName(name));
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

}
