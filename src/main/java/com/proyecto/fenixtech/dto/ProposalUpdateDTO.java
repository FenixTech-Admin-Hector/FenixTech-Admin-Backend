package com.proyecto.fenixtech.dto;

import com.proyecto.fenixtech.model.enums.ProposalStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProposalUpdateDTO {
    @NotBlank(message = "El título es obligatorio")
    private String title;
    @NotBlank(message = "La descripción es obligatoria")
    private String description;
    private Integer categoryId;
    private ProposalStatus status; // El campo que le interesa al ADMIN
}