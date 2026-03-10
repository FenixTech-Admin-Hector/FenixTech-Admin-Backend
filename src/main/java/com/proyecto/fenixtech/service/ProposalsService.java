package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.dto.ProposalDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.ProposalStatus;
import com.proyecto.fenixtech.model.enums.Rol;

@Service
public class ProposalsService {
    @Autowired
    private ProposalsRepository proposalsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;


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
    public Proposals save(ProposalDTO dto) {
        Users user = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(dto.getUserId(), Rol.ADMIN)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Proposals proposal = new Proposals();
        proposal.setTitle(dto.getTitle());
        proposal.setDescription(dto.getDescription());
        proposal.setRequester(user); 
        proposal.setCategory(category);
        proposal.setStatus(ProposalStatus.OPEN);

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
