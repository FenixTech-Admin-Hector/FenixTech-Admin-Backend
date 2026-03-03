package com.proyecto.fenixtech.model.json;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentalMetrics implements Serializable {
    private Double totalEwasteSavedKg = 0.0; 
    private Double totalCo2SavedKg = 0.0;
}