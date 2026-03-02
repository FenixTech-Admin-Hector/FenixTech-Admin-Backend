package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Users;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service

public class CompaniesService {
    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Companies> findAllCompanies(){
        return companiesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Companies findById(Integer id){
        return companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id:" + id));
    }

    @Transactional(readOnly = true)
    public Companies findByUserId(Integer id){
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + id));

        return companiesRepository.findByUsersId(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario" + id+ "no está asociado a ninguna empresa"));
    }

    @Transactional(readOnly = true)
    public List<Companies> findByCompanyName(String name){
        return companiesRepository.findByCompanyNameContainingIgnoringCase(name);
    }

    @Transactional(readOnly = true)
    public List<Companies> findByReputationScoreGreaterThan(Integer reputationScore){
        return companiesRepository.findByReputationScoreIsGreaterThan(reputationScore);
    }

    @Transactional(readOnly = true)
    public List<Companies> findByReputationScoreLessThan(Integer reputationScore){
        return companiesRepository.findByReputationScoreIsLessThan(reputationScore);
    }

    @Transactional(readOnly = true)
    public Long count(){
        return companiesRepository.count();
    }





}
