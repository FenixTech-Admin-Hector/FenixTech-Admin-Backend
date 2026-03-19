package com.proyecto.fenixtech.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequestUpdateDTO {
    private String title;
    
    @Size(max = 200)
    private String description;
    
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double price;
    
    @Min(value = 1, message = "El stock mínimo debe ser 1")
    private Integer stock;

    private ConditionStatus conditionStatus;

    private ListingType listingType;

    private String street;

    private String city;

    private String region;

    @Size(min = 5, max = 5)
    private String zipCode;

    private String country;

    private Integer subcategoryId;

    private List<MultipartFile> newImages;
}