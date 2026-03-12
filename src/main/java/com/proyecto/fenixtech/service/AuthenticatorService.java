package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatorService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersService usersService;

    // 1. REGISTRO: Crea el usuario y le da su primer Token
    public String registerParticular(ParticularRequestDTO dto) {
        // Delegamos la creación compleja a tu UsersService
        Users user = usersService.registerParticular(dto);
        // Generamos el token con el usuario ya creado
        return jwtService.generateToken(user);
    }

    public String registerCompany(CompanyRequestDTO dto) {
        // Delegamos la creación de empresa, métricas y dirección a tu UsersService
        Users user = usersService.registerCompany(dto);
        // Generamos el token
        return jwtService.generateToken(user);
    }

    public String authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        var user = usersRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow();

        return jwtService.generateToken(user);
    }

}
