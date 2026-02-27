package com.proyecto.fenixtech.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CompanyBadgeId implements Serializable {
    private Integer companyId;
    private Integer badgeId;
}
