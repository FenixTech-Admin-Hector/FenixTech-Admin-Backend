package com.proyecto.fenixtech.admin.proposal;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Proposals;

public interface AdminProposalsRepository extends JpaRepository<Proposals, Integer> {
    // JpaRepository ya incluye findAll(), findById() y delete() por defecto.
}