package com.proyecto.fenixtech.admin.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Dashboard", description = "Endpoint para obtener las métricas globales del panel")
@RequestMapping("/api/admin/stats")
@RestController
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @Operation(summary = "Obtener estadísticas globales", description = "Calcula totales e ingresos para las tarjetas y gráficos del Dashboard.")
    @GetMapping
    public ResponseEntity<AdminDashboardStatsDTO> getGlobalStats() {
        return ResponseEntity.ok(adminDashboardService.getDashboardStats());
    }
}