package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.model.enums.ProposalStatus;

public interface ProposalsRepository extends JpaRepository<Proposals, Integer> {
    
    List<Proposals> findByRequester_UserId(Integer id);

    List<Proposals> findByStatus(ProposalStatus status);



}