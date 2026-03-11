package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyBadgesRequestDTO {
    @NotNull(message = "El ID de la empresa es obligatorio")
    private Integer companyId;

    @NotNull(message = "El ID de la insignia es obligatorio")
    private Integer badgeId;

}
