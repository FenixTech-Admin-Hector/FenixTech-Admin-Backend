package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Proposals;


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
}




