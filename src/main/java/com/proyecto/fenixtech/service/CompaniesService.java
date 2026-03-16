package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.CompaniesRequestUpdateDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CompaniesService {
    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;


    @Transactional(readOnly = true)
    public List<Companies> findAll() {
        return companiesRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Companies> findAllCompanies() {
        return companiesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Companies findByIdActive(Integer id) {
        return companiesRepository.findByCompanyIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id:" + id));
    }

    @Transactional(readOnly = true)
    public Companies findByUserId(Integer id) {
        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para ver los datos de esta empresa");
        }
        
        usersRepository.findByUserIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + id));

        return companiesRepository.findByUser_UserIdAndIsActiveTrue(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("El usuario" + id + "no está asociado a ninguna empresa"));
    }

    @Transactional(readOnly = true)
    public List<Companies> findByCompanyName(String name) {
        return companiesRepository.findByCompanyNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    @Transactional(readOnly = true)
    public List<Companies> findByImpactFilters(Integer minReputation, Integer maxReputation, Double minCo2Saved,
            Integer minItemsDonated) {
        if (minReputation != null && minReputation < 0) {
            throw new IllegalArgumentException("La calificación mínima no puede ser negativa");
        }
        if (maxReputation != null && maxReputation < 0) {
            throw new IllegalArgumentException("La calificación máxima no puede ser negativa");
        }
        if (minReputation != null && maxReputation != null && minReputation > maxReputation) {
            throw new IllegalArgumentException("La calificación mínima no puede ser superior a la calificación máxima");
        }
        if (minCo2Saved != null && minCo2Saved < 0) {
            throw new IllegalArgumentException("El ahorro mínimo no puede ser negativo");
        }
        if (minItemsDonated != null && minItemsDonated < 0) {
            throw new IllegalArgumentException("El mínimo de artículos donados no puede ser negativo");
        }

        return companiesRepository.findByImpactFilters(minReputation, maxReputation, minCo2Saved, minItemsDonated);
    }

    @Transactional(readOnly = true)
    public List<Companies> findTop3ByReputationScore() {
        return companiesRepository.findTop3ByIsActiveTrueOrderByReputationScoreDesc();
    }

    @Transactional(readOnly = true)
    public Long count() {
        return companiesRepository.countByIsActiveTrue();
    }

    @Transactional
    public void deleteById(Integer id) {
        Companies company = companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !company.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta empresa.");
        }

        company.setIsActive(false);

        if (company.getUser() != null) {
            company.getUser().setIsActive(false);
        }

        companiesRepository.save(company);
    }

    @Transactional
    public Companies update(Integer id, CompaniesRequestUpdateDTO dto) {
        Companies company = companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") &&
                !company.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para editar esta empresa.");
        }
        company.setCompanyName(dto.getCompanyName());
        company.setCif(dto.getCif());
        company.setCompanyImg(dto.getCompanyImg());

        return companiesRepository.save(company);
    }

}
