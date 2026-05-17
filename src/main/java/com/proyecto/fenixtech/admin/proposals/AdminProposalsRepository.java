package com.proyecto.fenixtech.admin.proposals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.model.enums.ProposalStatus; // El import del Enum

@Repository
public interface AdminProposalsRepository extends JpaRepository<Proposals, Integer> {
    
    long countByStatus(ProposalStatus status); 
}