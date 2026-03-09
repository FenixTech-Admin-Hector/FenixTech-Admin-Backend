package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.model.Users;

@Service
public class ProposalsService {
    @Autowired
    private ProposalsRepository proposalsRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Proposals> findAllProposals() {
        return proposalsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Proposals findById(Integer id) {
        return proposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Proposals> findByUserId(Integer id) {
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return proposalsRepository.findByRequester_UserId(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return proposalsRepository.count();
    }

    @Transactional
    public Proposals save(Proposals proposal) {
        System.out.println("DEBUG: Proposal recibida -> " + proposal);
        System.out.println("DEBUG: Requester -> " + proposal.getRequester());
        
        if (proposal.getRequester() == null || proposal.getRequester().getUserId() == null) {
            throw new IllegalArgumentException("El ID del solicitante (userId) es obligatorio");
        }

        Integer userId = proposal.getRequester().getUserId();
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        proposal.setRequester(user);

        return proposalsRepository.save(proposal);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!proposalsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Propuesta no encontrada con id: " + id);
        }
        proposalsRepository.deleteById(id);
    }

    @Transactional
    public Proposals update(Integer id, Proposals proposal) {
        Proposals existingProposal = proposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));

        existingProposal.setTitle(proposal.getTitle());
        existingProposal.setDescription(proposal.getDescription());
        existingProposal.setStatus(proposal.getStatus());

        return proposalsRepository.save(existingProposal);
    }
}
