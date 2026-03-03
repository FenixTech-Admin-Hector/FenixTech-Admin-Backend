package com.proyecto.fenixtech.model.json;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImpactMetrics implements Serializable {
    private EnvironmentalMetrics environmental; 
    private SocialMetrics social; 
}
