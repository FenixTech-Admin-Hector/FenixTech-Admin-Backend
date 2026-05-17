package com.proyecto.fenixtech.admin.proposals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Proposals;

@Service
public class AdminProposalsService {

    @Autowired
    private AdminProposalsRepository adminProposalsRepository;

    @Transactional(readOnly = true)
    public List<Proposals> findAllProposals() {
        return adminProposalsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Proposals findById(Integer id) {
        return adminProposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));
    }

    @Transactional
    public void deleteById(Integer id) {
        Proposals proposal = adminProposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));
        
        // Al ser Admin, no necesitamos comprobar si somos el dueño de la propuesta.
        adminProposalsRepository.delete(proposal);
    }
}