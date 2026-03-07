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
public class FollowsId implements Serializable {
    private Integer followerId;
    private Integer followingId;
}

    

