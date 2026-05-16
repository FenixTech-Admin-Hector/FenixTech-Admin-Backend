package com.proyecto.fenixtech.admin.user;

import com.proyecto.fenixtech.model.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponseDTO {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String userImg;
    private Rol role; 
    private String description;
}