package com.proyecto.fenixtech.controller;

import com.proyecto.fenixtech.model.Reviews;
import com.proyecto.fenixtech.service.ReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Reviews", description = "API para gestión de reviews")
@RequestMapping("/api/reviews")
@RestController
public class ReviewsController {

    @Autowired
    private ReviewsService reviewsService;

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
}
