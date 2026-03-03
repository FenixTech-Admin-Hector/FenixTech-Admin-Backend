package com.proyecto.fenixtech.model.json;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialMetrics implements Serializable {
    private Integer itemsDonated = 0;
    private Integer itemsSoldDiscounted = 0;

}