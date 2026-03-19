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
public class ProductsRequestPostDTO {

    @NotBlank(message = "El título del producto es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String title;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    @NotNull(message = "El estado de condición es obligatorio (NEW, USED_GOOD, USED_FAIR)")
    private ConditionStatus conditionStatus;

    @NotNull(message = "El tipo de publicación es obligatorio (SALE, DONATION)")
    private ListingType listingType;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double price;

    @NotNull(message = "La cantidad de stock es obligatoria")
    @Min(value = 1, message = "El stock mínimo debe ser 1")
    private Integer stockQuantity;


    @NotBlank(message = "La calle es obligatoria")
    private String street;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "El código postal es obligatorio")
    @Size(min = 5, max = 5, message = "El código postal debe tener exactamente 5 dígitos")
    private String zipCode;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    @NotNull(message = "El ID de la subcategoría es obligatorio")
    private Integer subcategoryId;

    @NotNull(message = "El ID de la empresa es obligatorio")
    private Integer companyId;

    @NotNull(message = "El producto debe tener al menos una imagen")
    @Size(min = 1, message = "Debes proporcionar al menos una URL de imagen")
    private List<MultipartFile> imageUrls;
}

