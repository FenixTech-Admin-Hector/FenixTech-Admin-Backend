package com.proyecto.fenixtech.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.CompanyBadgeId;
import com.proyecto.fenixtech.model.CompanyBadges;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.CompanyBadgesRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CompanyBadgesService {
    @Autowired
    private CompanyBadgesRepository companyBadgesRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Transactional(readOnly = true)
    public List<CompanyBadges> findAll() {
        return companyBadgesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CompanyBadges findById(Integer companyId, Integer badgeId) {
        CompanyBadgeId compositeId = new CompanyBadgeId(companyId, badgeId);
        
        return companyBadgesRepository.findById(compositeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Insignia no encontrada para la empresa " + companyId + " y badge " + badgeId));
    }

    @Transactional(readOnly = true)
    public List<CompanyBadges> findByCompanyId(Integer id) {    
        companiesRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Empresa no encontrada con id: " + id));
        return companyBadgesRepository.findByCompany_CompanyId(id);
    }

    @Transactional(readOnly = true)
    public List<CompanyBadges> findByAwardedAtBetween(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(java.time.LocalTime.MAX);
        return companyBadgesRepository.findByAwardedAtBetween(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return companyBadgesRepository.count();
    }



}
