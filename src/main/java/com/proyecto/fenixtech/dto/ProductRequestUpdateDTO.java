package com.proyecto.fenixtech.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequestUpdateDTO {
    @NotBlank(message = "El título es obligatorio")
    private String title;
    
    @Size(max = 200)
    private String description;
    
    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double price;
    
    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 1, message = "El stock mínimo debe ser 1")
    private Integer stock;

    @NotNull(message = "El estado de condición es obligatorio")
    private ConditionStatus conditionStatus;

    @NotNull(message = "El tipo de publicación es obligatorio")
    private ListingType listingType;

    @NotBlank(message = "La calle es obligatoria")
    private String street;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "El código postal es obligatorio")
    @Size(min = 5, max = 5)
    private String zipCode;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    @NotNull(message = "El ID de la subcategoría es obligatorio")
    private Integer subcategoryId;

    private List<MultipartFile> newImages;
}