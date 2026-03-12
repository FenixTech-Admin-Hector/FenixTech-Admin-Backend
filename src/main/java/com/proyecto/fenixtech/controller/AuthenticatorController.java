package com.proyecto.fenixtech.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
import com.proyecto.fenixtech.service.AuthenticatorService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticatorController {
    
    @Autowired
    AuthenticatorService service;

    @PostMapping("/register/particular")
    public ResponseEntity<Map<String, String>> register(@RequestBody ParticularRequestDTO dto) {
        String token = service.registerParticular(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register/company")
    public ResponseEntity<Map<String, String>> registerCompany(@RequestBody CompanyRequestDTO dto) {
        String token = service.registerCompany(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> request) {
        String token = service.authenticate(request.get("email"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}
