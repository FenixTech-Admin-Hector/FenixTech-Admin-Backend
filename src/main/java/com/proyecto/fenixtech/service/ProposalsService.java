package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.dto.ProposalRequestPostDTO;
import com.proyecto.fenixtech.dto.ProposalRequestUpdateDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.ProposalStatus;

@Service
public class ProposalsService {
    @Autowired
    private ProposalsRepository proposalsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public List<Proposals> findAllProposals() {
        return proposalsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Proposals> findByStatus(ProposalStatus status) {
        return proposalsRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Proposals findById(Integer id) {
        return proposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Proposals> findByUserId(Integer id) {
        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para ver las propuestas de otro usuario");
        }

        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return proposalsRepository.findByRequester_UserId(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return proposalsRepository.count();
    }

    @Transactional
    public Proposals save(ProposalRequestPostDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Proposals proposal = new Proposals();
        proposal.setTitle(dto.getTitle());
        proposal.setDescription(dto.getDescription());
        proposal.setRequester(currentUser);
        proposal.setCategory(category);
        proposal.setStatus(ProposalStatus.OPEN);

        return proposalsRepository.save(proposal);
    }

    @Transactional
    public void deleteById(Integer id) {
        Proposals proposal = proposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada"));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !proposal.getRequester().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes eliminar una propuesta que no creaste.");
        }

        proposalsRepository.delete(proposal);
    }

    @Transactional
    public Proposals update(Integer id, ProposalRequestUpdateDTO dto) {
        Proposals existing = proposalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propuesta no encontrada con id: " + id));

        if (existing.getStatus() == ProposalStatus.FULFILLED) {
            throw new IllegalStateException("Esta propuesta ya ha sido completada y no puede modificarse.");
        }

        if (dto.getStatus() == ProposalStatus.FULFILLED && existing.getStatus() != ProposalStatus.FULFILLED) {
            existing.setStatus(dto.getStatus());

            emailService.sendProposalStatusEmail(
                    existing.getRequester().getEmail(),
                    existing.getTitle());
        } 

        return proposalsRepository.save(existing);
    }
}
