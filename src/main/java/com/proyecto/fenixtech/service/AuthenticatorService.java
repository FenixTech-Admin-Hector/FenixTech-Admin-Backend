package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.AuthResponseDTO;
import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.LoginRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.UsersRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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

    public AuthResponseDTO registerParticular(ParticularRequestDTO dto) {
        Users user = usersService.registerParticular(dto);
        String token = jwtService.generateToken(user);
        
        return AuthResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getUserId())
                .build();

                
    }

    public AuthResponseDTO registerCompany(CompanyRequestDTO dto) {
        Users user = usersService.registerCompany(dto);
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getUserId())
                .build();
    }

    public AuthResponseDTO authenticate(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        Users user = usersService.findByEmail(dto.getEmail(), true);

        String token = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getUserId())
                .build();
    }

}
