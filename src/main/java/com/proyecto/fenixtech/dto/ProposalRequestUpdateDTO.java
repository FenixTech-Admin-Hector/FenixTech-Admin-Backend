package com.proyecto.fenixtech.dto;

import com.proyecto.fenixtech.model.enums.ProposalStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProposalRequestUpdateDTO {
    @NotBlank(message = "El título es obligatorio")
    private String title;
    @NotBlank(message = "La descripción es obligatoria")
    private String description;
    private Integer categoryId;
    private ProposalStatus status;
}