package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.proyecto.fenixtech.dto.AuthResponseDTO;
import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.LoginRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
import com.proyecto.fenixtech.service.AuthenticatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@Tag(name = "Authentication", description = "Endpoints para registro y login")
@RestController
@RequestMapping("/auth")
public class AuthenticatorController {
    
    @Autowired
    private AuthenticatorService service;

    @Operation(summary = "Registro de usuario particular")
    @PostMapping(value = "/register/particular")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody ParticularRequestDTO dto) {
        return ResponseEntity.ok(service.registerParticular(dto));
    }

    @Operation(summary = "Registro de empresa")
    @PostMapping("/register/company")
    public ResponseEntity<AuthResponseDTO> registerCompany(@Valid @RequestBody CompanyRequestDTO dto) {
        return ResponseEntity.ok(service.registerCompany(dto));
    }

    @Operation(summary = "Inicio de sesión")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(service.authenticate(dto));
    }
}