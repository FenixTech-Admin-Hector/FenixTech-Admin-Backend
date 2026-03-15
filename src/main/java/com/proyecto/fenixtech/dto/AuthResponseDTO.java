package com.proyecto.fenixtech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String email;
    private String role;
    private Integer userId;
}