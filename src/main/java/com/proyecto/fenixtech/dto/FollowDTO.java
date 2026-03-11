package com.proyecto.fenixtech.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FollowDTO {
    @NotNull(message = "El ID del seguidor es obligatorio")
    private Integer followerId; 

    @NotNull(message = "El ID del usuario a seguir es obligatorio")
    private Integer following;
}
