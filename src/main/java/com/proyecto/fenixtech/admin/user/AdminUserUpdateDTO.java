package com.proyecto.fenixtech.admin.user;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminUserUpdateDTO {
    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", message = "El email está en un formato incorrecto")
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    // Permite al admin subir una nueva foto si la anterior era inapropiada
    private MultipartFile userImg;

    private String description;
}