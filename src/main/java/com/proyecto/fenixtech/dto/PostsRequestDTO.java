package com.proyecto.fenixtech.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostsRequestDTO {
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El contenido es obligatorio")
    private String body;

    private Integer userId;
    
    private List<MultipartFile> imagesUrls;

}
