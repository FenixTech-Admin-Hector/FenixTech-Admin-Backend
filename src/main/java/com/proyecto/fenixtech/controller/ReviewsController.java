package com.proyecto.fenixtech.controller;

import com.proyecto.fenixtech.dto.ReviewsRequestDTO;
import com.proyecto.fenixtech.model.Reviews;
import com.proyecto.fenixtech.service.ReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Reviews", description = "API para gestión de reviews")
@RequestMapping("/reviews")
@RestController
public class ReviewsController {

    @Autowired
    private ReviewsService reviewsService;

    @Operation(summary = "Obtener todas las reviews", description = "Devuelve una lista de todas las reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Reviews>> findAllReviews() {
        return ResponseEntity.ok(reviewsService.findAllReviews());
    }

    @Operation(summary = "Obtener reviews de una empresa", description = "Devuelve una lista de reviews para una empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Reviews>> findReviewsByCompanyId(@PathVariable Integer companyId) {
        return ResponseEntity.ok(reviewsService.findReviewsByCompanyId(companyId));
    }

    @Operation(summary = "Obtener reviews por ID de usuario", description = "Devuelve una lista de reviews asociada a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reviews para el usuario")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reviews>> findReviewsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(reviewsService.findReviewsByUserId(userId));
    }

    @Operation(summary = "Obtener la media de valoraciones de una empresa", description = "Devuelve la media de todas las valoraciones para una empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/company/{companyId}/average")
    public ResponseEntity<Map<String, Double>> getAverageRatingByCompanyId(@PathVariable Integer companyId) {
        Double average = reviewsService.getAverageRatingByCompanyId(companyId);
        Map<String, Double> response = new HashMap<>();
        response.put("averageRating", average);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener el número total de reviews", description = "Devuelve el número total de reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de reviews obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countAllReviews() {
        Long count = reviewsService.countAllReviews();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear una review", description = "Crea una nueva review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Reviews> save(@Valid @RequestBody ReviewsRequestDTO review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewsService.save(review));
    }

    @Operation(summary = "Eliminar una review", description = "Elimina una review por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Review no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        reviewsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar una review", description = "Actualiza una review por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Review no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reviews> update(@PathVariable Integer id, @Valid @RequestBody ReviewsRequestDTO review) {
        Reviews updatedReview = reviewsService.update(id, review);
        return ResponseEntity.ok(updatedReview);
    }

}
