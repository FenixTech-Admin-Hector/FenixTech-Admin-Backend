package com.proyecto.fenixtech.admin.proposals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Proposals;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Proposals", description = "API exclusiva para la moderación de solicitudes (demandas) por el Administrador")
@RequestMapping("/admin/proposals")
@RestController
public class AdminProposalsController {

    @Autowired
    private AdminProposalsService adminProposalsService;

    @Operation(summary = "Obtener todas las solicitudes", description = "Lista todas las solicitudes cruzadas con el usuario para la tabla de Figma.")
    @GetMapping
    public ResponseEntity<List<Proposals>> findAllAdmin() {
        return ResponseEntity.ok(adminProposalsService.findAllProposals());
    }

    @Operation(summary = "Ver detalle de solicitud", description = "Devuelve la información detallada de una solicitud concreta para su revisión exhaustiva.")
    @GetMapping("/{id}")
    public ResponseEntity<Proposals> findByIdAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(adminProposalsService.findById(id));
    }

    @Operation(summary = "Eliminar solicitud", description = "Borra la solicitud de la base de datos si se considera inválida u ofensiva.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposalAdmin(@PathVariable Integer id) {
        adminProposalsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}