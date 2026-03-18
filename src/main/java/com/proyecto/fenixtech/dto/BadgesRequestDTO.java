package com.proyecto.fenixtech.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BadgesRequestDTO {
    @NotBlank(message = "El nombre de la insignia es obligatorio")
    private String badgeName;

    @NotBlank(message = "La URL del icono es obligatoria")
    private MultipartFile iconUrl;
}
